import jdk.jfr.Description;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresinTest extends TestBase {

    @Test
    @Description("Test from the lesson")
    void loginTest() {
        String body = "{ \"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is(notNullValue()));
    }

    @Test
    @Description("Test from the lesson")
    void missingPasswordLoginTest() {
        String body = "{ \"email\": \"eve.holt@reqres.in\"}";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Registration is successful")
    public void registrationIsSuccessful() {
        String body = "{\"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"pistol\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is(notNullValue()));
    }

    @Test
    @DisplayName("The total and per page number of users is correct")
    public void numberOfUsersIsCorrect() {
        get("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12),
                        "per_page", is(6));
    }

    @Test
    @DisplayName("User can be found by id")
    public void userCanBeFoundById() {
        given()
                .pathParam("id", 12)
                .when()
                .get("/users/{id}")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.email", is("rachel.howell@reqres.in")
                , "data.first_name", is("Rachel")
                , "data.last_name", is("Howell"));
    }

    @Test
    @DisplayName("Not existing user isn't found")
    public void userCanNoteBeFound() {
        given()
                .pathParam("id", 13)
                .when()
                .get("/users/{id}")
                .then()
                .log().status()
                .log().body()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    @DisplayName("User can be successfully created")
    public void userCanBeCreated() {
        String body = "{\"name\": \"Aleksei\",\n" +
                "    \"job\": \"QA engineer\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Aleksei"),
                        "job", is("QA engineer"));
    }

}