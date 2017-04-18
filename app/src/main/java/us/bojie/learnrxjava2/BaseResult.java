package us.bojie.learnrxjava2;

/**
 * Created by bojiejiang on 4/16/17.
 */

public class BaseResult {

    private String message;
    private int success;
    private int id;
    private String username;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
