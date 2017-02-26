package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 3/1/17.
 */

public class CenterItem {
    private String name;
    private double lat;
    private double lon;
    private String location;
    private String address;

    public CenterItem(String name, double lat, double lon, String location, String address) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.location = location;
        this.address = address;
    }

    public CenterItem() {
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

}
