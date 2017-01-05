package hitam.epics.sahaya.support;

import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created by sanjit on 5/1/17.
 */

public class DiscussionMessage {
    private String name;
    private String message;
    private long time;


    public DiscussionMessage(String name, String message) {
        this.name = name;
        this.message = message;
        time = System.currentTimeMillis();
    }

    public DiscussionMessage(String name, String message, long time) {
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public DiscussionMessage() {
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
