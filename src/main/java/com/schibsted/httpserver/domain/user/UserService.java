package com.schibsted.httpserver.domain.user;

import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.domain.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Integer id);
    Optional<User> getUserByName(String userName);
    Collection<User> getUsers();
    Boolean isUserPermissions(String userName, Role role);
    Optional<User> checkUserLogin(String userName, String password);

}
