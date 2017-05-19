package com.schibsted.httpserver.infraestructure.handler;

import com.schibsted.httpserver.domain.request.Request;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpHandlerPageNotFound extends HttpHandlerService {

    public static final String PAGE_NOT_FOUND_TITLE = "Page not found";
    public static final String PAGE_NOT_FOUND_DESCRIPTION = "The requested page was not found";
    public static final String PAGE_NOT_FOUND_FILE = "error.html";

    public HttpHandlerPageNotFound(UserInMemoryRepository userRepository) {
        super(userRepository);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        setViewPage(httpExchange, Request.HTTP_NOT_FOUND, PAGE_NOT_FOUND_TITLE, PAGE_NOT_FOUND_DESCRIPTION, PAGE_NOT_FOUND_FILE);
    }

}
