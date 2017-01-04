package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 2/1/17.
 */

public class DashboardItem {
    private int ImageResource;
    private String name;

    public int getImageResource() {
        return ImageResource;
    }

    public String getName() {
        return name;
    }

    public DashboardItem(int imageResource, String name) {

        ImageResource = imageResource;
        this.name = name;
    }

    public DashboardItem() {

    }
}
