package blog.sidvitech.com.models.request;

/**
 * Created by vishal on 16/6/16.
 */
public class LoginRequestData extends BaseRequestData {

    private String userEmail;
    private String password;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
