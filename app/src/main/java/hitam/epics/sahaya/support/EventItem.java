package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 3/1/17.
 */

public class EventItem {
    private String name;
    private String start;
    private String end;
    private String area;
    private String date;
    private double lat;
    private double lon;

    public EventItem(String name, String area) {
        this.name = name;
        start = null;
        end = null;
        this.area = area;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public EventItem(String name, String date, String start, String end, String area, double lat, double lon) {

        this.name = name;
        this.start = start;
        this.end = end;
        this.area = area;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
    }

    public EventItem() {
    }

    public String getDate() {
        return date;
    }

    public String getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "EventItem{" +
                "name='" + name + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", area='" + area + '\'' +
                ", date='" + date + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
