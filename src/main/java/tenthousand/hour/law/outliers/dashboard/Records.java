package tenthousand.hour.law.outliers.dashboard;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by jeeyu_000 on 2016-09-24.
 */

public class Records extends SugarRecord {
    private Long id;
    @Unique
    String start;
    String end;
    int duration;

    public Records(){ }

    public Records(String start, String end, int duration){
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }
}
