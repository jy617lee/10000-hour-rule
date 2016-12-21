package tenthousand.hour.law.outliers;

/**
 * Created by jeeyu_000 on 2016-10-01.
 */
public class Item {
    public String date;
    public String time;
    public int[] duration;
    public String accumulation;

    public Item(String date, int [] duration, String accumulation){
        this.date = date;
        this.duration = duration;
        this.accumulation = accumulation;
    }
}
