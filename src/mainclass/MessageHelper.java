/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ICT 2022
 */
package mainclass;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MessageHelper {
    
    // Arrays 
    private List<Message> sentMessages = new ArrayList<>();
    private List<Message> disregardedMessages = new ArrayList<>();
    private List<Message> storedMessages = new ArrayList<>();
    private List<String> messageHashes = new ArrayList<>();
    private List<String> messageIDs = new ArrayList<>();
    
    // Constructor
    public MessageHelper() {
        loadStoredMessagesFromJSON();
    }
    
    // Getters for arrays
    public List<Message> getSentMessages() { return sentMessages; }
    public List<Message> getDisregardedMessages() { return disregardedMessages; }
    public List<Message> getStoredMessages() { return storedMessages; }
    public List<String> getMessageHashes() { return messageHashes; }
    public List<String> getMessageIDs() { return messageIDs; }
    
    
    public String processMessage(Message message, String flag) {
        switch (flag.toLowerCase()) {
            case "sent":
                sentMessages.add(message);
                messageIDs.add(message.getMessageID());
                messageHashes.add(message.getMessageHash());
                return "Message sent successfully";
                
            case "stored":
                storedMessages.add(message);
                storeMessageToJSON(message);
                return "Message stored successfully";
                
            case "disregarded":
                disregardedMessages.add(message);
                return "Message disregarded";
                
            default:
                return "Invalid flag specified";
        }
    }
    
    /**
     * 2a. Display the sender and recipient of all sent messages
     */
    public String displayAllSentMessages() {
        if (sentMessages.isEmpty()) {
            return "No sent messages available.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("All Sent Messages (Recipient & Message):\n\n");
        for (Message msg : sentMessages) {
            sb.append("To: ").append(msg.getRecipient())
              .append(" | Message: ").append(msg.getMessageText())
              .append("\n");
        }
        return sb.toString();
    }
    
    /**
     * 2b. Display the longest sent message
     */
    public String displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            return "No sent messages available.";
        }
        
        Message longest = sentMessages.get(0);
        for (Message msg : sentMessages) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }
        
        return "Longest Sent Message:\n" +
               "To: " + longest.getRecipient() + "\n" +
               "Message: " + longest.getMessageText() + "\n" +
               "Length: " + longest.getMessageText().length() + " characters";
    }
    
    /**
     * 2c. Search for a message ID and display the corresponding recipient and message
     */
    public String searchMessageByID(String searchID) {
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(searchID)) {
                return "Message Found:\n" +
                       "ID: " + msg.getMessageID() + "\n" +
                       "To: " + msg.getRecipient() + "\n" +
                       "Message: " + msg.getMessageText() + "\n" +
                       "Hash: " + msg.getMessageHash();
            }
        }
        
        // Search in stored messages
        for (Message msg : storedMessages) {
            if (msg.getMessageID().equals(searchID)) {
                return "Message Found (Stored):\n" +
                       "ID: " + msg.getMessageID() + "\n" +
                       "To: " + msg.getRecipient() + "\n" +
                       "Message: " + msg.getMessageText() + "\n" +
                       "Hash: " + msg.getMessageHash();
            }
        }
        
        return "No message found with ID: " + searchID;
    }
    
    /**
     * 2d. Search for all the messages sent to a particular recipient
     */
    public String searchMessagesByRecipient(String recipient) {
        List<Message> recipientMessages = new ArrayList<>();
        
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(recipient)) {
                recipientMessages.add(msg);
            }
        }
        
        // Search in stored messages
        for (Message msg : storedMessages) {
            if (msg.getRecipient().equals(recipient)) {
                recipientMessages.add(msg);
            }
        }
        
        if (recipientMessages.isEmpty()) {
            return "No messages found for recipient: " + recipient;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Messages for recipient ").append(recipient).append(":\n\n");
        for (Message msg : recipientMessages) {
            sb.append("Message: ").append(msg.getMessageText())
              .append(" | Status: ").append(sentMessages.contains(msg) ? "Sent" : "Stored")
              .append("\n");
        }
        return sb.toString();
    }
    
    /**
     * 2e. Delete a message using the message hash
     */
    public String deleteMessageByHash(String hash) {
        // Search and delete from sent messages
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(hash)) {
                Message removed = sentMessages.remove(i);
                messageIDs.remove(removed.getMessageID());
                messageHashes.remove(hash);
                return "Message successfully deleted:\n" +
                       "Hash: " + hash + "\n" +
                       "Recipient: " + removed.getRecipient() + "\n" +
                       "Message: " + removed.getMessageText();
            }
        }
        
        // Search and delete from stored messages
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(hash)) {
                Message removed = storedMessages.remove(i);
                return "Stored message successfully deleted:\n" +
                       "Hash: " + hash + "\n" +
                       "Recipient: " + removed.getRecipient() + "\n" +
                       "Message: " + removed.getMessageText();
            }
        }
        
        return "No message found with hash: " + hash;
    }
    
    /**
     * 2f. Display a report that lists the full details of all the sent messages
     */
    public String displayFullReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages available for report.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("FULL MESSAGING REPORT\n");
        sb.append("=====================\n\n");
        
        sb.append("Total Messages Sent: ").append(sentMessages.size()).append("\n\n");
        
        sb.append("DETAILED MESSAGE LIST:\n");
        sb.append("----------------------\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            sb.append("Message ").append(i + 1).append(":\n");
            sb.append("  Message Hash: ").append(msg.getMessageHash()).append("\n");
            sb.append("  Message ID: ").append(msg.getMessageID()).append("\n");
            sb.append("  Recipient: ").append(msg.getRecipient()).append("\n");
            sb.append("  Message: ").append(msg.getMessageText()).append("\n");
            sb.append("  Length: ").append(msg.getMessageText().length()).append(" characters\n\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Store message to JSON file
     */
    private void storeMessageToJSON(Message message) {
        try {
            // Read existing stored messages
            JSONArray messagesArray = readStoredMessages();
            
            // Create JSON object for current message
            JSONObject messageJson = new JSONObject();
            messageJson.put("messageID", message.getMessageID());
            messageJson.put("recipient", message.getRecipient());
            messageJson.put("messageText", message.getMessageText());
            messageJson.put("messageHash", message.getMessageHash());
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
    
    /**
     * Load stored messages from JSON file into storedMessages array
     */
    private void loadStoredMessagesFromJSON() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("stored_messages.json"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            
            JSONArray messagesArray = new JSONArray(content.toString());
            storedMessages.clear();
            
            for (int i = 0; i < messagesArray.length(); i++) {
                JSONObject msgJson = messagesArray.getJSONObject(i);
                String recipient = msgJson.getString("recipient");
                String messageText = msgJson.getString("messageText");
                Message message = new Message(recipient, messageText);
                storedMessages.add(message);
            }
        } catch (Exception e) {
            // Return empty array if file doesn't exist or is empty
            System.err.println("Error loading stored messages: " + e.getMessage());
        }
    }
    
   
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
    
  
    public void populateWithTestData() {
        // Clear existing data
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        
        // Test Data Message 1 - Sent
        Message message1 = new Message("+27834557896", "Did you get the cake?");
        processMessage(message1, "sent");
        
        // Test Data Message 2 - Stored
        Message message2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        processMessage(message2, "stored");
        
        // Test Data Message 3 - No flag specified 
        Message message3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        processMessage(message3, "sent");
        
        // Test Data Message 4 - Sent
        Message message4 = new Message("0838884567", "It is dinner time!");
        processMessage(message4, "sent");
        
        // Test Data Message 5 - Stored
        Message message5 = new Message("+27838884567", "Ok, I am leaving without you.");
        processMessage(message5, "stored");
    }
    
   
    public String getStatistics() {
        return "Message Statistics:\n" +
               "Sent Messages: " + sentMessages.size() + "\n" +
               "Stored Messages: " + storedMessages.size() + "\n" +
               "Disregarded Messages: " + disregardedMessages.size() + "\n" +
               "Total Message Hashes: " + messageHashes.size() + "\n" +
               "Total Message IDs: " + messageIDs.size();
    }
}
