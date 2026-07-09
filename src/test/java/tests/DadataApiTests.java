package tests;
import config.ConfigReader;
import dto.SuggestionRequest;
import dto.SuggestionResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DadataApiTests {

    private static final String TOKEN = ConfigReader.getToken();

    private static RequestSpecification getRequestSpec() {
        return given()
            .baseUri("https://suggestions.dadata.ru")
            .header("Authorization", "Token " + TOKEN)
            .header("Content-Type", "application/json");
    }

    @Test
    public void testAddressSuggest() {
        SuggestionRequest request = new SuggestionRequest("москва кремль");

        SuggestionResponse response = getRequestSpec()
            .body(request)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/address")
        .then()
            .statusCode(200)
            .extract()
            .as(SuggestionResponse.class);

        assertThat(response.getSuggestions(), notNullValue());
        assertThat(response.getSuggestions().size(), greaterThan(0));
        assertThat(response.getSuggestions().get(0).getValue(), containsString("Москва"));

        System.out.println("Test 1 (POST /address) PASSED!");
    }

    @Test
    public void testFioSuggest() {
        SuggestionRequest request = new SuggestionRequest("иван петров");

        SuggestionResponse response = getRequestSpec()
            .body(request)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/fio")
        .then()
            .statusCode(200)
            .extract()
            .as(SuggestionResponse.class);

        assertThat(response.getSuggestions(), notNullValue());
        assertThat(response.getSuggestions().size(), greaterThan(0));
        assertThat(response.getSuggestions().get(0).getValue(), containsString("Иван"));

        System.out.println("Test 2 (POST /fio) PASSED!");
    }

    @Test
    public void testPartySuggest() {
        SuggestionRequest request = new SuggestionRequest("сбербанк");

        SuggestionResponse response = getRequestSpec()
            .body(request)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/party")
        .then()
            .statusCode(200)
            .extract()
            .as(SuggestionResponse.class);

        assertThat(response.getSuggestions(), notNullValue());
        assertThat(response.getSuggestions().size(), greaterThan(0));
        assertThat(response.getSuggestions().get(0).getValue(), containsString("СБЕРБАНК"));

        System.out.println("Test 3 (POST /party) PASSED!");
    }

    @Test
    public void testGetCityByIp() {
        given()
            .baseUri("https://dadata.ru")
            .header("Authorization", "Token " + TOKEN)
            .queryParam("ip", "77.88.55.66")
        .when()
            .get("/api/v2/detectAddressByIp")
        .then()
            .statusCode(200);

        System.out.println("Test 4 (GET /detectAddressByIp) PASSED!");
    }
}