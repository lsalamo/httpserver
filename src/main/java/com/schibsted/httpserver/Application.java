package com.schibsted.httpserver;

import com.schibsted.httpserver.application.HttpServerService;

public class Application {

    public static void main(String[] args) throws Exception {
        new HttpServerService().start();
    }

}
