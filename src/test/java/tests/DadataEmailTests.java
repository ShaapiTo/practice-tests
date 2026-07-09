package tests;
import config.ConfigReader;
import dto.EmailSuggestionResponse;
import dto.SuggestionRequest;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DadataEmailTests {

    private static final String TOKEN = ConfigReader.getToken();

    private static RequestSpecification getRequestSpec() {
        return given()
            .baseUri("https://suggestions.dadata.ru")
            .header("Authorization", "Token " + TOKEN)
            .header("Content-Type", "application/json");
    }

    @Test
    public void testEmailSuggestStatus() {
        SuggestionRequest request = new SuggestionRequest("ivan");

        EmailSuggestionResponse response = getRequestSpec()
            .body(request)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/email")
        .then()
            .statusCode(200)
            .extract()
            .as(EmailSuggestionResponse.class);

        assertThat(response.getSuggestions(), notNullValue());
        assertThat(response.getSuggestions().size(), greaterThan(0));
        assertThat(response.getSuggestions().get(0).getValue(), containsString("ivan"));
        assertThat(response.getSuggestions().get(0).getData().getLocal(), containsString("ivan"));

        System.out.println("Test 1 (POST /email) PASSED!");
    }

    @Test
    public void testEmailSuggestWithDifferentQueries() {
        String[] queries = {"petr", "alex", "mariya"};

        for (String query : queries) {
            SuggestionRequest request = new SuggestionRequest(query);

            given()
                .spec(getRequestSpec())
                .body(request)
            .when()
                .post("/suggestions/api/4_1/rs/suggest/email")
            .then()
                .statusCode(200)
                .body("suggestions[0].value", containsString(query));
        }

        System.out.println("Test 2 (POST /email with different queries) PASSED!");
    }

    @Test
    public void testEmailSuggestWithLimit() {
        SuggestionRequest request = new SuggestionRequest("ivan", 3);

        EmailSuggestionResponse response = getRequestSpec()
            .body(request)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/email")
        .then()
            .statusCode(200)
            .extract()
            .as(EmailSuggestionResponse.class);

        assertThat(response.getSuggestions().size(), lessThanOrEqualTo(3));

        System.out.println("Test 3 (POST /email with limit) PASSED!");
    }

    @Test
    public void testEmailSuggestEmptyQuery() {
        SuggestionRequest request = new SuggestionRequest("");

        EmailSuggestionResponse response = getRequestSpec()
            .body(request)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/email")
        .then()
            .statusCode(200)
            .extract()
            .as(EmailSuggestionResponse.class);

        assertThat(response.getSuggestions().size(), is(0));

        System.out.println("Test 4 (POST /email with empty query) PASSED!");
    }

    @Test
    public void testEmailSuggestUnauthorized() {
        SuggestionRequest request = new SuggestionRequest("ivan");

        given()
            .baseUri("https://suggestions.dadata.ru")
            .header("Authorization", "Token invalid_token")
            .header("Content-Type", "application/json")
            .body(request)
        .when()
            .post("/suggestions/api/4_1/rs/suggest/email")
        .then()
            .statusCode(403);

        System.out.println("Test 5 (POST /email unauthorized) PASSED!");
    }
}