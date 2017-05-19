package com.schibsted.httpserver.domain.result;

import com.schibsted.httpserver.domain.request.Request;

public class ResultError500 extends ResultError {

    public static final int STATUS = Request.HTTP_INTERNAL_ERROR;
    public static final String TITLE = "Internal server error";
    public static final String DESCRIPTION = "The page cannot be displayed because an internal server error has occurred";

    public ResultError500() {
        super(STATUS, TITLE, DESCRIPTION);
    }

}
