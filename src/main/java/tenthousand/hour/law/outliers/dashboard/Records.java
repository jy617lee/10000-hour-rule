package tenthousand.hour.law.outliers.dashboard;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by jeeyu_000 on 2016-09-24.
 */

public class Records extends SugarRecord {
    private Long id;
    @Unique
    String day;
    int duration;
    int accumulation = 0;

    public Records(){ }

    public Records(String day, int duration, int accumulation){
        this.day = day;
        this.duration = duration;
        this.accumulation = accumulation;
    }

    public Long getId() {
        return id;
    }
}
