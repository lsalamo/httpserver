package com.schibsted.httpserver.domain.result;

import com.schibsted.httpserver.domain.request.Request;

public class ResultError204 extends ResultError {

    public static final int STATUS = Request.HTTP_NO_CONTENT;
    public static final String TITLE = "No content";
    public static final String DESCRIPTION = "";

    public ResultError204() {
        super(STATUS, TITLE, DESCRIPTION);
    }
}
