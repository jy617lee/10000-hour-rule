package tenthousand.hour.law.outliers.utils;

import android.widget.AbsListView;

/**
 * Created by jeeyu_000 on 2016-09-25.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {
    private int visibleThreshold = 10;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    public EndlessScrollListener() {
    }
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public interface OnScrollEnd{
        void loadMore(int totalItemCount);
    }
    private OnScrollEnd mScrollEndListener;
    public void setOnScrollEndListener(OnScrollEnd listner) {
        mScrollEndListener = listner;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//        Log.d("SCROLLLLL", "total : " + totalItemCount);
        if(loading){
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }else{
            if(totalItemCount - firstVisibleItem <= visibleItemCount && totalItemCount > 8){
                if(mScrollEndListener != null) {
//                    Log.d("SCROLLLLL", "END + total : " + totalItemCount);
                    mScrollEndListener.loadMore(totalItemCount);
                    loading = true;
                }
            }
        }

//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//                currentPage++;
//            }
//        }
//        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//            // I load the next page of gigs using a background task,
//            // but you can call any function here.
//            Log.d("SCROLLLLL ENNNDDD", "total" + totalItemCount + "visible" + visibleItemCount + "first" + firstVisibleItem + "thres" + visibleThreshold);
//            loading = true;
//        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
