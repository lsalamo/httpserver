package com.schibsted.httpserver.infraestructure.handler;

import com.schibsted.httpserver.domain.request.Request;
import com.schibsted.httpserver.domain.result.ResultError;
import com.schibsted.httpserver.domain.result.ResultError403;
import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.domain.user.User;
import com.schibsted.httpserver.infraestructure.user.AdministratorServiceImpl;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import com.schibsted.httpserver.infraestructure.user.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.Optional;

public class HttpHandlerPage extends HttpHandlerService {

    private final Role role;

    public HttpHandlerPage(UserInMemoryRepository userRepository, Role role) {
        super(userRepository);
        this.role = role;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ResultError resultError = null;
        String page = Optional.of(StringUtils.replaceAll(httpExchange.getRequestURI().getPath(), Request.PREFIX_PATTERN_PAGE, "") + ".html").orElse("index.html");
        String description = "";
        if (role != Role.NONE) {
            String userName = httpExchange.getPrincipal().getUsername();
            UserServiceImpl userServiceImpl = new UserServiceImpl(getUserRepository());
            AdministratorServiceImpl administratorService = null;
            Optional<User> user = userServiceImpl.getUserByName(userName);
            if (user.isPresent()) {
                if (user.get().isAdmin()) administratorService = new AdministratorServiceImpl(getUserRepository());
            }
            Boolean isUserPermissions = user.get().isAdmin() ? administratorService.isUserPermissions(userName, role) : userServiceImpl.isUserPermissions(userName, role);
            if (isUserPermissions) {
                description = "Hello " + userName;
                setViewPage(httpExchange, Request.HTTP_OK, "", description, page);
            } else {
                resultError = new ResultError403();
                setViewPage(httpExchange, resultError.getStatus(), resultError.getTitle(), resultError.getDescription(), "error.html");
            }
        }
        if (resultError == null) setViewPage(httpExchange, Request.HTTP_OK, "", description, page);
    }

}
