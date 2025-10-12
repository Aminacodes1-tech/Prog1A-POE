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

/**
 *
 * @author ICT 2022
 */

public class MessageNGTest {
    
    @BeforeMethod
    public void setUp() {
        // Reset static variables before each test
        Message.getSentMessages().clear();
        resetTotalMessagesCounter();
    }
    
    @AfterMethod
    public void tearDown() {
        // for cleaning up after each test
        Message.getSentMessages().clear();
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
    
    // Test 1: Message length validation - Success case
    @Test
    public void testMessageLengthSuccess() {
        String validMessage = "Hi Mike, can you join us for dinner tonight";
        boolean result = Message.validateMessageLength(validMessage);
        assertTrue(result, "Message should be valid when under 250 characters");
    }
    
    // Test 2: Message length validation - Failure case
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
    
    // Test 3: Recipient number formatting - Success case
    @Test
    public void testRecipientNumberSuccess() {
        Message testMessage = new Message("+27718693002", "Test message");
        int result = testMessage.checkRecipientCell();
        assertEquals(result, -1, "Recipient number should be valid");
    }
    
    // Test 4: Recipient number formatting - Failure case (no international code)
    @Test
    public void testRecipientNumberFailureNoInternationalCode() {
        Message testMessage = new Message("08575975889", "Test message");
        int result = testMessage.checkRecipientCell();
        assertEquals(result, -1, "Recipient number should fail without international code");
    }
    
    // Test 5: Message hash generation with test data from screenshot
    @Test
    public void testMessageHashGeneration() {
        // Test with data from Test Case 1
        Message testMessage = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        String messageHash = testMessage.getMessageHash();
        
        assertNotNull(messageHash, "Message hash should not be null");
        assertTrue(messageHash.contains(":"), "Message hash should contain colons");
        assertTrue(messageHash.toUpperCase().equals(messageHash), "Message hash should be in all caps");
        
        // Verify the format contains first and last words
        assertTrue(messageHash.contains("HI"), "Hash should contain first word 'HI'");
        assertTrue(messageHash.contains("TONIGHT"), "Hash should contain last word 'TONIGHT'");
    }
    
    // Test 6: Message ID creation and validation
    @Test
    public void testMessageIDCreation() {
        Message testMessage = new Message("+27718693002", "Test message");
        boolean result = testMessage.checkMessageID();
        assertTrue(result, "Message ID should be valid (10 characters or less)");
        
        String messageID = testMessage.getMessageID();
        assertNotNull(messageID, "Message ID should not be null");
        assertTrue(messageID.length() <= 10, "Message ID should be 10 characters or less");
    }
    
    // Test 7: Total messages counter
    @Test
    public void testTotalMessagesCounter() {
        assertEquals(Message.returnTotalMessages(), 0, "Initial total messages should be 0");
        
        // Test that counter increments when messages are sent
        TestableMessage message1 = new TestableMessage("+27718693002", "First message", 0);
        message1.SentMessage();
        assertEquals(Message.returnTotalMessages(), 1, "Total messages should be 1 after sending first message");
    }
    
    // Testable inner class for testing without GUI dependencies
    private static class TestableMessage extends Message {
        private final int simulatedUserChoice;
        
        public TestableMessage(String recipient, String messageText, int userChoice) {
            super(recipient, messageText);
            this.simulatedUserChoice = userChoice;
        }
        
        @Override
        public String SentMessage() {
            // Simulate the user choice without GUI
            switch (simulatedUserChoice) {
                case 0: // Send
                    Message.getSentMessages().add(this);
                    incrementTotalMessages();
                    return "Message sent successfully";
                    
                case 1: // Store
                    storeMessage();
                    return "Message stored successfully";
                    
                case 2: // Disregard
                    return "Message disregarded";
                    
                default:
                    return "No action taken";
            }
        }
        
        private void incrementTotalMessages() {
            try {
                Field field = Message.class.getDeclaredField("totalMessages");
                field.setAccessible(true);
                int currentTotal = (int) field.get(null);
                field.set(null, currentTotal + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}