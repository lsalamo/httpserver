package com.schibsted.httpserver.domain.result;

import com.schibsted.httpserver.domain.request.Request;

public class ResultError403 extends ResultError {

    public static final int STATUS = Request.HTTP_FORBIDDEN;
    public static final String TITLE = "Forbidden";
    public static final String DESCRIPTION = "The server understood the request but refuses to authorize it";

    public ResultError403() {
        super(STATUS, TITLE, DESCRIPTION);
    }

}
