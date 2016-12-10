package tenthousand.hour.law.outliers;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import tenthousand.hour.law.outliers.utils.Constants;

/**
 * Created by jeeyu_000 on 2016-12-09.
 */
public class TimerService extends Service {
    String start;
    String stop;
    private String statusFlag;
    private String TAG = "TimerService";

    private int curTime = 0;
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        setStrings();

//        String val = intent.getStringExtra(Constants.startOrStop);
//        Log.d(TAG, val);
//        if(val.equals(start)){
//            statusFlag = start;
//            mTimer = new Timer();
//            mTimer.schedule(new CustomTimerTask(), 1000, 1000);
//        }else{
//            mTimer.cancel();
//            statusFlag = stop;
//            curTime = 0;
//        }
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
            Log.d(TAG, "TICK"+curTime);
            setCurTime(INTERVAL);
            Intent timeIntent = new Intent(Constants.tick);
            timeIntent.setAction(Constants.tick);
            timeIntent.putExtra(Constants.curTime, curTime);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(timeIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        startForeground(1,new Notification());
        mTimer = new Timer();
        mTimer.schedule(new CustomTimerTask(), 1000, 1000);
//        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        Notification notification;
//
//        notification = new Notification.Builder(getApplicationContext())
//                .setContentTitle("")
//                .setContentText("")
//                .build();
//
//        nm.notify(startId, notification);
//        nm.cancel(startId);

        return super.onStartCommand(intent, flags, startId);
    }


//    @Override
//    protected void onHandleIntent(Intent intent) {
//        String val = intent.getStringExtra(Constants.startOrStop);
//        Log.d(TAG, val);
//        if(val.equals(start)){
//            statusFlag = start;
//            mTimer = new Timer();
//            mTimer.schedule(new CustomTimerTask(), 1000, 1000);
//        }else{
//            mTimer.cancel();
//            statusFlag = stop;
//            curTime = 0;
//        }
//    }

    public int getCurTime() {
        return curTime;
    }

    private void setCurTime(int curTime) {
        this.curTime += curTime;
    }
}
