package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 14/2/17.
 */

public class RoleItem {
    private final String username;
    private String role;

    public RoleItem(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
