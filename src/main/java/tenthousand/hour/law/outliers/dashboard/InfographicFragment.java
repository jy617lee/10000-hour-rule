package tenthousand.hour.law.outliers.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import tenthousand.hour.law.outliers.R;
import tenthousand.hour.law.outliers.utils.Constants;
import tenthousand.hour.law.outliers.utils.ProgressBar;

/**
 * Created by jeeyu_000 on 2016-12-10.
 */
public class InfographicFragment extends Fragment {
    private String TAG = "InfographicFragment";
    @BindView(R.id.end)    TextView endView;
    @BindView(R.id.goalTime)    TextView goalTimeView;
    @BindView(R.id.todayDate)    TextView todayDateView;
    @BindView(R.id.curTime)    TextView curTimeView;
    @BindView(R.id.progressBar)    ProgressBar progressBar;
    private int goalTime, curTime;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fr_dashboard_infographic, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        setStrings(bundle);
        setProgressBar(goalTime, curTime);
        return view;
    }

    SimpleDateFormat dateFormat;
    public void setStrings(Bundle bundle){
        goalTime = Integer.valueOf(bundle.getString(Constants.goalTime));
        curTime = bundle.getInt(Constants.curTime) / 3600;
        endView.setText(bundle.getString(Constants.end));
        goalTimeView.setText(goalTime+"");
        curTimeView.setText(curTime+"");
        dateFormat = new SimpleDateFormat("yy/MM/dd");
        todayDateView.setText(dateFormat.format(new Date(System.currentTimeMillis())));
    }

    private void setProgressBar(int goalTime, int curTime){
        progressBar.setMax(goalTime);
        progressBar.setCurAmount(curTime);
    }
}
