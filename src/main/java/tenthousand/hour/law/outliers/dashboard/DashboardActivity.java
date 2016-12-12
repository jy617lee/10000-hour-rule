package tenthousand.hour.law.outliers.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.orm.SugarContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tenthousand.hour.law.outliers.Item;
import tenthousand.hour.law.outliers.R;
import tenthousand.hour.law.outliers.TimerService;
import tenthousand.hour.law.outliers.utils.Constants;
import tenthousand.hour.law.outliers.utils.StyledTextView;

/**
 * Created by jeeyu_000 on 2016-09-17.
 */
public class DashboardActivity extends AppCompatActivity {
    private String TAG = "DashboardActivity";

    @BindView(R.id.goal)    StyledTextView goal;
    @BindView(R.id.timer)   StyledTextView timer;
    @BindView(R.id.btnStart)Button btnStart;
    @BindView(R.id.btnListOrInfo)   Button btnListOrInfo;

    @BindString(R.string.btnStart) String start;
    @BindString(R.string.btnStop)   String stop;
    @BindString(R.string.initTime)  String initTime;
    @BindString(R.string.hoursOf)  String hoursOf;

    private Purpose purpose;
    private Records record;
    private String startTime;
    private int duration;

    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        SugarContext.init(this);
        listViewFragment();

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        purpose = PurposeManager.getPurpose(getApplicationContext());
        setGoal();
//        setDate();
//        setTime();
        setTimer(initTime);
//        setListView();
    }


    @OnClick(R.id.btnListOrInfo)
    public void transFragment(){
        Log.d(TAG, "btnListOrInfo clicked");
        if(btnListOrInfo.getText() == "리스트"){
            btnListOrInfo.setText("인포");
            listViewFragment();

        }else{
            btnListOrInfo.setText("리스트");
            infographicFragment();
        }
    }
    FragmentTransaction ft;
    public void infographicFragment(){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.goalTime, purpose.getGoalTime());
        bundle.putString(Constants.end, purpose.getEnd());
        bundle.putInt(Constants.curTime, purpose.getCurTime());
        InfographicFragment infoFragment = new InfographicFragment();
        infoFragment.setArguments(bundle);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frDashboard, infoFragment);
        ft.commit();
    }

    public void listViewFragment(){
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frDashboard, new ListViewFragment());
        ft.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.tick);
        LocalBroadcastManager.getInstance(this).registerReceiver(timerReciever, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(timerReciever);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }


    @OnClick(R.id.btnStart)
    public void startTimer(){
        if(btnStart.getText().equals(start)){
            Log.d(TAG, "startTimer click");
            btnStart.setText(stop);
            startTime = dateFormat.format(new Date(System.currentTimeMillis()));
            startTimerService(start);
            Log.d(TAG, startTime);
        }else{
            //STOP
            Log.d(TAG, "stopTimer click");
            startTimerService(stop);
            btnStart.setText(start);
            timer.setText(initTime);
            setDuration(curTick);
            curTick = 0;
            setTimer(initTime);

            record = new Records(getStartTime(), getEndTime(), getDuration());
            record.save();


            String date = record.start.substring(0, 10);
            String time = record.start.substring(11, 16) + " ~ " + record.end.substring(11, 16);
            int[] duration = getTimeInFormatForListView(record.duration);
            Item newItem = new Item(date, time, duration);
//            //TODO 이걸 10번에 한번은 db에서 불러온다던지
            PurposeManager.setCurTime(purpose.getCurTime() + getDuration());

            ListViewFragment.addNewData(newItem, purpose.getCurTime());
//            setTime();
        }
    }
    int[] getTimeInFormatForListView(int curTime){
        int [] res = new int[2];
        int hour; int min;
        res[0] = hour = curTime / 3600;
        res[1] = min = curTime % 3600 / 60;
        return res;
    }
    public String getStartTime(){
        Log.d(TAG, "getStartTime" + startTime);
        return startTime;
    }

    public String getEndTime(){
        String endTime = dateFormat.format(new Date(System.currentTimeMillis()));
        Log.d(TAG, "getEndTime" + endTime);
        return endTime;
    }

    public int getDuration(){
        Log.d(TAG, "getDuration" + duration);
        return duration;
    }

    public void setDuration(int duration){
        Log.d(TAG, "setDuration" + duration);
        this.duration = duration;
    }

    int curTick = 0;

    //**이걸 getTimeInFormat이랑 합치자


    public String getTimeInFormat(int curTime){
        //seconds to hour:min:sec format
        String split = ":";

        StringBuffer res = new StringBuffer(6);
        int hour; int min; int sec;
        sec = curTime % 60;
        min = curTime % 3600 / 60;
        hour = curTime / 3600;
//        Log.d(TAG, hour + " : " + min);

        if(hour == 0){
            res.append("00");
        }else if(hour < 10){
            res.append("0");
            res.append(hour);
        }else{
            res.append(hour);
        }

        res.append(split);

        if(min == 0){
            res.append("00");
        }else if(min < 10){
            res.append("0");
            res.append(min);
        }else{
            res.append(min);
        }

        res.append(split);

        if(sec == 0){
            res.append("00");
        }else if(sec < 10){
            res.append("0");
            res.append(sec);
        }else{
            res.append(sec);
        }


//        Log.d(TAG, "getTimeInFormat " + res);
        return res.toString();
    }

    void setGoal(){
        String mGoal = purpose.getPurposeName();
         //+ " " + purpose.getFinalGoal();
        goal.setText(mGoal);
    }

//    void setDate(){
//        String mDate = purpose.getStart() + " ~ " + purpose.getEnd();
//        date.setText(mDate);
//    }

//    void setTime(){
//        String mTime = secToHour(purpose.getCurTime()) + " " + hoursOf + " " + purpose.getGoalTime() + " ";
//        time.setText(mTime);
//    }

    void setTimer(String time){
        timer.setText(time);
    }

    String secToHour(int sec){
        StringBuffer res = new StringBuffer(4);
        int hour; int min;
        hour = sec / 3600;
        min = (sec % 3600) / 60 * 10 / 60;
        if(hour != 0){
            res.append(hour);
            res.append(".");
        }else{
            res.append("0.");
        }
        res.append(min);
        Log.d(TAG, "secToHour " + sec + " to " + res + "hour");
        return res.toString();
    }

    //====================================
    //Service
    //====================================
    public void startTimerService(String startOrStop){
        Log.d(TAG, "startTimerService");
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra(Constants.startOrStop, startOrStop);
        startService(intent);
    }

    private BroadcastReceiver timerReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "timerReceiver");
            switch(intent.getAction()){
                case Constants.tick : {
                    Bundle bundle = intent.getExtras();
                    curTick = bundle.getInt(Constants.curTick, 0);
                    setTimer(getTimeInFormat(curTick));
                    break;
                }
            }
        }
    };
}
