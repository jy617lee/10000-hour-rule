package tenthousand.hour.law.outliers.dashboard;

/**
 * Created by jeeyu_000 on 2016-09-17.
 */
public class Purpose {
    private String purposeName;
    private String goalTime;
    private String start;
    private String end;
    private String finalGoal;
    private int curTime;

    public Purpose(String purposeName, String goalTime, String start, String end, String finalGoal) {
        this(purposeName, goalTime, start, end, finalGoal, 0);
    }

    public Purpose(String purposeName, String goalTime, String start, String end, String finalGoal, int curTime) {
        this.purposeName = purposeName;
        this.goalTime = goalTime;
        this.start = start;
        this.end = end;
        this.finalGoal = finalGoal;
        this.curTime = curTime;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }

    public String getGoalTime() {
        return goalTime;
    }

    public void setGoalTime(String goalTime) {
        this.goalTime = goalTime;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getFinalGoal() {
        return finalGoal;
    }

    public void setFinalGoal(String finalGoal) {
        this.finalGoal = finalGoal;
    }

    public int getCurTime() {
        return curTime;
    }

    public void setCurTime(int curTime) {
        this.curTime = curTime;
    }

    @Override
    public String toString() {
        return "Purpose{" +
                "purposeName='" + purposeName + '\'' +
                ", goalTime='" + goalTime + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", finalGoal='" + finalGoal + '\'' +
                '}';
    }
}
