package tenthousand.hour.law.outliers;

/**
 * Created by jeeyu_000 on 2016-10-01.
 */
public class Item {
    public String date;
    public String time;
    public int[] duration;
    public String hourNum;
    public String minNum;

    public Item(String date, int [] duration){
        this.date = date;
        this.duration = duration;
    }

    public Item(String date, String time, int [] duration){
        this.date = date;
        this.time = time;
        this.duration = duration;
    }
}
