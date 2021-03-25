package nl.hu.cisq1.lingo.security.data;


public class UserProfile {
    private String username;

    public UserProfile(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}