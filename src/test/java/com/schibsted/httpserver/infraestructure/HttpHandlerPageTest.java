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

public class HttpHandlerPageTest extends ContextServerTest {

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
    public void testHttpHandlePageIndex() throws Exception {
        given().
        when().
                get(Request.PREFIX_PATTERN_PAGE + "index").
        then().
                statusCode(Request.HTTP_OK);
    }

    @Test
    public void testHttpHandlePageNotFound() throws Exception {
        given().
        when().
                get("/xxx").
        then().
                contentType(ContentType.HTML).
                statusCode(Request.HTTP_NOT_FOUND);
    }

    @Test
    public void testHttpHandlePageRole() throws Exception {
        given().
                auth().preemptive().basic("user1", "1234").
        when().
                get(Request.PREFIX_PATTERN_PAGE + "page1").
        then().
                contentType(ContentType.HTML).
                statusCode(Request.HTTP_OK);
    }

    @Test
    public void testHttpHandlePageRoleForbidden() throws Exception {
        given().
                auth().preemptive().basic("user1", "1234").
        when().
                get(Request.PREFIX_PATTERN_PAGE + "page2").
        then().
                contentType(ContentType.HTML).
                statusCode(Request.HTTP_FORBIDDEN);
    }

    @Test
    public void testHttpHandlePageRoleAdmin() throws Exception {
        given().
                auth().preemptive().basic("user2", "1234").
        when().
                get(Request.PREFIX_PATTERN_PAGE + "page1").
        then().
                contentType(ContentType.HTML).
                statusCode(Request.HTTP_OK);
    }

    @Test
    public void testHttpHandlePageLogout() throws Exception {
        given().
        when().
                get(Request.PREFIX_PATTERN_PAGE + "logout").
        then().
                contentType(ContentType.HTML).
                statusCode(Request.HTTP_UNAUTHORIZED);
    }

}