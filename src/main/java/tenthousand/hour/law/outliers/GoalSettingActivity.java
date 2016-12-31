package tenthousand.hour.law.outliers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tenthousand.hour.law.outliers.dashboard.DashboardActivity;
import tenthousand.hour.law.outliers.dashboard.PurposeManager;
import tenthousand.hour.law.outliers.utils.Constants;
import tenthousand.hour.law.outliers.utils.StyledEditTextView;
import tenthousand.hour.law.outliers.utils.StyledTextView;

public class GoalSettingActivity extends AppCompatActivity {

    private String TAG = "GoalSettingActivity";

    @OnClick(R.id.btnSave)
    public void save(){
        Log.d(TAG, "btnSave click");
        if(!checkInputs()){
            return;
        }
        //get settings
        String mPurposeName = purposeName.getText().toString();
        String mTime = time.getText().toString();;
        String mEnd = end.getText().toString();
        String mFinalGoal = "";

        //// TODO: 2016-12-24 start 날짜 오늘 날짜로 넣어주기
//        Log.d(TAG, mPurposeName + " " + mTime + " "+ mStart + " " + mEnd + " "+ mFinalGoal);

        //save sharedPreference
        PurposeManager.setPurpose(getApplicationContext(), mPurposeName, mTime, null, mEnd, mFinalGoal);

        //go DashboardActivity
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean checkInputs(){
        if(purposeName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "No Purpose Name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(time.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "No Goal Time", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!dateInput){
            Toast.makeText(getApplicationContext(), "No End date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @OnClick(R.id.end)
    public void end(){
        Log.d(TAG, "end clicked");
        showDatePickerDialog(Constants.end);
    }

    static StyledTextView end;
    private static boolean dateInput = false;
    @BindView(R.id.purposeName) StyledEditTextView purposeName;
    @BindView(R.id.purposeDetail) StyledEditTextView purposeDetail;
    @BindView(R.id.time)        StyledEditTextView time;
    @BindView(R.id.resolution ) StyledEditTextView resolution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);
        ButterKnife.bind(this);
        end = (StyledTextView) findViewById(R.id.end);

        //if there's no existing purpose, show xml
        if(PurposeManager.getPurpose(getApplicationContext()) == null){
            setDefaultDate();
        }
        //if there's existing purpose, go to DashboardActivity
        else{
            Intent goDashboard = new Intent(this, DashboardActivity.class);
            startActivity(goDashboard);
            finish();
        }

        StyledEditTextView[] editTextArr = {purposeName, purposeDetail, time, resolution};
        initEdittext(editTextArr);

    }

    private void initEdittext(StyledEditTextView [] arr) {
        for(int i = 0; i < arr.length; i++) {
            arr[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    return false;
                }
            });

//            arr[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//                        View view = getCurrentFocus();
//                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                        try{
//                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                        }catch(NullPointerException e){
//                            e.printStackTrace();
//                        }
//                        return true;
//                    }
//                    return false;
//                }
//            });
        }
    }

    public void setDefaultDate(){
        Log.d(TAG, "setDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
        String date = sdf.format(new Date());
        end.setText("ex) " + date);
    }

    public void showDatePickerDialog(String flag) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.flagForStartEnd, flag);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener{
        String flag;

        @Override @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //set flag
            flag = (String) getArguments().get(Constants.flagForStartEnd);
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            StringBuffer date = new StringBuffer(11);
            date.append(year-2000);
            date.append("/");
            String m = (month + 1) > 9 ? ("" + (month + 1)) : ("0" + (month + 1));
            date.append(m);
            date.append("/");
            String d = day > 9 ? day+"" : "0"+day;
            date.append(d);
            switch(flag){
                case Constants.end : {
                    if(!checkEndDate(year, month+1, day)){
                        Toast.makeText(getContext(), "goal date is earlier than today", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(Build.VERSION.SDK_INT < 23){
                        end.setTextColor(getResources().getColor(R.color.blue));

                    }else{
                        end.setTextColor(getResources().getColor(R.color.blue, null));
                    }

                    dateInput = true;
                    end.setText(date);
                    break;
                }
            }
        }
    }

    public static boolean checkEndDate(int year, int month, int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date());
        String startDate[] = date.split("/");
        if(Integer.parseInt(startDate[0]) > year){
            return false;
        }else if(Integer.parseInt(startDate[0]) == year){
            if(Integer.parseInt(startDate[1]) > month) {
                return false;
            } else if(Integer.parseInt(startDate[1]) == month){
                if(Integer.parseInt(startDate[2]) >= day) {
                    return false;
                }else{
                    return true;
                }
            }else{
                return true;
            }
        }else{
            return true;
        }
    }
}
