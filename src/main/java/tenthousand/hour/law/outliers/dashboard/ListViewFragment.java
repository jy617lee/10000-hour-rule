package tenthousand.hour.law.outliers.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tenthousand.hour.law.outliers.Item;
import tenthousand.hour.law.outliers.ItemAdapter;
import tenthousand.hour.law.outliers.R;
import tenthousand.hour.law.outliers.utils.EndlessScrollListener;

/**
 * Created by jeeyu_000 on 2016-12-10.
 */
public class ListViewFragment extends Fragment {
    private static String TAG = "ListViewFragment";
    @BindView(R.id.dailyRecords)    ListView listView;

    private static List<Records> records;
    private static ArrayList<Item> arrayOfItems;
    private static ItemAdapter itemsAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListView();
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fr_dashboard_listview, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void setListView(){
        arrayOfItems = new ArrayList<Item>();
        itemsAdapter = new ItemAdapter(getContext(), arrayOfItems);
        listView.setAdapter(itemsAdapter);
        EndlessScrollListener listener = new EndlessScrollListener();
        listener.setOnScrollEndListener(new EndlessScrollListener.OnScrollEnd() {
            @Override
            public void loadMore(int totalItemCount) {
                //토탈카운트 다음것부터 10개를 불러온다
                String curLastStart = (records.get(records.size() - 1).day);
//                SELECT * FROM XXX WHERE start < '2016 09 28'
                records = Records.find(Records.class, "day < '" + curLastStart + "'", null, null, "day desc", "10");
                if(records.equals(null)){
                }else{
//                item = new String[records.size()];
                    for(int i = 0; i < records.size(); i++){
                        String date = records.get(i).day;
                        int[] duration = getTimeInFormatForListView(records.get(i).duration);
                        Item newItem = new Item(date, duration);
                        itemsAdapter.add(newItem);
                    }
                }
            }
        });
        listView.setOnScrollListener(listener);
        try{
            //find(Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {
            records = Records.find(Records.class, null, null, null, "day desc", "10");
            if(records.equals(null)){
            }else{
//                item = new String[records.size()];
                for(int i = 0; i < records.size(); i++){
                    String date = records.get(i).day;
                    int[] duration = getTimeInFormatForListView(records.get(i).duration);
                    Item newItem = new Item(date, duration);
                    itemsAdapter.add(newItem);
                }
            }

        }catch(Exception e){
            Log.d("records", "err");
            e.printStackTrace();
        }
    }

    int[] getTimeInFormatForListView(int curTime){
        int [] res = new int[2];
        int hour; int min;
        res[0] = hour = curTime / 3600;
        res[1] = min = curTime % 3600 / 60;
        return res;
    }

    //// TODO: 2016-12-11 이 부분의 구조가 좀 이상한거 같은데...
    public static void addNewData(Item newItem, int purposeCurTime) {
       Log.d(TAG, "addNewData");
        arrayOfItems.add(0, newItem);
        itemsAdapter.refresh(arrayOfItems);
        itemsAdapter.notifyDataSetChanged();
//        listView.smoothScrollToPosition(0);
    }

    public static String getRecentData(){
        try {
            Item item = arrayOfItems.get(0);
            String recendData = item.date;
            return recendData;
        }catch (Exception e){

        }
        return null;
    }

    public static void modifyNewData(Item item){
        arrayOfItems.remove(0);
        arrayOfItems.add(0, item);
        itemsAdapter.refresh(arrayOfItems);
        itemsAdapter.notifyDataSetChanged();
    }
}
