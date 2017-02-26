package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 26/2/17.
 */

public class Note {
    private long time;
    private String name;
    private String note;
    private String path;


    public Note() {

    }


    public Note(long time, String name, String note, String path) {
        this.time = time;
        this.name = name;
        this.note = note;
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
