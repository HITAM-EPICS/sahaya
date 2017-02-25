package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 2/1/17.
 */

public class DashboardItem {
    private int ImageResource;
    private String name;
    private Class associatedClass;

    public DashboardItem(int imageResource, String name, Class associatedClass) {

        ImageResource = imageResource;
        this.name = name;
        this.associatedClass = associatedClass;
    }

    public Class getAssociatedClass() {
        return associatedClass;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public String getName() {
        return name;
    }
}
