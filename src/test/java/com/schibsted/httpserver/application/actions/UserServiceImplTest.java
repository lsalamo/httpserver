package com.schibsted.httpserver.application.actions;

import com.schibsted.httpserver.ContextServerTest;
import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.domain.user.User;
import com.schibsted.httpserver.infraestructure.user.UserServiceImpl;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class UserServiceImplTest extends ContextServerTest {

    private UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository);

    @BeforeClass
    public static void beforeClass() {
        userRepository = new UserInMemoryRepository();
    }

    @AfterClass
    public static void afterClass() {
        userRepository.getUserConcurrentHashMap().clear();
    }

    @Test
    public void testGetUserById() {
        Optional<User> user = userServiceImpl.getUserById(1);
        assertTrue("Get user by ID", user.isPresent());
    }

    @Test
    public void testGetUserByName() {
        Optional<User> user = userServiceImpl.getUserByName("user1");
        assertTrue("Get user by NAME", user.isPresent());
    }

    @Test
    public void testGetUsers() {
        assertTrue("Get users", userServiceImpl.getUsers().size() == 3);
    }

    @Test
    public void testIsUserPermissions() {
        assertTrue("Is user Permissions", userServiceImpl.isUserPermissions("user2", Role.ADMIN));
    }

    @Test
    public void testCheckUserLogin() {
        assertTrue("Check user login", userServiceImpl.checkUserLogin("user1", "1234").isPresent());
    }

}