package android.ext;

import android.os.Build.VERSION;
import android.os.Handler;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.ListView;

public class FastScrollerFix implements AbsListView.OnScrollListener, Runnable {
    private int lastState;
    private ListView list;

    public FastScrollerFix(ListView list) {
        this.lastState = 0;
        this.list = list;
        FastScrollerFix.setFastScrollEnabledInternal(list, false);
        try {
            list.setOnScrollListener(this);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.AbsListView$OnScrollListener
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(this.lastState != 0) {
            return;
        }
        Handler handler0 = ThreadManager.getHandlerUiThread();
        handler0.removeCallbacks(this);
        handler0.postDelayed(this, 1000L);
    }

    @Override  // android.widget.AbsListView$OnScrollListener
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.lastState = scrollState;
        Handler handler0 = ThreadManager.getHandlerUiThread();
        handler0.removeCallbacks(this);
        if(scrollState != 0) {
            FastScrollerFix.setFastScrollEnabledInternal(this.list, true);
            return;
        }
        handler0.postDelayed(this, 1000L);
    }

    @Override
    public void run() {
        FastScrollerFix.setFastScrollEnabledInternal(this.list, false);
    }

    public static void setFastScrollEnabled(ListView list, boolean state) {
        if(list == null) {
            return;
        }
        list.setScrollBarStyle(0x1000000);
        FastScrollerFix.setFastScrollEnabledInternal(list, state);
    }

    private static void setFastScrollEnabledInternal(ListView list, boolean state) {
        if(list != null) {
            try {
                list.setFastScrollEnabled(state);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            if(Build.VERSION.SDK_INT >= 11) {
                try {
                    list.setFastScrollAlwaysVisible(state);
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                }
            }
        }
    }
}

