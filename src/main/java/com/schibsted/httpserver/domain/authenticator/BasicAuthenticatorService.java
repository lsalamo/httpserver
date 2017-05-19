package com.schibsted.httpserver.domain.authenticator;

import com.schibsted.httpserver.infraestructure.user.UserServiceImpl;
import com.schibsted.httpserver.domain.user.User;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import com.sun.net.httpserver.BasicAuthenticator;
import java.util.Optional;

public class BasicAuthenticatorService extends BasicAuthenticator {

    private UserInMemoryRepository userRepository;
    private final SessionService sessionService;

    public BasicAuthenticatorService(UserInMemoryRepository userRepository, SessionService sessionService) {
        super("httpserver");
        this.userRepository = userRepository;
        this.sessionService = sessionService;
    }

    @Override
    public boolean checkCredentials(String userName, String pass) {
        Boolean result = false;
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository);
        Optional<User> user = userServiceImpl.checkUserLogin(userName, pass);
        if (user.isPresent()) {
            result = sessionService.isValid(user.get());
        }
        return result;
    }

}
