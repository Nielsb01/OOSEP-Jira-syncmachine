package nl.avisi.dto;

public class UserDTO {
   private int userID;
   private String username;
   private String password;

    public int getUserID() {
        return userID;
    }

    public UserDTO setUserID(int userID) {
        this.userID = userID;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
