package us.bojie.learnrxjava2;

/**
 * Created by bojiejiang on 4/16/17.
 */

public class UserParam {

    private String username;
    private String password;

    public UserParam(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserParam() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
