package com.schibsted.httpserver.infraestructure.user;

import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.domain.user.UserService;
import com.schibsted.httpserver.domain.user.User;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UserServiceImpl implements UserService {

    private UserInMemoryRepository userRepository;

    public UserInMemoryRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserInMemoryRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserServiceImpl(UserInMemoryRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        Predicate<User> predicateUser = c -> c.getId() == id;
        return userRepository.getUserConcurrentHashMap().values().stream().filter(predicateUser).findFirst();
    }

    @Override
    public Optional<User> getUserByName(String userName) {
        Predicate<User> predicateUser = c -> c.getUserName().equals(userName);
        return userRepository.getUserConcurrentHashMap().values().stream().filter(predicateUser).findFirst();
    }

    @Override
    public Collection<User> getUsers() {
        return userRepository.getUserConcurrentHashMap().values();
    }

    @Override
    public Boolean isUserPermissions(String userName, Role role) {
        Predicate<Role> predicateRole = c->c.equals(role) || c.equals(Role.ADMIN);
        Optional<User> user = getUserByName(userName);
        if (user.isPresent()) {
            Stream<Role> roleStream = user.get().getRoleList().stream().filter(predicateRole);
            return (roleStream.count()>0);
        } else return false;
    }

    @Override
    public Optional<User> checkUserLogin(String userName, String password) {
        Predicate<User> predicateUser = c -> c.getUserName().equals(userName) && c.getPassword().equals(password);
        return userRepository.getUserConcurrentHashMap().values().stream().filter(predicateUser).findFirst();
    }

}
