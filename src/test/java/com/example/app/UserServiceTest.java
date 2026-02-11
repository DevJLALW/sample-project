package com.example.app;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class demonstrating legacy test patterns
 */
public class UserServiceTest {
    
    private UserService userService;
    
    @Before
    public void setUp() {
        userService = new UserService();
    }
    
    @Test
    public void testCreateUser() {
        User user = userService.createUser("Jane Doe", "jane@example.com");
        
        assertNotNull(user);
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertTrue(user.getId() > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithBlankName() {
        userService.createUser("", "test@example.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithBlankEmail() {
        userService.createUser("John", "");
    }
    
    @Test
    public void testSerializeAndDeserializeUser() {
        User originalUser = userService.createUser("Test User", "test@example.com");
        String json = userService.serializeUser(originalUser);
        
        assertNotNull(json);
        assertTrue(json.contains("Test User"));
        assertTrue(json.contains("test@example.com"));
        
        User deserializedUser = userService.deserializeUser(json);
        assertEquals(originalUser.getName(), deserializedUser.getName());
        assertEquals(originalUser.getEmail(), deserializedUser.getEmail());
    }
    
    @Test
    public void testUpdateUserEmail() {
        User user = userService.createUser("Update Test", "old@example.com");
        userService.updateUser(user, "new@example.com");
        
        assertEquals("new@example.com", user.getEmail());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateUserWithInvalidEmail() {
        User user = userService.createUser("Test", "test@example.com");
        userService.updateUser(user, "invalid-email");
    }
}
