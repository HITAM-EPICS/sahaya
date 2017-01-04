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

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
        ).toString();
    }

    public long getTimeNumber() {
        return time;
    }
}
