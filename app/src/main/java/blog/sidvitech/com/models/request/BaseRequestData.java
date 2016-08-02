package blog.sidvitech.com.models.request;

/**
 * Created by vishal on 16/6/16.
 */
public class BaseRequestData {
    private final String USER_APP_TAG = "USER_APP";

    public String getSource() {
        return USER_APP_TAG;
    }
}
