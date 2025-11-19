/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package mainclass;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class MainClass {

    public static void main(String[] args) {
        // Object calling
        Login log = new Login();
        Scanner scan = new Scanner(System.in);
       
        System.out.println("***Registration***");
        
        System.out.print("Hi, Please enter your first name: ");
        String firstName = scan.nextLine();
        log.setName(firstName);
        
        System.out.print("Please enter your last name: ");
        String LastName = scan.nextLine();
        log.setLastname(LastName);
        
        System.out.println();
        
        // Username input with validation and retry
        String UserName;
        boolean validUsername = false;
        do {
            System.out.print("Please enter a username(must contain underscore and max of 5 characters): ");
            UserName = scan.nextLine();
            log.setUsername(UserName);
            if(log.checkUsernameComplexity(UserName)){
                System.out.println("Username is correctly formatted");
                validUsername = true;
            }else{
                System.out.println("Username is not correctly formatted. Please try again.");
            }
        } while (!validUsername);
        
        // Password input with validation and retry
        String Password;
        boolean validPassword = false;
        do {
            System.out.print("Enter a password( must contain a capital letter,min 8 characters,special character,and a number): ");
            Password = scan.nextLine();
            log.setPassword(Password);
             
            if(log.CheckPasswordComplexity(Password)){
                System.out.println("Password is correctly formatted");
                validPassword = true;
            }else{
                System.out.println("Password is not correctly formatted. Please try again.");
            }
        } while (!validPassword);
        
         // Phone number input with validation and retry
        String PhoneNumber;
        boolean validPhoneNumber = false;
        do {
            System.out.print("Please enter your phone number (must start with +27*********): ");
            PhoneNumber = scan.nextLine();
            log.setPhoneNumber(PhoneNumber);
           
            if(log.CheckPhoneNumberComplexity(PhoneNumber)){
                System.out.println("Phone number is correctly formatted");
                validPhoneNumber = true;
            }else{
                System.out.println("Phone number is not correctly formatted. Please try again.");
            }
        } while (!validPhoneNumber);
        
        System.out.println();
     
        System.out.println("****LOGIN****");
        // Login user
        System.out.print("Enter username to login: ");
        String Secondusername = scan.next();
        System.out.print("Enter password to login: ");
        String Secondpassword = scan.next();
        
        // Display login Status
        boolean loginSuccessful = log.LoginUser(Secondusername, Secondpassword);
        String loginMessage = log.ReturnLoginStatus(loginSuccessful);
      
        if(loginSuccessful){
            System.out.println(loginMessage);
            // Load stored messages from JSON file
            Message.loadStoredMessagesFromJSON();
            // Start messaging application after successful login
            startMessagingApplication(scan);
        } else {
            System.out.println("Password or username is incorrect");
        }
        
        scan.close();
    }
    
    private static void startMessagingApplication(Scanner scan) {
        // welcome message
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.", "QuickChat", JOptionPane.INFORMATION_MESSAGE);
        
        boolean running = true;
        
        while (running) {
            // Main menu
            String menu = "QuickChat Menu:\n\n" +
                         "1) Send Messages\n" +
                         "2) Show Recently Sent Messages(New Update Functions)\n" +
                         "3) Quit\n\n" +
                         "Please choose an option:";
            
            String choiceStr = JOptionPane.showInputDialog(null, menu, "QuickChat", JOptionPane.QUESTION_MESSAGE);
            
            if (choiceStr == null) {
                break;
            }
            
            try {
                int choice = Integer.parseInt(choiceStr);
                
                switch (choice) {
                    case 1:
                        sendMessages(scan);
                        break;
                    case 2:
                        showMessageOperationsMenu();
                        break;
                    case 3:
                        running = false;
                        JOptionPane.showMessageDialog(null, 
                            "Thank you for using QuickChat!\nTotal messages sent: " + Message.returnTotalMessages(), 
                            "Goodbye", 
                            JOptionPane.INFORMATION_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option. Please choose 1, 2, or 3.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number (1, 2, or 3).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
   private static void showMessageOperationsMenu() {
    MessageHelper messageHelper = new MessageHelper();
    boolean backToMain = false;
    
    while (!backToMain) {
        String operationsMenu = "Message Operations & New Update Functions:\n\n" +
                              "a) Populate With Test Data\n"+
                              "b) Show Recently Sent Messages\n" +
                              "c) Display sender and recipient of all sent messages\n" +
                              "d) Display the longest sent message\n" +
                              "e) Search for message by ID\n" +
                              "f) Search messages by recipient\n" +
                              "g) Delete message by hash\n" +
                              "h) Display full report\n" +
                              "j) Back to main menu\n\n" +
                              "Please choose an option:";
        
        String choiceStr = JOptionPane.showInputDialog(null, operationsMenu, "Message Operations", JOptionPane.QUESTION_MESSAGE);
        
        if (choiceStr == null) {
            backToMain = true;
            continue;
        }
        
        switch (choiceStr.toLowerCase()) {
            case "a":
                messageHelper.populateWithTestData();
                JOptionPane.showMessageDialog(null, "Test data populated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "b":
                String recentMessages = Message.printMessages();
                JOptionPane.showMessageDialog(null, recentMessages, "Recently Sent Messages", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "c":
                String sentMessages = messageHelper.displayAllSentMessages();
                JOptionPane.showMessageDialog(null, sentMessages, "All Sent Messages", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "d":
                String longestMessage = messageHelper.displayLongestMessage();
                JOptionPane.showMessageDialog(null, longestMessage, "Longest Message", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "e":
                String searchID = JOptionPane.showInputDialog(null, "Enter Message ID to search:", "Search by ID", JOptionPane.QUESTION_MESSAGE);
                if (searchID != null && !searchID.trim().isEmpty()) {
                    String searchResult = messageHelper.searchMessageByID(searchID.trim());
                    JOptionPane.showMessageDialog(null, searchResult, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
                
            case "f":
                String recipient = JOptionPane.showInputDialog(null, "Enter recipient phone number:", "Search by Recipient", JOptionPane.QUESTION_MESSAGE);
                if (recipient != null && !recipient.trim().isEmpty()) {
                    String recipientResult = messageHelper.searchMessagesByRecipient(recipient.trim());
                    JOptionPane.showMessageDialog(null, recipientResult, "Recipient Messages", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
                
            case "g":
                String hash = JOptionPane.showInputDialog(null, "Enter message hash to delete:", "Delete by Hash", JOptionPane.QUESTION_MESSAGE);
                if (hash != null && !hash.trim().isEmpty()) {
                    String deleteResult = messageHelper.deleteMessageByHash(hash.trim());
                    JOptionPane.showMessageDialog(null, deleteResult, "Delete Result", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
                
            case "h":
                String fullReport = messageHelper.displayFullReport();
                JOptionPane.showMessageDialog(null, fullReport, "Full Messaging Report", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "i":
                String stats = messageHelper.getStatistics();
                JOptionPane.showMessageDialog(null, stats, "Message Statistics", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "j":
                backToMain = true;
                break;
                
            default:
                JOptionPane.showMessageDialog(null, "Invalid option. Please choose a valid option.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    private static void sendMessages(Scanner scan) {
        String numMessagesStr = JOptionPane.showInputDialog(null, 
            "How many messages do you wish to send?", 
            "Number of Messages", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (numMessagesStr == null) return;
        
        try {
            int numMessages = Integer.parseInt(numMessagesStr);
            
            for (int i = 0; i < numMessages; i++) {
                JOptionPane.showMessageDialog(null, 
                    "Creating message " + (i + 1) + " of " + numMessages, 
                    "Message Creation", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Get recipient
                String recipient = JOptionPane.showInputDialog(null, 
                    "Enter recipient cell number (must start with + and be max 10 characters):", 
                    "Recipient", 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (recipient == null) continue; // User cancelled
                
                // Get message text
                String messageText = JOptionPane.showInputDialog(null, 
                    "Enter your message (max 250 characters):", 
                    "Message", 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (messageText == null) continue; // User cancelled
                
                // Validate message length
                if (!Message.validateMessageLength(messageText)) {
                    JOptionPane.showMessageDialog(null, 
                        "Please enter a message of less than 250 characters.", 
                        "Message Too Long", 
                        JOptionPane.ERROR_MESSAGE);
                    i--; // Retry this message
                    continue;
                }
                
                // Create message object
                Message message = new Message(recipient, messageText);
                
                // Validate recipient
                int recipientCheck = message.checkRecipientCell();
                if (recipientCheck != 0) {
                    String errorMsg = recipientCheck == -1 ? 
                        "Recipient number must be no more than 10 characters." : 
                        "Recipient number must start with '+'.";
                    JOptionPane.showMessageDialog(null, errorMsg, "Invalid Recipient", JOptionPane.ERROR_MESSAGE);
                    i--; // Retry this message
                    continue;
                }
                
                // Process message (send, store, or disregard)
                String result = message.SentMessage();
                JOptionPane.showMessageDialog(null, result, "Message Status", JOptionPane.INFORMATION_MESSAGE);
            }
            
            //total messages 
            JOptionPane.showMessageDialog(null, 
                "All messages processed!\nTotal messages sent: " + Message.returnTotalMessages(), 
                "Completion", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}