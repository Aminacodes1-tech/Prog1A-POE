/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package mainclass;

/**
 *
 * @author ICT 2022
 */


public class Login {
   private String name;
    private String Lastname;
     private String Username;
     private String password;
      private String phoneNumber;

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return Lastname;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
   
    
    public boolean checkUsernameComplexity(String username){
        
        
        return username.contains("_") && username.length()<=5;
    }
    
public boolean CheckPasswordComplexity(String password){
    // Check if password has at least 8 characters
    if (password.length() < 8) {
        return false;
    }
    
    // Check if password contains at least a capital letter
    boolean hasCapital = false;
    // Check if password contains at least one number
    boolean hasNumber = false;
    // Check if password contains at least one special character
    boolean hasSpecial = false;
    
    for (char c : password.toCharArray()) {
        if (Character.isUpperCase(c)) {
            hasCapital = true;
        }
        if (Character.isDigit(c)) {
            hasNumber = true;
        }
        if (!Character.isLetterOrDigit(c)) {
            hasSpecial = true;
        }
    }
    
    return hasCapital && hasNumber && hasSpecial;
    
   
    }
    
    public boolean LoginUser(String Username , String password){
     
        
        return Username.equals(this.Username) && password.equals(this.password);
    }
    
    
    public String RegisterUser(String username, String Password){
        
        if(!checkUsernameComplexity(username) && CheckPasswordComplexity(Password)){
           return "Username is not correctly formatted";
        }else if (checkUsernameComplexity(username) && !CheckPasswordComplexity(Password)){
            return "password is not correctly formatted";
        }else{
            return "the two conditions have been met and the user has registered successfully";
        }
        
        
    }
    
  
 public boolean CheckPhoneNumberComplexity(String phone_Number) {
    String pattern = "^\\+27\\d{9}$";
    return phoneNumber != null && phoneNumber.matches(pattern);
}
    
    public String ReturnLoginStatus(boolean loginSuccessful){
        if(loginSuccessful){
          return "Welcome " + getName() + " "  +getLastname() + " it is great to see you again";
        }else{
            return "Username or password is incorrect please try again";
        }
    }
   
}
