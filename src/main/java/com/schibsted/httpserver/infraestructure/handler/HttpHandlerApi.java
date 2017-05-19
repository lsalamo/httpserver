package com.schibsted.httpserver.infraestructure.handler;

import com.schibsted.httpserver.domain.request.Request;
import com.schibsted.httpserver.domain.result.*;
import com.schibsted.httpserver.domain.user.User;
import com.schibsted.httpserver.infraestructure.user.AdministratorServiceImpl;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import com.schibsted.httpserver.infraestructure.user.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

public class HttpHandlerApi extends HttpHandlerService {

    public HttpHandlerApi(UserInMemoryRepository userRepository) {
        super(userRepository);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ResultError resultError;
        String userName = httpExchange.getPrincipal().getUsername();
        String requestMethod = httpExchange.getRequestMethod();
        String page = httpExchange.getRequestURI().getPath();

        AdministratorServiceImpl administratorService = null;
        UserServiceImpl userServiceImpl = new UserServiceImpl(getUserRepository());
        Optional<User> user = userServiceImpl.getUserByName(userName);
        if (user.isPresent()) {
            if (user.get().isAdmin()) administratorService = new AdministratorServiceImpl(getUserRepository());
        }

        Optional<Integer> id = getId(page);
        switch (requestMethod) {
            case Request.REQUEST_METHOD_GET:
                if (id.isPresent()) {
                    if (id.get() > 0) {
                        Optional<User> userOptional = user.get().isAdmin() ? administratorService.getUserById(id.get()) : userServiceImpl.getUserById(id.get());
                        if (userOptional.isPresent()) {
                            setViewResponse(httpExchange, Request.HTTP_OK, userOptional.get());
                        } else {
                            resultError = new ResultError204();
                            setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                        }
                    } else if (id.get() == 0) {
                        Collection<User> json = user.get().isAdmin() ? administratorService.getUsers() : userServiceImpl.getUsers();
                        setViewResponse(httpExchange, Request.HTTP_OK, json);
                    }
                } else {
                    resultError = new ResultError400();
                    setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                }
                break;
            case Request.REQUEST_METHOD_POST:
                if (id.isPresent()) {
                    if (id.get() > 0) {
                        Optional<User> userOptional;
                        if (user.get().isAdmin()) {
                            Map<String, String> postQueryMap = getPostQueryMap(getPostAsString(httpExchange.getRequestBody()));
                            userOptional = administratorService.updateUser(id.get(), postQueryMap);
                            if (userOptional.isPresent()) {
                                setViewResponse(httpExchange, Request.HTTP_OK, userOptional.get());
                            } else {
                                resultError = new ResultError204();
                                setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                            }
                        } else {
                            resultError = new ResultError403();
                            setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                        }
                    } else if (id.get() == 0) {
                        Optional<User> userOptional;
                        if (user.get().isAdmin()) {
                            Map<String, String> postQueryMap = getPostQueryMap(getPostAsString(httpExchange.getRequestBody()));
                            userOptional = administratorService.addUser(postQueryMap);
                            if (userOptional.isPresent())
                                setViewResponse(httpExchange, Request.HTTP_CREATED, userOptional.get());
                            else {
                                resultError = new ResultError400();
                                setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                            }
                        } else {
                            resultError = new ResultError403();
                            setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                        }
                    }
                } else {
                    resultError = new ResultError400();
                    setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                }
                break;
            default:
                resultError = new ResultError500();
                setViewResponse(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription());
                break;
        }
    }

    private static Map<String, String> getPostQueryMap(String post) {
        String[] params = post.split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private String getPostAsString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private Optional<Integer> getId(String page) {
        Matcher matcher = Request.PATTERN_API.matcher(page);
        if (matcher.find()) {
            try {
                return Optional.of(Integer.parseInt(matcher.group(1)));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else {
            return Optional.of(0);
        }
    }

}




