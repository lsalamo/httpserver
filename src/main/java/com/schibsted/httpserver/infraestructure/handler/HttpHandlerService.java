package com.schibsted.httpserver.infraestructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.schibsted.httpserver.domain.request.Request;
import com.schibsted.httpserver.domain.result.ResultView;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

public abstract class HttpHandlerService implements HttpHandler {

    private UserInMemoryRepository userRepository;

    public UserInMemoryRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserInMemoryRepository userRepository) {
        this.userRepository = userRepository;
    }

    public HttpHandlerService(UserInMemoryRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setViewPage(HttpExchange httpExchange, int status, String title, String description, String page) throws IOException {
        Headers headersResponse = httpExchange.getResponseHeaders();
        headersResponse.add("Content-Type", "text/html");

        ResultView resultView = new ResultView();
        resultView.setHttpStatus(status);
        resultView.setTitle(title);
        resultView.setDescription(description);

        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(page);
        mustache.execute(writer, resultView).flush();
        String response = writer.toString();

        setView(httpExchange, status, response);
    }

    public void setViewResponse(HttpExchange httpExchange, int status, Object json) throws IOException {
        Headers headersResponse = httpExchange.getResponseHeaders();
        headersResponse.add("Content-Type", "application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        setView(httpExchange, status, objectMapper.writeValueAsString(json));
    }

    public void setViewResponse(HttpExchange httpExchange, int status, String title, String description) throws IOException {
        Headers headersResponse = httpExchange.getResponseHeaders();
        headersResponse.add("Content-Type", "application/json");

        ResultView resultView = new ResultView();
        resultView.setHttpStatus(status);
        resultView.setTitle(title);
        resultView.setDescription(description);

        ObjectMapper objectMapper = new ObjectMapper();
        String response = objectMapper.writeValueAsString(resultView);

        setView(httpExchange, status, response);
    }

    public void setView(HttpExchange httpExchange, int status, String response) throws IOException {
        httpExchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
