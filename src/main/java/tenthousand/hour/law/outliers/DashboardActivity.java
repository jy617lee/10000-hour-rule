package tenthousand.hour.law.outliers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.orm.SugarContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tenthousand.hour.law.outliers.utils.Constants;
import tenthousand.hour.law.outliers.utils.EndlessScrollListener;
import tenthousand.hour.law.outliers.utils.StyledTextView;

/**
 * Created by jeeyu_000 on 2016-09-17.
 */
public class DashboardActivity extends AppCompatActivity {
    private String TAG = "DashboardActivity";

    @BindView(R.id.goal)    StyledTextView goal;
    @BindView(R.id.date)    StyledTextView date;
    @BindView(R.id.time)    StyledTextView time;
    @BindView(R.id.timer)   StyledTextView timer;
    @BindView(R.id.btnStart)Button btnStart;
    @BindView(R.id.dailyRecords)    ListView listView;

    @BindString(R.string.btnStart) String start;
    @BindString(R.string.btnStop)   String stop;
    @BindString(R.string.initTime)  String initTime;
    @BindString(R.string.hoursOf)  String hoursOf;
    private Purpose purpose;
    private Records record;
    private String startTime;
    private int duration;
    ItemAdapter itemsAdapter;
    SimpleDateFormat dateFormat;
    List<Records> records;
    ArrayList<Item> arrayOfItems;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        SugarContext.init(this);

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        purpose = PurposeManager.getPurpose(getApplicationContext());
        setGoal();
        setDate();
        setTime();
        setTimer(initTime);
        setListView();
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

    public void setListView(){
        arrayOfItems = new ArrayList<Item>();
        itemsAdapter = new ItemAdapter(this, arrayOfItems);
        listView.setAdapter(itemsAdapter);
        EndlessScrollListener listener = new EndlessScrollListener();
        listener.setOnScrollEndListener(new EndlessScrollListener.OnScrollEnd() {
            @Override
            public void loadMore(int totalItemCount) {
                //토탈카운트 다음것부터 10개를 불러온다

                String curLastStart = (records.get(records.size() - 1).start);
//                SELECT * FROM XXX WHERE start < '2016 09 28'
                records = Records.find(Records.class, "start < '" + curLastStart + "'", null, null, "start desc", "10");
                if(records.equals(null)){
                }else{
//                item = new String[records.size()];
                    for(int i = 0; i < records.size(); i++){
                        String date = records.get(i).start.substring(0, 10);
                        String time = records.get(i).start.substring(11, 16) + " ~ " + records.get(i).end.substring(11, 16);
                        int[] duration = getTimeInFormatForListView(records.get(i).duration);
                        Item newItem = new Item(date, time, duration);
                        itemsAdapter.add(newItem);
                    }
                }
            }
        });
        listView.setOnScrollListener(listener);
        try{
            //find(Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {
            records = Records.find(Records.class, null, null, null, "start desc", "10");
            if(records.equals(null)){
            }else{
//                item = new String[records.size()];
                for(int i = 0; i < records.size(); i++){
                    String date = records.get(i).start.substring(0, 10);
                    String time = records.get(i).start.substring(11, 16) + " ~ " + records.get(i).end.substring(11, 16);
                    int[] duration = getTimeInFormatForListView(records.get(i).duration);
                    Item newItem = new Item(date, time, duration);
                    itemsAdapter.add(newItem);
                }
            }

        }catch(Exception e){
            Log.d("records", "err");
            e.printStackTrace();
        }
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
            setDuration(curTime);
            curTime = 0;
            setTimer(initTime);

            record = new Records(getStartTime(), getEndTime(), getDuration());
            record.save();

            String date = record.start.substring(0, 10);
            String time = record.start.substring(11, 16) + " ~ " + record.end.substring(11, 16);
            int[] duration = getTimeInFormatForListView(record.duration);
            Item newItem = new Item(date, time, duration);
            arrayOfItems.add(0, newItem);
            itemsAdapter.refresh(arrayOfItems);
//            itemsAdapter.add(newItem);
            itemsAdapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(0);
            //TODO 이걸 10번에 한번은 db에서 불러온다던지
            PurposeManager.setCurTime(purpose.getCurTime() + getDuration());
            setTime();
        }
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

    int curTime = 0;

    //**이걸 getTimeInFormat이랑 합치자
    int[] getTimeInFormatForListView(int curTime){
        int [] res = new int[2];
        int hour; int min;
        res[0] = hour = curTime / 3600;
        res[1] = min = curTime % 3600 / 60;
        return res;
    }

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

    void setDate(){
        String mDate = purpose.getStart() + " ~ " + purpose.getEnd();
        date.setText(mDate);
    }

    void setTime(){
        String mTime = secToHour(purpose.getCurTime()) + " " + hoursOf + " " + purpose.getGoalTime() + " ";
        time.setText(mTime);
    }

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
//            Log.d(TAG, "timerReceiver");
            switch(intent.getAction()){
                case Constants.tick : {
                    Bundle bundle = intent.getExtras();
                    curTime = bundle.getInt(Constants.curTime, 0);
                    setTimer(getTimeInFormat(curTime));
                    break;
                }
            }
        }
    };
}
