package hitam.epics.sahaya.support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by sanjit on 5/1/17.
 */

public class DiscussionMessage {
    private String name;
    private String message;
    private String UID;
    private long time;

    public DiscussionMessage(String name, String message, String UID, long time) {
        this.name = name;
        this.message = message;
        this.UID = UID;
        this.time = time;
    }


    public DiscussionMessage(String name, String message) {
        this.name = name;
        this.message = message;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            this.UID = currentUser.getUid();
        }
        time = System.currentTimeMillis();
    }

    public DiscussionMessage(String name, String message, long time) {
        this.name = name;
        this.message = message;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            this.UID = currentUser.getUid();
        }
        this.time = time;
    }

    public DiscussionMessage() {
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

}
