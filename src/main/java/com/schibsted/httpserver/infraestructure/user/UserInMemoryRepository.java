package com.schibsted.httpserver.infraestructure.user;

import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.domain.user.Administrator;
import com.schibsted.httpserver.domain.user.User;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserInMemoryRepository {

    public static final String SEPARATOR_ROLE = ",";

    private final AtomicInteger countUserRepository = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer, User> userConcurrentHashMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, User> getUserConcurrentHashMap() {
        return userConcurrentHashMap;
    }

    public AtomicInteger getCountUserRepository() {
        return countUserRepository;
    }

    public UserInMemoryRepository() {
        InitializeUser();
    }

    private void InitializeUser() {
        List<Role> roleList = Pattern.compile(SEPARATOR_ROLE)
                .splitAsStream(Role.PAGE_1.toString())
                .map(this::getRole)
                .collect(Collectors.toList());
        User user1 = new User(countUserRepository.incrementAndGet(), "user1", "1234", roleList);
        userConcurrentHashMap.put(countUserRepository.get(), user1);
        roleList = Pattern.compile(SEPARATOR_ROLE)
                .splitAsStream(Role.ADMIN.toString())
                .map(this::getRole)
                .collect(Collectors.toList());
        User user2 = new Administrator(countUserRepository.incrementAndGet(), "user2", "1234", roleList);
        userConcurrentHashMap.put(countUserRepository.get(), user2);
        roleList = Pattern.compile(SEPARATOR_ROLE)
                .splitAsStream(Role.PAGE_1.toString() + SEPARATOR_ROLE + Role.PAGE_2.toString())
                .map(this::getRole)
                .collect(Collectors.toList());
        User user3 = new User(countUserRepository.incrementAndGet(), "user3", "1234", roleList);
        userConcurrentHashMap.put(countUserRepository.get(), user3);
    }

    private Role getRole(String role) {
        Role result = Role.NONE;
        switch (role.toUpperCase()) {
            case "ADMIN":
                result = Role.ADMIN;
                break;
            case "PAGE_1":
                result = Role.PAGE_1;
                break;
            case "PAGE_2":
                result = Role.PAGE_2;
                break;
            case "PAGE_3":
                result = Role.PAGE_3;
                break;
        }
        return result;
    }

}
