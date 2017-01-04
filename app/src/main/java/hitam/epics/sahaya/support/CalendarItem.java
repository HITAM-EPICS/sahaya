package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 3/1/17.
 */

public class CalendarItem {
    private String name;
    private String start;
    private String end;
    private String area;
    private String date;

    public CalendarItem(String name, String area) {
        this.name = name;
        start = null;
        end = null;
        this.area = area;
    }

    public CalendarItem(String name, String date, String start, String end, String area) {
        this.name = name;
        this.start = start;
        this.date = date;
        this.end = end;
        this.area = area;
    }

    public CalendarItem() {
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
}
