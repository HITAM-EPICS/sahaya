package hitam.epics.sahaya.support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by sanjit on 5/1/17.
 */

public class DiscussionMessage {
    private String name;
    private String message;
    private String uid;
    private String photoUrl;
    private long time;

    public DiscussionMessage(String name, String message, String uid, String photoUrl, long time) {
        this.name = name;
        this.message = message;
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.time = time;
    }

    public DiscussionMessage(String name, String message) {
        this.name = name;
        this.message = message;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            this.uid = currentUser.getUid();
            this.photoUrl = String.valueOf(currentUser.getPhotoUrl());
        }
        time = System.currentTimeMillis();
    }

    public DiscussionMessage() {
    }

    public String getUid() {
        return uid;
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

    public String getPhotoUrl() {
        return photoUrl;
    }
}
