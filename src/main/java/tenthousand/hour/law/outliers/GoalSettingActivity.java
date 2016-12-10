package tenthousand.hour.law.outliers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
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
        String mTime = time.getSelectedItem().toString();
        String mStart = start.getText().toString();
        String mEnd = end.getText().toString();
        String mFinalGoal = "";
        Log.d(TAG, mPurposeName + " " + mTime + " "+ mStart + " " + mEnd + " "+ mFinalGoal);

        //save sharedPreference
        PurposeManager.setPurpose(getApplicationContext(), mPurposeName, mTime, mStart, mEnd, mFinalGoal);

        //go DashboardActivity
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean checkInputs(){
        if(purposeName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "No Purpose Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.start)
    public void start(){
        Log.d(TAG, "start clicked");
        showDatePickerDialog(Constants.start);
    }

    @OnClick(R.id.end)
    public void end(){
        Log.d(TAG, "end clicked");
        showDatePickerDialog(Constants.end);
    }

    static StyledTextView start;
    static StyledTextView end;
    @BindView(R.id.purposeName) StyledEditTextView purposeName;
    @BindView(R.id.time)        Spinner time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting);
        ButterKnife.bind(this);
        start = (StyledTextView) findViewById(R.id.start);
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

        purposeName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    public void setDefaultDate(){
        Log.d(TAG, "setDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        start.setText(date);
        end.setText(date);
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
            date.append(year);
            date.append("-");
            String m = (month + 1) > 9 ? ("" + (month + 1)) : ("0" + (month + 1));
            date.append(m);
            date.append("-");
            String d = day > 9 ? day+"" : "0"+day;
            date.append(d);
            switch(flag){
                case Constants.start : {
                    if(!checkStartDate(year, month+1, day)){
                        Toast.makeText(getContext(), "Start cannot be later than End", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    start.setText(date);
                    break;
                }
                case Constants.end : {
                    if(!checkEndDate(year, month+1, day)){
                        Toast.makeText(getContext(), "End cannot be earlier than Start", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    end.setText(date);
                    break;
                }
            }
        }
    }

    public static boolean checkEndDate(int year, int month, int day){
        String startDate[] = start.getText().toString().split("-");
        if(Integer.parseInt(startDate[0]) > year){
            return false;
        } else if(Integer.parseInt(startDate[1]) > month) {
            return false;
        } else if(Integer.parseInt(startDate[2]) > day) {
            return false;
        }
        return true;
    }

    public static boolean checkStartDate(int year, int month, int day){
        String endDate[] = end.getText().toString().split("-");
        if(Integer.parseInt(endDate[0]) < year){
            return false;
        } else if(Integer.parseInt(endDate[1]) < month) {
            return false;
        } else if(Integer.parseInt(endDate[2]) < day) {
            return false;
        }
        return true;
    }

}
