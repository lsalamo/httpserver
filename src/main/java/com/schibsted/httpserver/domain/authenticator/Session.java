package com.schibsted.httpserver.domain.authenticator;

import com.schibsted.httpserver.domain.user.User;

import java.time.Duration;

public class Session {
    private final long date = System.currentTimeMillis();
    private final long expirationTime = Duration.ofMinutes(5).toMillis();

    private final String session;
    private final User user;

    public Session(String session, User user) {
        this.session = session;
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public User getUser() {
        return user;
    }

    public long getDate() {
        return date;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
