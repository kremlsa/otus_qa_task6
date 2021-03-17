package service;

import utils.Parser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserService {
    private static final String CREATE_USER = "/user";
    private static final String GET_USER = "/user/";

    RequestSpecification spec;

    public void createContext(String url) {
        spec = given()
                .contentType(ContentType.JSON)
                .baseUri(url);
    }

    public Response addUserRequest(String userJSON) {
        return given()
                .spec(spec)
                .with()
                .body(userJSON)
                .when()
                .log().all()
                .post(CREATE_USER);
    }

    public Response getUserRequest(String username) {
        return given()
                .spec(spec)
                .with()
                .when()
                .log().all()
                .get(GET_USER + username);
    }
}
