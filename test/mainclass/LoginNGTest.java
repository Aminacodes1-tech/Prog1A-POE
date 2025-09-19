package mainclass;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class LoginNGTest {
    
    private Login login;
    
    @BeforeMethod
    public void setUp() {
        login = new Login();
        login.setName("Kyle");
        login.setLastname("Smith");
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setPhoneNumber("+27838968976");
    }
  
    // Test assertTrue/False scenarios
    
    @Test
    public void testLoginSuccessful() {
        boolean result = login.LoginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue(result);
    }
    
    @Test
    public void testLoginFailed() {
        boolean result = login.LoginUser("wronguser", "wrongpass");
        assertFalse(result);
    }
    
    @Test
    public void testUsernameCorrectlyFormatted() {
        boolean result = login.checkUsernameComplexity("kyl_1");
        assertTrue(result);
    }
    
    @Test
    public void testUsernameIncorrectlyFormatted() {
        boolean result = login.checkUsernameComplexity("kyle!!!!!!");
        assertFalse(result);
    }
    
    @Test
    public void testPasswordMeetsComplexityRequirements() {
        boolean result = login.CheckPasswordComplexity("Ch&&sec@ke99!");
        assertTrue(result);
    }
    
    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        boolean result = login.CheckPasswordComplexity("password");
        assertFalse(result);
    }
    
    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        boolean result = login.CheckPhoneNumberComplexity("+27838968976");
        assertTrue(result);
    }
    
    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        boolean result = login.CheckPhoneNumberComplexity("08966553");
        assertTrue(result);
    }
    
    
    @Test
    public void testReturnLoginStatusSuccessful() {
        String result = login.ReturnLoginStatus(true);
        assertEquals(result, "Welcome Kyle Smith it is great to see you again");
    }
    
    @Test
    public void testReturnLoginStatusFailed() {
        String result = login.ReturnLoginStatus(false);
        assertEquals(result, "Username or password incorrect please try again");
    }
}