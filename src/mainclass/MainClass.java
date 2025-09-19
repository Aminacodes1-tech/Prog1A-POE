/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mainclass;
import java.util.Scanner;


/**
 *
 * @author ICT 2022
 */

public class MainClass {

    public static void main(String[] args) {
        //object calling
        Login log = new Login();
        //Scanner object
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
     
        System.out.println("***LOGIN***");
        //login user
        System.out.print("Enter username to login:");
        String Secondusername=scan.next();
        System.out.print("Enter password to login:");
        String Secondpassword=scan.next();
         //display login Status
        boolean loginSuccessful= log.LoginUser(Secondusername,Secondpassword);
        String loginMessage=log.ReturnLoginStatus(loginSuccessful);
      
        if(loginSuccessful){
            System.out.println(loginMessage);
        } else{
            System.out.println("Password or username is incorrect");
        }
    }
}