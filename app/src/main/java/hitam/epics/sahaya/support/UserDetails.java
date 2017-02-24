package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 23/2/17.
 */

public class UserDetails {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private String occupation;
    private long attendance;
    private long last_update;
    private boolean last_update_present;

    public UserDetails() {
    }

    public UserDetails(String uid, String name, String email, String phone, String occupation,
                       long attendance, long last_update, boolean last_update_present) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.occupation = occupation;
        this.attendance = attendance;
        this.last_update = last_update;
        this.last_update_present = last_update_present;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getOccupation() {
        return occupation;
    }

    public long getAttendance() {
        return attendance;
    }

    public void setAttendance(long attendance) {
        this.attendance = attendance;
    }

    public long getLast_update() {
        return last_update;
    }

    public void setLast_update(long last_update) {
        this.last_update = last_update;
    }

    public boolean isLast_update_present() {
        return last_update_present;
    }

    public void setLast_update_present(boolean last_update_present) {
        this.last_update_present = last_update_present;
    }
}
