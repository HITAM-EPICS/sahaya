package hitam.epics.sahaya.support;

/**
 * Created by sanjit on 26/2/17.
 */

public class Remark {
    private long time;
    private String name;
    private String remark;


    public Remark() {

    }

    public Remark(long time, String name, String remark) {
        this.time = time;
        this.name = name;
        this.remark = remark;
    }

    public long getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }
}
