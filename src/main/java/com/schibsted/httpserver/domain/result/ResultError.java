package com.schibsted.httpserver.domain.result;

public class ResultError {



    private int status;
    private String title;
    private String description;

    public ResultError(int status, String title, String description) {
        this.status = status;
        this.title = title;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
