package co.example.hp.myapplication.classes;

public class LoggedInUser {



    private String username;
    private String authToken;

    public LoggedInUser(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
