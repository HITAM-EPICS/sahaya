package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 22/5/17.
 */

public class Curriculum {
    private int id;
    private String title;
    private String description;
    private boolean status;

    public Curriculum() {
    }

    public Curriculum(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        status = false;
    }

    @Override
    public String toString() {
        return id + " : " + title + "\n" + description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setCompleted() {
        status = true;
    }

}
