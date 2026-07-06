import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DadataApiTests {

    private static final String TOKEN = "ff5fcb554399f0bfcbee86636f1894100f17c839";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://suggestions.dadata.ru";
    }

    //POST ЗАПРОСЫ

    @Test
    public void testAddressSuggest() {
        String requestBody = "{ \"query\": \"москва кремль\" }";

        given()
            .header("Authorization", "Token " + TOKEN)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/address")
        .then()
            .statusCode(200)
            .body("suggestions[0].value", containsString("Москва"))
            .body("suggestions.size()", greaterThan(0));

        System.out.println("Test 1 (POST /address) PASSED!");
    }

    @Test
    public void testFioSuggest() {
        String requestBody = "{ \"query\": \"иван петров\" }";

        given()
            .header("Authorization", "Token " + TOKEN)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/fio")
        .then()
            .statusCode(200)
            .body("suggestions[0].value", containsString("Иван"))
            .body("suggestions.size()", greaterThan(0));

        System.out.println("Test 2 (POST /fio) PASSED!");
    }

    @Test
    public void testPartySuggest() {
        String requestBody = "{ \"query\": \"сбербанк\" }";

        given()
            .header("Authorization", "Token " + TOKEN)
            .header("Content-Type", "application/json")
            .body(requestBody)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/party")
        .then()
            .statusCode(200)
            .body("suggestions[0].value", containsString("СБЕРБАНК"))
            .body("suggestions.size()", greaterThan(0));

        System.out.println("Test 3 (POST /party) PASSED!");
    }

    //GET ЗАПРОС

    @Test
    public void testGetCityByIp() {
        given()
            .header("Authorization", "Token " + TOKEN)
            .queryParam("ip", "77.88.55.66")
        .when()
            .get("https://dadata.ru/api/v2/detectAddressByIp")
        .then()
            .statusCode(200);  // Проверяем только статус, т.к. location может быть null

        System.out.println("Test 4 (GET /detectAddressByIp) PASSED!");
    }
}