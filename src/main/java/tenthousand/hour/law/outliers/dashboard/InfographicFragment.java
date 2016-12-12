package tenthousand.hour.law.outliers.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import tenthousand.hour.law.outliers.R;
import tenthousand.hour.law.outliers.utils.Constants;

/**
 * Created by jeeyu_000 on 2016-12-10.
 */
public class InfographicFragment extends Fragment {
    private String TAG = "InfographicFragment";
    @BindView(R.id.end)    TextView end;
    @BindView(R.id.goalTime)    TextView goalTime;
    @BindView(R.id.todayDate)    TextView todayDate;
    @BindView(R.id.curTime)    TextView curTime;
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
        return view;
    }

    SimpleDateFormat dateFormat;
    public void setStrings(Bundle bundle){
        end.setText(bundle.getString(Constants.end));
        goalTime.setText(bundle.getString(Constants.goalTime));
        curTime.setText((bundle.getInt(Constants.curTime) / 3600)+"");
        dateFormat = new SimpleDateFormat("yy/MM/dd");
        todayDate.setText(dateFormat.format(new Date(System.currentTimeMillis())));
        NumberProgressBar progressBar;
    }
}
