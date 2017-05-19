package com.schibsted.httpserver.infraestructure.handler;

import com.schibsted.httpserver.domain.request.Request;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpHandlerPageLogout extends HttpHandlerService {

    public static final String PAGE_LOGOUT_FILE = "logout.html";

    public HttpHandlerPageLogout(UserInMemoryRepository userRepository) {
        super(userRepository);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        setViewPage(httpExchange, Request.HTTP_UNAUTHORIZED, "", "", PAGE_LOGOUT_FILE);
    }

}
