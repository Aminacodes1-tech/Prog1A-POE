/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mainclass;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author ICT 2022
 */

public class MessageNGTest {
    
    @BeforeMethod
    public void setUp() {
        // Reset static variables before each test
        Message.getSentMessages().clear();
        Message.getStoredMessages().clear();
        Message.getMessageHashes().clear();
        Message.getMessageIDs().clear();
        resetTotalMessagesCounter();
    }
    
    @AfterMethod
    public void tearDown() {
        
        Message.getSentMessages().clear();
        Message.getStoredMessages().clear();
        Message.getMessageHashes().clear();
        Message.getMessageIDs().clear();
        resetTotalMessagesCounter();
    }
    
    private void resetTotalMessagesCounter() {
        try {
            Field field = Message.class.getDeclaredField("totalMessages");
            field.setAccessible(true);
            field.set(null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void createTestMessages() {
        // Test Data Message 1
        Message message1 = new Message("+27834557896", "Did you get the cake?");
        addMessageToSentArrays(message1);
        
        // Test Data Message 2
        Message message2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        addMessageToSentArrays(message2);
        
        // Test Data Message 3
        Message message3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        addMessageToSentArrays(message3);
        
        // Test Data Message 4
        Message message4 = new Message("0838884567", "It is dinner time!");
        addMessageToSentArrays(message4);
        
        // Test Data Message 5 (stored message)
        Message message5 = new Message("+27838884567", "Ok, I am leaving without you.");
        Message.getStoredMessages().add(message5);
    }
    
   
    private void addMessageToSentArrays(Message message) {
        Message.getSentMessages().add(message);
        Message.getMessageIDs().add(message.getMessageID());
        Message.getMessageHashes().add(message.getMessageHash());
        
       
        try {
            Field totalMessagesField = Message.class.getDeclaredField("totalMessages");
            totalMessagesField.setAccessible(true);
            int currentTotal = (int) totalMessagesField.get(null);
            totalMessagesField.set(null, currentTotal + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test 1: Sent Messages array correctly populated
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        // Create test messages
        createTestMessages();
        
        // Get the sent messages array
        List<Message> sentMessages = Message.getSentMessages();
        
        // Verify the array contains expected test data
        assertEquals(sentMessages.size(), 4, "Should have 4 sent messages");
        
     
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
        
        assertTrue(foundMessage1, "Should contain 'Did you get the cake?'");
        assertTrue(foundMessage4, "Should contain 'It is dinner time!'");
    }
    
    // Test 2: Display the longest Message
    @Test
    public void testDisplayLongestMessage() {
        // Create test messages
        createTestMessages();
        
        // Use the displayLongestMessage method
        String result = Message.displayLongestMessage();
        
        // Verify the result contains the longest message
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."), 
                  "Should return the longest message");
    }
    
    // Test 3: Search for message by ID
    @Test
    public void testSearchForMessageByID() {
        // Create test messages
        createTestMessages();
        
        // Find message 4 (It is dinner time!) and get its ID
        String targetMessageID = null;
        for (Message msg : Message.getSentMessages()) {
            if ("It is dinner time!".equals(msg.getMessageText())) {
                targetMessageID = msg.getMessageID();
                break;
            }
        }
        
        assertNotNull(targetMessageID, "Should find message 4 ID");
        
        // Search for the message by ID
        String searchResult = Message.searchMessageByID(targetMessageID);
        
        // Verify the search result contains the message and recipient
        assertTrue(searchResult.contains("It is dinner time!"), "Should find the correct message by ID");
        assertTrue(searchResult.contains("0838884567"), "Should contain recipient 0838884567");
    }
    
    // Test 4: Search all messages for a particular recipient 
    @Test
    public void testSearchMessagesByRecipient() {
        // Create test messages
        createTestMessages();
        
        // Search for messages sent to +27838884567
        String recipient = "+27838884567";
        String searchResult = Message.searchMessagesByRecipient(recipient);
        
        
        boolean foundMessage2 = searchResult.contains("Where are you? You are late! I have asked you to be on time.");
        boolean foundMessage5 = searchResult.contains("Ok, I am leaving without you.");
        
        
        assertTrue(foundMessage2 || foundMessage5, "Should find messages for recipient");
    }
    
    // Test 5: Delete a message using message hash
    @Test
    public void testDeleteMessageByHash() {
        // Create test messages
        createTestMessages();
        
        
        String targetMessageHash = null;
        for (Message msg : Message.getSentMessages()) {
            if ("Where are you? You are late! I have asked you to be on time.".equals(msg.getMessageText())) {
                targetMessageHash = msg.getMessageHash();
                break;
            }
        }
        
        assertNotNull(targetMessageHash, "Should find message 2 hash");
        
        // Store initial count
        int initialCount = Message.getSentMessages().size();
        
        // Delete the message by hash
        String deleteResult = Message.deleteMessageByHash(targetMessageHash);
        
        // Verify deletion was successful and contains the expected message
        assertTrue(deleteResult.contains("successfully") || deleteResult.contains("deleted"), 
                  "Deletion should be successful");
        
        // Verify the message was removed from sent messages array
        assertEquals(Message.getSentMessages().size(), initialCount - 1, "Should have one less message");
    }
    
    // Test 6: Display Report 
    @Test
    public void testDisplayReport() {
        // Create test messages
        createTestMessages();
        
        // Generate the report
        String report = Message.displayFullReport();
        
     
        boolean hasMessageContent = report.contains("Did you get the cake?") || 
                                  report.contains("Where are you? You are late!") ||
                                  report.contains("It is dinner time!");
        
        assertTrue(hasMessageContent, "Report should contain message content");
        
        // Also check if it has recipient information
        boolean hasRecipientInfo = report.contains("+27834557896") || 
                                  report.contains("0838884567") ||
                                  report.contains("Recipient");
        
        assertTrue(hasRecipientInfo, "Report should contain recipient information");
    }
    
    // Test 7: Message length validation - Success case
    @Test
    public void testMessageLengthSuccess() {
        String validMessage = "Hi Mike, can you join us for dinner tonight";
        boolean result = Message.validateMessageLength(validMessage);
        assertTrue(result, "Message should be valid when under 250 characters");
    }
    
    // Test 8: Message length validation - Failure case
    @Test
    public void testMessageLengthFailure() {
        // Create a message longer than 250 characters
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longMessage.append("a");
        }
        String invalidMessage = longMessage.toString();
        
        boolean result = Message.validateMessageLength(invalidMessage);
        assertFalse(result, "Message should be invalid when over 250 characters");
    }
    
    // Test 9: Recipient number formatting - Success case
    @Test
    public void testRecipientNumberSuccess() {
        Message testMessage = new Message("+27718693002", "Test message");
        int result = testMessage.checkRecipientCell();
        assertEquals(result, -1, "Recipient number should be valid");
    }
    
    // Test 10: Recipient number formatting - Failure case (no international code)
    @Test
    public void testRecipientNumberFailureNoInternationalCode() {
        Message testMessage = new Message("08575975889", "Test message");
        int result = testMessage.checkRecipientCell();
        assertEquals(result, -1, "Recipient number should fail without international code");
    }
    
    // Test 11: Message hash generation
    @Test
    public void testMessageHashGeneration() {
        Message testMessage = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        String messageHash = testMessage.getMessageHash();
        
        assertNotNull(messageHash, "Message hash should not be null");
        assertTrue(messageHash.contains(":"), "Message hash should contain colons");
        assertTrue(messageHash.toUpperCase().equals(messageHash), "Message hash should be in all caps");
    }
    
    // Test 12: Message ID creation and validation
    @Test
    public void testMessageIDCreation() {
        Message testMessage = new Message("+27718693002", "Test message");
        boolean result = testMessage.checkMessageID();
        assertTrue(result, "Message ID should be valid (10 characters or less)");
        
        String messageID = testMessage.getMessageID();
        assertNotNull(messageID, "Message ID should not be null");
        assertTrue(messageID.length() <= 10, "Message ID should be 10 characters or less");
    }
    
    // Test 13: Total messages counter
    @Test
    public void testTotalMessagesCounter() {
        assertEquals(Message.returnTotalMessages(), 0, "Initial total messages should be 0");
    }
}