package com.schibsted.httpserver.application;

import com.schibsted.httpserver.domain.authenticator.BasicAuthenticatorService;
import com.schibsted.httpserver.infraestructure.handler.*;
import com.schibsted.httpserver.infraestructure.session.SessionServiceInMemoryRepository;
import com.schibsted.httpserver.domain.request.Request;
import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerService {

    private HttpServer server;
    private static final int PORT = 8000;
    protected UserInMemoryRepository userRepository = new UserInMemoryRepository();
    protected SessionServiceInMemoryRepository sessionInMemoryRepository = new SessionServiceInMemoryRepository();

    public UserInMemoryRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserInMemoryRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new HttpHandlerPageNotFound(userRepository));
        server.createContext(Request.PREFIX_PATTERN_PAGE + "index", new HttpHandlerPageIndex(userRepository));
        server.createContext(Request.PREFIX_PATTERN_PAGE + "page1", new HttpHandlerPage(userRepository, Role.PAGE_1)).setAuthenticator(new BasicAuthenticatorService(userRepository, sessionInMemoryRepository));
        server.createContext(Request.PREFIX_PATTERN_PAGE + "page2", new HttpHandlerPage(userRepository, Role.PAGE_2)).setAuthenticator(new BasicAuthenticatorService(userRepository, sessionInMemoryRepository));
        server.createContext(Request.PREFIX_PATTERN_PAGE + "page3", new HttpHandlerPage(userRepository, Role.PAGE_3)).setAuthenticator(new BasicAuthenticatorService(userRepository, sessionInMemoryRepository));
        server.createContext(Request.PREFIX_PATTERN_PAGE_LOGOUT, new HttpHandlerPageLogout(userRepository));
        server.createContext(Request.PREFIX_PATTERN_API_USERS, new HttpHandlerApi(userRepository)).setAuthenticator(new BasicAuthenticatorService(userRepository, sessionInMemoryRepository));
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on http://localhost:8000/app/index");
    }

    public void stop() {
        if (server != null) server.stop(1);
    }

}
