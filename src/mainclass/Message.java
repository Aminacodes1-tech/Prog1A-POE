/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ICT 2022
 */
package mainclass;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;//JSON
import org.json.JSONObject;
import java.io.FileWriter;//FILE HANDLING
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class Message {
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessages = 0;
    private static int messageCounter = 0;
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    
    // Constructor
    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public static int getTotalMessages() { return totalMessages; }
    public static List<Message> getSentMessages() { return sentMessages; }
    
    // Method 1: Check if Message ID is valid (not more than 10 characters)
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
    
    // Method 2: Check recipient cell number
    public int checkRecipientCell() {
        if (recipient == null || recipient.length() > 10) {
            return -1; // Invalid length
        }
        if (!recipient.startsWith("+")) {
            return -2; // Doesn't start with +
        }
        return 0; // Valid
    }
    
    // Method 3: Create message hash
    public String createMessageHash() {
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String[] words = messageText.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        return (firstTwoID + ":" + messageCounter + ":" + firstWord + lastWord).toUpperCase();
    }
    
    // Method 4: Send message with options
    public String SentMessage() {
        String[] options = {"Send Message", "Store Message", "Disregard Message"};
        int choice = JOptionPane.showOptionDialog(null,
            "Choose what to do with the message:",
            "Message Options",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]);
        
        switch (choice) {
            case 0: // Send
                sentMessages.add(this);
                totalMessages++;
                displayMessageDetails();
                return "Message sent successfully";
                
            case 1: // Store
                storedMessages.add(this);
                storeMessage();
                return "Message stored successfully";
                
            case 2: // Disregard
                return "Message disregarded";
                
            default:
                return "No action taken";
        }
    }
    
    // Method 5: Print all sent messages
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Recently Sent Messages:\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            sb.append(i + 1).append(". ")
              .append("ID: ").append(msg.getMessageID())
              .append(" | To: ").append(msg.getRecipient())
              .append(" | Message: ").append(msg.getMessageText())
              .append("\n");
        }
        return sb.toString();
    }
    
    // Method 6: Return total messages
    public static int returnTotalMessages() {
        return totalMessages;
    }
    
    // Method 7: Store messages in JSON
    public void storeMessage() {
        try {
            // Read existing stored messages
            JSONArray messagesArray = readStoredMessages();
            
            // Create JSON object for current message
            JSONObject messageJson = new JSONObject();
            messageJson.put("messageID", this.messageID);
            messageJson.put("recipient", this.recipient);
            messageJson.put("messageText", this.messageText);
            messageJson.put("messageHash", this.messageHash);
            messageJson.put("timestamp", System.currentTimeMillis());
            
            // Add to array
            messagesArray.put(messageJson);
            
            // Write back to file
            try (FileWriter file = new FileWriter("stored_messages.json")) {
                file.write(messagesArray.toString(4)); // Indent for readability
                file.flush();
            }
            
        } catch (IOException e) {
            System.err.println("Error storing message: " + e.getMessage());
        }
    }
    
    // Helper method to read stored messages from JSON file
    private JSONArray readStoredMessages() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("stored_messages.json"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            return new JSONArray(content.toString());
        } catch (Exception e) {
            // Return empty array if file doesn't exist or is empty
            return new JSONArray();
        }
    }
    
    
    private void displayMessageDetails() {
        String details = "Message Details:\n\n" +
                        "Message ID: " + messageID + "\n" +
                        "Message Hash: " + messageHash + "\n" +
                        "Recipient: " + recipient + "\n" +
                        "Message: " + messageText + "\n\n" +
                        "Total Messages Sent: " + totalMessages;
        
        JOptionPane.showMessageDialog(null, details, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Generating random 10-digit message ID
    private String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }
    
    // Validate message length (not more than 250 characters)
    public static boolean validateMessageLength(String message) {
        return message != null && message.length() <= 250;
    }
}