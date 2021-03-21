package nl.hu.cisq1.lingo.security.presentation;

public class UserDto{
    public String username;
    public double score;

    public UserDto(String username, Double score) {
        this.username = username;
        this.score = score;
    }
}