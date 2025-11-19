/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mainclass;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.stream.Collectors;

/**
 *
 * @author ICT 2022
 */
public class Message {
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessages = 0;
    private static int messageCounter = 0;
    
    // Required arrays
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();
    
    // Constructor
    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        messageCounter++;
    }
    
    // Getters
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public static int getTotalMessages() { return totalMessages; }
    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIDs() { return messageIDs; }
    
    //  Check if Message ID is valid 
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
    
    //  Check recipient cell number
    public int checkRecipientCell() {
        if (recipient == null || recipient.length() > 10) {
            return -1; // Invalid length
        }
        if (!recipient.startsWith("+")) {
            return -2; // Doesn't start with +
        }
        return 0; // Valid
    }
    
    //  Create message hash
    public String createMessageHash() {
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String[] words = messageText.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        String hash = (firstTwoID + ":" + messageCounter + ":" + firstWord + lastWord).toUpperCase();
        messageHashes.add(hash);
        return hash;
    }
    
    //  Send message with options
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
                messageIDs.add(this.messageID);
                totalMessages++;
                displayMessageDetails();
                return "Message sent successfully";
                
            case 1: // Store
                storedMessages.add(this);
                storeMessage();
                return "Message stored successfully";
                
            case 2: // Disregard
                disregardedMessages.add(this);
                return "Message disregarded";
                
            default:
                return "No action taken";
        }
    }
    
    //  Print all sent messages
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
    
    //  Return total messages
    public static int returnTotalMessages() {
        return totalMessages;
    }
    
    //  Store messages in JSON
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
    
    // Load stored messages from JSON file into storedMessages array
    public static void loadStoredMessagesFromJSON() {
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
            System.err.println("Error loading stored messages: " + e.getMessage());
        }
    }
    
   
    
    // a. Display sender and recipient of all sent messages
    public static String displayAllSentMessages() {
        if (sentMessages.isEmpty()) {
            return "No sent messages available.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("All Sent Messages (Sender & Recipient):\n\n");
        for (Message msg : sentMessages) {
            sb.append("To: ").append(msg.getRecipient())
              .append(" | Message: ").append(msg.getMessageText())
              .append("\n");
        }
        return sb.toString();
    }
    
    // b. Display the longest sent message
    public static String displayLongestMessage() {
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
    
    // c. Search for message by ID and display details
    public static String searchMessageByID(String searchID) {
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(searchID)) {
                return "Message Found:\n" +
                       "ID: " + msg.getMessageID() + "\n" +
                       "To: " + msg.getRecipient() + "\n" +
                       "Message: " + msg.getMessageText() + "\n" +
                       "Hash: " + msg.getMessageHash();
            }
        }
        return "No message found with ID: " + searchID;
    }
    
    // d. Search for all messages sent to a particular recipient
    public static String searchMessagesByRecipient(String recipient) {
        List<Message> recipientMessages = sentMessages.stream()
            .filter(msg -> msg.getRecipient().equals(recipient))
            .collect(Collectors.toList());
        
        if (recipientMessages.isEmpty()) {
            return "No messages found for recipient: " + recipient;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Messages sent to ").append(recipient).append(":\n\n");
        for (Message msg : recipientMessages) {
            sb.append("ID: ").append(msg.getMessageID())
              .append(" | Message: ").append(msg.getMessageText())
              .append("\n");
        }
        return sb.toString();
    }
    
    // e. Delete a message using message hash
    public static String deleteMessageByHash(String hash) {
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(hash)) {
                Message removed = sentMessages.remove(i);
                messageIDs.remove(removed.getMessageID());
                messageHashes.remove(hash);
                totalMessages--;
                return "Message deleted successfully:\n" +
                       "Hash: " + hash + "\n" +
                       "Recipient: " + removed.getRecipient() + "\n" +
                       "Message: " + removed.getMessageText();
            }
        }
        return "No message found with hash: " + hash;
    }
    
    // f. Display full report of all sent messages
    public static String displayFullReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages available for report.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("FULL MESSAGING REPORT\n");
        sb.append("=====================\n\n");
        
        sb.append("Total Messages Sent: ").append(totalMessages).append("\n\n");
        
        sb.append("DETAILED MESSAGE LIST:\n");
        sb.append("----------------------\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            sb.append("Message ").append(i + 1).append(":\n");
            sb.append("  ID: ").append(msg.getMessageID()).append("\n");
            sb.append("  Recipient: ").append(msg.getRecipient()).append("\n");
            sb.append("  Message: ").append(msg.getMessageText()).append("\n");
            sb.append("  Hash: ").append(msg.getMessageHash()).append("\n");
            sb.append("  Length: ").append(msg.getMessageText().length()).append(" characters\n\n");
        }
        
        return sb.toString();
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