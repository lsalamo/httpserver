package com.schibsted.httpserver.domain.user;

import java.util.Map;
import java.util.Optional;

public interface AdministratorService {

    Optional<User> addUser(Map<String, String> post);
    Optional<User> updateUser(Integer id, Map<String, String> post);

}

