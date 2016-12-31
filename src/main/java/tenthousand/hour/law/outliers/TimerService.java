package tenthousand.hour.law.outliers;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import tenthousand.hour.law.outliers.utils.Constants;

/**
 * Created by jeeyu_000 on 2016-12-09.
 */
public class TimerService extends IntentService{
    String start;
    String stop;
    private String statusFlag;
    private String TAG = "TimerService";
    public TimerService(){
        super("test-service");
    }

    private int curTime = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        setStrings();
    }

    public void setStrings(){
        start = getString(R.string.btnStart);
        stop = getString(R.string.btnStop);
        statusFlag = stop;
    }
    final static int INTERVAL = 1;
    final static int TIMEOUT = 86400;
    private static Timer mTimer;


    public class CustomTimerTask extends TimerTask{
        @Override
        public void run(){
            if(curTime == TIMEOUT){
                this.cancel();
            }
            setCurTime(INTERVAL);
            Intent timeIntent = new Intent(Constants.tick)
                    .putExtra(Constants.curTick, getCurTime());
            Log.d(TAG, "TICK" + curTime);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(timeIntent);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String val = intent.getStringExtra(Constants.startOrStop);
        Log.d(TAG, val);
        if(val.equals(start)){
            statusFlag = start;
            mTimer = new Timer();
            mTimer.schedule(new CustomTimerTask(), 1000, 1000);
        }else{
            if(mTimer != null){
                mTimer.cancel();
                statusFlag = stop;
                curTime = 0;
            }
        }
    }


    public int getCurTime() {
        return curTime;
    }

    private void setCurTime(int curTime) {
        this.curTime += curTime;
    }
}
