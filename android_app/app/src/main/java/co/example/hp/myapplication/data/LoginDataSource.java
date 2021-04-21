package co.example.hp.myapplication.data;

import java.io.IOException;

import co.example.hp.myapplication.classes.LoggedInUser;
import co.example.hp.myapplication.classes.RegistredUser;


public class LoginDataSource {


    private LoggedInUser user = null;
    private RegistredUser userR = null;

    public Result<LoggedInUser> login(final String username, String authToken) {
        try {
            user = new LoggedInUser(username, authToken);
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
    public Result<RegistredUser> register(final String username) {
        try {
            userR = new RegistredUser(username);
            return new Result.Success<>(userR);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error register ", e));
        }
    }

    public void logout() {

    }
}
