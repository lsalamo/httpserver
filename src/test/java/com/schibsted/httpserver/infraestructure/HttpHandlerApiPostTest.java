package com.schibsted.httpserver.infraestructure;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.schibsted.httpserver.ContextServerTest;
import com.schibsted.httpserver.application.HttpServerService;
import com.schibsted.httpserver.domain.request.Request;
import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class HttpHandlerApiPostTest extends ContextServerTest {

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
    public void testHttpHandleApiUpdateUser() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
                formParam("user", "user1_UPDATE").
                formParam("pwd", "1234_UPDATE").
                formParam("role", Role.PAGE_1.toString() + UserInMemoryRepository.SEPARATOR_ROLE + Role.PAGE_2.toString()).
        when().
                post(Request.PREFIX_PATTERN_API_USERS + "/{userId}", 1).
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_OK);

        given().
                auth().preemptive().basic("user2", "1234").
        when().
                get(Request.PREFIX_PATTERN_API_USERS + "/{userId}", 1).
        then().
                statusCode(Request.HTTP_OK).
                body("id", is(1)).
                body("userName", is("user1_UPDATE"));
    }

    @Test
    public void testHttpHandleApiUpdateUserForbidden() throws Exception {
        given().
                auth().preemptive().basic("user1_UPDATE", "1234_UPDATE").
                formParam("user", "user1").
                formParam("pwd", "1234").
                formParam("role", Role.PAGE_1.toString() + UserInMemoryRepository.SEPARATOR_ROLE + Role.PAGE_2.toString()).
        when().
                post(Request.PREFIX_PATTERN_API_USERS + "/{userId}", 1).
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_FORBIDDEN);
    }

    @Test
    public void testHttpHandleApiUpdateUserBadRequest() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
                formParam("user", "user1_mod").
                formParam("pwd", "123456").
                formParam("role", Role.PAGE_1.toString() + UserInMemoryRepository.SEPARATOR_ROLE + Role.PAGE_2.toString()).
        when().
                post(Request.PREFIX_PATTERN_API_USERS + "/{userId}", "xxx").
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_BAD_REQUEST);
    }

    @Test
    public void testHttpHandleApiUpdateUserNoContent() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
                formParam("user", "user1000").
        when().
                post(Request.PREFIX_PATTERN_API_USERS + "/{userId}", "1000").
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_NO_CONTENT);
    }

    @Test
    public void testHttpHandleApiAddUser() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
                formParam("user", "user4").
                formParam("pwd", "1234").
                formParam("role", Role.ADMIN.toString()).
        when().
                post(Request.PREFIX_PATTERN_API_USERS).
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_CREATED);

        given().
                auth().preemptive().basic("user2", "1234").
        when().
                get(Request.PREFIX_PATTERN_API_USERS + "/{userId}", 4).
        then().
                statusCode(Request.HTTP_OK).
                body("userName", is("user4"));
    }

    @Test
    public void testHttpHandleApiAddUserForbidden() throws Exception {
        given().
                auth().preemptive().basic("user1_UPDATE", "1234_UPDATE").
                formParam("user", "user1").
                formParam("pwd", "1234").
                formParam("role", Role.ADMIN.toString()).
        when().
                post(Request.PREFIX_PATTERN_API_USERS).
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_FORBIDDEN);
    }

    @Test
    public void testHttpHandleApiInternalError() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
        when().
                delete(Request.PREFIX_PATTERN_API_USERS + "/{userId}", 1000).
        then().
                contentType(ContentType.JSON).
                statusCode(Request.HTTP_INTERNAL_ERROR);
    }


}