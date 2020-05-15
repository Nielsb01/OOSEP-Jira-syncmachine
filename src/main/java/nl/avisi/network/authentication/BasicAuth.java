package nl.avisi.network.authentication;

public class BasicAuth {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BasicAuth setUsername(String username) {
        this.username = username;
        return this;
    }

    public BasicAuth setPassword(String password) {
        this.password = password;
        return this;
    }
}
