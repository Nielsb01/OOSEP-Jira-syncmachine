package nl.avisi.dto;
/**
 * Class contains the return value of the {@link nl.avisi.datasource.LoginDAO}
 * used to validate the login credentials and return the userID to the front end so
 * the user can use this id with every following request to the backend.
 */
public class UserDTO {

    /**
     * is the users unique id selected from the database by username
     */
    private int userID;

    /**
     * is the users unique username
     */
    private String username;

    /**
     * is the users password in a hashed form to assure privacy
     * in the validation process the filled in password in the {@link LoginDTO}
     * will be hashed and compared to this hashed password
     */
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
