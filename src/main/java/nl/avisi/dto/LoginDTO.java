package nl.avisi.dto;
/**
 * Class contains the necessary information to
 * handle the logingin of a user
 */
public class LoginDTO {

    /**
     * is the username the user filled in on the frondend
     * used in the login form and send by frontend to a endpoint here in the backend in form of this DTO
     */
    private String username;

    /**
     * is the password the user filled in on the frondend
     * used in the login form and send by frontend to a endpoint here in the backend in form of this DTO
     */
    private String password;

    public String getUsername() {
        return username;
    }

    public LoginDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
