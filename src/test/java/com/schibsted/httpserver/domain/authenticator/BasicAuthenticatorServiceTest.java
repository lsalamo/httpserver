package com.schibsted.httpserver.domain.authenticator;

import com.schibsted.httpserver.ContextServerTest;
import com.schibsted.httpserver.infraestructure.session.SessionServiceInMemoryRepository;
import com.schibsted.httpserver.infraestructure.user.UserInMemoryRepository;
import org.junit.*;

import static org.junit.Assert.assertTrue;

public class BasicAuthenticatorServiceTest extends ContextServerTest {

    @BeforeClass
    public static void beforeClass() {
        userRepository = new UserInMemoryRepository();
    }

    @AfterClass
    public static void afterClass() {
        userRepository.getUserConcurrentHashMap().clear();
    }

    @Test
    public void testCheckCredentials() throws Exception {
        SessionService sessionService = new SessionServiceInMemoryRepository();
        BasicAuthenticatorService basicAuthenticatorService = new BasicAuthenticatorService(userRepository, sessionService);
        Boolean isValidUser = basicAuthenticatorService.checkCredentials("user2", "1234");

        assertTrue("Valid user", isValidUser);
    }
}