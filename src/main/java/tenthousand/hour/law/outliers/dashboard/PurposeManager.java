package tenthousand.hour.law.outliers.dashboard;

import android.content.Context;
import android.content.SharedPreferences;

import tenthousand.hour.law.outliers.utils.Constants;

/**
 * Created by jeeyu_000 on 2016-09-17.
 */
public class PurposeManager {
    private static Purpose purpose = null;
    private static SharedPreferences mPurpose = null;

    public static Purpose getPurpose(Context context){
        if(purpose == null){
            purpose = create(context);
        }
        if(mPurpose == null) mPurpose = context.getSharedPreferences(Constants.purposeSP, 0);
        return purpose;
    }

    public static void setPurpose(Context context, String purposeName, String goalTime, String start, String end, String finalGoal){
        setPurpose(context, purposeName, goalTime, start, end, finalGoal, "0");
    }

    public static void setPurpose(Context context, String purposeName, String goalTime, String start, String end, String finalGoal, String curTime){
        if(mPurpose == null) mPurpose = context.getSharedPreferences(Constants.purposeSP, 0);
        editor = mPurpose.edit();
        editor.putString(Constants.purposeName,purposeName);
        editor.putString(Constants.goalTime,goalTime);
        editor.putString(Constants.start, start);
        editor.putString(Constants.end, end);
        editor.putString(Constants.finalGoal, finalGoal);
        editor.apply();
    }

    public static void setPurpose(Object o){
        purpose = null;
        editor.clear();
        editor.apply();
        mPurpose = null;
    }

    private static Purpose create(Context context){
        if(mPurpose == null) mPurpose = context.getSharedPreferences(Constants.purposeSP, 0);
        editor = mPurpose.edit();
        String purposeName = mPurpose.getString(Constants.purposeName, Constants.noName);
        if(purposeName.equals(Constants.noName)){
            return null;
        }
        String goalTime = mPurpose.getString(Constants.goalTime, Constants.noName);
        String start = mPurpose.getString(Constants.start, Constants.noName);
        String end = mPurpose.getString(Constants.end, Constants.noName);
        String finalGoal = mPurpose.getString(Constants.finalGoal, Constants.noName);
        int curTime = mPurpose.getInt(Constants.curTime, 0);

        return new Purpose(purposeName, goalTime, start, end, finalGoal, curTime);
    }

    public static SharedPreferences.Editor editor;
    public String getPurposeName() {
        return purpose.getPurposeName();
    }

    public static void setPurposeName(String purposeName) {
        purpose.setPurposeName(purposeName);
        editor.putString(Constants.purposeName, purposeName);
        editor.apply();
    }

    public String getGoalTime() {
        return purpose.getGoalTime();
    }

    public static void setGoalTime(String goalTime) {
        purpose.setGoalTime(goalTime);
        editor.putString(Constants.goalTime, goalTime);
        editor.apply();
    }

    public String getStart() {
        return purpose.getStart();
    }

    public static void setStart(String start) {
        purpose.setStart(start);
        editor.putString(Constants.start, start);
        editor.apply();
    }

    public String getEnd() {
        return purpose.getEnd();
    }

    public static void setEnd(String end) {
        purpose.setEnd(end);
        editor.putString(Constants.end, end);
        editor.apply();
    }

    public String getFinalGoal() {
        return purpose.getFinalGoal();
    }

    public static void setFinalGoal(String finalGoal) {
        purpose.setFinalGoal(finalGoal);
        editor.putString(Constants.finalGoal, finalGoal);
        editor.apply();
    }

    public static int getCurTime() {
        return purpose.getCurTime();
    }

    public static void setCurTime(int curTime) {
        purpose.setCurTime(curTime);
        editor.putInt(Constants.curTime, curTime);
        editor.apply();
    }
}
