package com.schibsted.httpserver.domain.request;

import java.util.regex.Pattern;

public class Request {

    public static final String PREFIX_PATTERN_PAGE = "/app/";
    public static final String PREFIX_PATTERN_PAGE_LOGOUT = "/app/logout";
    public static final String PREFIX_PATTERN_API_USERS = "/api/users";

    public static final Pattern PATTERN_API = Pattern.compile(PREFIX_PATTERN_API_USERS + "/(.+)$");

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";

    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_ERROR = 500;

}
