package com.schibsted.httpserver.infraestructure;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.schibsted.httpserver.ContextServerTest;
import com.schibsted.httpserver.application.HttpServerService;
import com.schibsted.httpserver.domain.request.Request;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

public class HttpHandlerApiGetTest extends ContextServerTest {

    private static final HttpServerService SERVICE = new HttpServerService();

    @BeforeClass
    public static void beforeClass() throws IOException {
        RestAssured.port = 8000;
        RestAssured.baseURI = "http://localhost";
        SERVICE.start();
    }

    @AfterClass
    public static void afterClass() {
        SERVICE.stop();
    }

    @Test
    public void testHttpHandleApiGetUser() throws Exception {
        given().
                auth().preemptive().basic("user1", "1234").
        when().
                get(Request.PREFIX_PATTERN_API_USERS + "/{userId}", 1).
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_OK).
                body("userName", is("user1"));
    }

    @Test
    public void testHttpHandleApiGetUsers() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
        when().
                get(Request.PREFIX_PATTERN_API_USERS).
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_OK).
                body("userName", hasItems("user1", "user2", "user3"));
    }

    @Test
    public void testHttpHandleApiGetUserBadRequest() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
        when().
                get(Request.PREFIX_PATTERN_API_USERS + "/{userId}", "xxx").
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_BAD_REQUEST);
    }

    @Test
    public void testHttpHandleApiGetUserNoContent() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
        when().
                get(Request.PREFIX_PATTERN_API_USERS + "/{userId}", "1000").
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_NO_CONTENT);
    }


}