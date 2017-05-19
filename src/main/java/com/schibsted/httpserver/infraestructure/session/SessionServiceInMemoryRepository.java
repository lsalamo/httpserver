package com.schibsted.httpserver.infraestructure.session;

import com.schibsted.httpserver.domain.authenticator.Session;
import com.schibsted.httpserver.domain.authenticator.SessionService;
import com.schibsted.httpserver.domain.user.User;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class SessionServiceInMemoryRepository implements SessionService {

    private final ConcurrentHashMap<String, Session> sessionConcurrentHashMap = new ConcurrentHashMap();

    @Override
    public void addSession(User user) {
        Session session = new Session(UUID.randomUUID().toString(), user);
        sessionConcurrentHashMap.put(session.getSession(), session);
    }

    @Override
    public Optional<Session> getSession(String userName) {
        Predicate<Session> predicateSession = c->c.getUser().getUserName().equals(userName);
        return sessionConcurrentHashMap.values().stream().filter(predicateSession).findFirst();
    }

    @Override
    public void deleteSession(String session) {
        sessionConcurrentHashMap.remove(session);
    }

    @Override
    public Boolean isValid(User user) {
        Boolean result = true;
        Optional<Session> session = this.getSession(user.getUserName());
        if (session.isPresent()) {
            if (!isSessionExpired(session.get())) {
                this.deleteSession(session.get().getSession());
                result = false;
            }
        } else {
            this.addSession(user);
        }
        return result;
    }

    private Boolean isSessionExpired(Session session) {
        long timeSession = System.currentTimeMillis() - session.getDate() - session.getExpirationTime();
        return (timeSession < 0);
    }
}
