package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 16/2/17.
 */

public class AnnouncementItem {
    private String name;
    private String announcement;
    private long time;

    public AnnouncementItem() {
    }

    public AnnouncementItem(String name, String announcement, long time) {
        this.name = name;
        this.announcement = announcement;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public long getTime() {
        return time;
    }
}
