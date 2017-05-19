package com.schibsted.httpserver.domain.authenticator;

import com.schibsted.httpserver.domain.authenticator.Session;
import com.schibsted.httpserver.domain.user.User;

import java.util.Optional;

public interface SessionService {

    void addSession(User user);
    Optional<Session> getSession(String userName);
    void deleteSession(String session);
    Boolean isValid(User user);

}
