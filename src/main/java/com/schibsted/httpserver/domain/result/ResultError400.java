package com.schibsted.httpserver.domain.result;

import com.schibsted.httpserver.domain.request.Request;

public class ResultError400 extends ResultError {

    public static final int STATUS = Request.HTTP_BAD_REQUEST;
    public static final String TITLE = "Bad Request";
    public static final String DESCRIPTION = "";

    public ResultError400() {
        super(STATUS, TITLE, DESCRIPTION);
    }

}
