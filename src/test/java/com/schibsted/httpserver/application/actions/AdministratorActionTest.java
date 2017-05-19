package com.schibsted.httpserver.application.actions;

import com.schibsted.httpserver.ContextServerTest;
import com.schibsted.httpserver.domain.role.Role;
import com.schibsted.httpserver.domain.user.User;
import com.schibsted.httpserver.infraestructure.user.AdministratorServiceImpl;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import java.util.*;

public class AdministratorActionTest extends ContextServerTest {

    @BeforeClass
    public static void beforeClass() {
        userRepository = new UserInMemoryRepository();
    }

    @AfterClass
    public static void afterClass() {
        userRepository.getUserConcurrentHashMap().clear();
    }

    @Test
    public void testAddUser() {
        AdministratorServiceImpl administratorAction = new AdministratorServiceImpl(userRepository);
        Map<String,String> post = new HashMap<String,String>() {{
                put(AdministratorServiceImpl.FIELD_USER, "user4");
                put(AdministratorServiceImpl.FIELD_PWD, "1234");
                put(AdministratorServiceImpl.FIELD_ROLE, Role.PAGE_1.toString());
        }};
        Optional<User> user = administratorAction.addUser(post);
        assertNotNull("User doesn´t exist", user);
        Collection<User> users = administratorAction.getUsers();

        assertEquals("Waiting 4 items", 4, users.size());
        assertEquals("The ID is autoimcrement", 4, user.get().getId());
    }

    @Test
    public void testUpdateUser() {
        AdministratorServiceImpl administratorAction = new AdministratorServiceImpl(userRepository);
        Map<String,String> post = new HashMap<String,String>() {{
                put(AdministratorServiceImpl.FIELD_PWD, "123456");
                put(AdministratorServiceImpl.FIELD_ROLE, Role.ADMIN.toString());
        }};
        Optional<User> user = administratorAction.updateUser(3, post);

        assertNotNull("User not update", user);
        if (user.isPresent()) {
            assertEquals("Password should be 123456", "123456", user.get().getPassword());
            List<Role> roleList = new ArrayList<>();
            roleList.add(Role.ADMIN);
            assertEquals("The role ADMIN is asigned", roleList, user.get().getRoleList());
        }
    }
}