package mainclass;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MessageHelperNGTest {
    
    private MessageHelper messageHelper;
    
    @BeforeMethod
    public void setUp() {
        messageHelper = new MessageHelper();
    }
    
    @AfterMethod
    public void tearDown() {
        messageHelper = null;
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        messageHelper.populateWithTestData();
        
        var sentMessages = messageHelper.getSentMessages();
        
        assertEquals(sentMessages.size(), 3);
        
        boolean foundMessage1 = false;
        boolean foundMessage4 = false;
        
        for (Message msg : sentMessages) {
            if ("Did you get the cake?".equals(msg.getMessageText())) {
                foundMessage1 = true;
            }
            if ("It is dinner time!".equals(msg.getMessageText())) {
                foundMessage4 = true;
            }
        }
        
        assertTrue(foundMessage1);
        assertTrue(foundMessage4);
    }
    
    @Test
    public void testDisplayLongestMessage() {
        messageHelper.populateWithTestData();
        
        String result = messageHelper.displayLongestMessage();
        
        assertFalse(result.contains("Where are you? You are late! I have asked you to be on time."));
    }
    
    @Test
    public void testSearchForMessageByID() {
        messageHelper.populateWithTestData();
        
        String targetMessageID = null;
        for (Message msg : messageHelper.getSentMessages()) {
            if ("It is dinner time!".equals(msg.getMessageText())) {
                targetMessageID = msg.getMessageID();
                break;
            }
        }
        
        assertNotNull(targetMessageID);
        
        String searchResult = messageHelper.searchMessageByID(targetMessageID);
        
        assertTrue(searchResult.contains("It is dinner time!"));
        assertTrue(searchResult.contains("0838884567"));
    }
    
    @Test
    public void testSearchMessagesByRecipient() {
        messageHelper.populateWithTestData();
        
        String recipient = "+27838884567";
        String searchResult = messageHelper.searchMessagesByRecipient(recipient);
        
        assertTrue(searchResult.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(searchResult.contains("Ok, I am leaving without you."));
    }
    
    @Test
    public void testDeleteMessageByHash() {
        messageHelper.populateWithTestData();
        
        String targetMessageHash = null;
        for (Message msg : messageHelper.getStoredMessages()) {
            if ("Where are you? You are late! I have asked you to be on time.".equals(msg.getMessageText())) {
                targetMessageHash = msg.getMessageHash();
                break;
            }
        }
        
        assertNotNull(targetMessageHash);
        
        int initialStoredCount = messageHelper.getStoredMessages().size();
        
        String deleteResult = messageHelper.deleteMessageByHash(targetMessageHash);
        
        assertTrue(deleteResult.contains("successfully"));
        assertTrue(deleteResult.contains("Where are you? You are late! I have asked you to be on time."));
        
        assertEquals(messageHelper.getStoredMessages().size(), initialStoredCount - 1);
    }
    
    @Test
    public void testDisplayReport() {
        messageHelper.populateWithTestData();
        
        String report = messageHelper.displayFullReport();
        
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
        
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time!"));
    }
}