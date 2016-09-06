package pw.arcticwind.expressnow.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.List;

import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.utils.MyApp;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ScrollingTextView extends LinearLayout {

    private Scroller scroller;
    private Context context;

    private List<String> titleList;
    private static final int DURING_TIME = 2000;
    private static final int PX_50 = MyApp.dp2Px(50);

    public ScrollingTextView(Context context) {
        super(context);
        init();
    }

    public ScrollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ScrollingTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    private class ViewHolder {
        TextView nameTv;
    }

    public void setData(List<String> titleList) {
        this.titleList = titleList;
        if (titleList != null) {
            removeAllViews();
            int size = titleList.size();
            for (int i = 0; i < size; i++) {
                addContentView(i);
            }
            if (titleList.size() > 3) {
                getLayoutParams().height = PX_50;
                //停止
                scrollWait();
                handler.sendEmptyMessageDelayed(0, DURING_TIME);
                //滚动
                smoothScroll(0, PX_50);
            }
        }
    }

    private void addContentView(int position) {
        ViewHolder holder;
        if (position >= getChildCount()) {
            holder = new ViewHolder();
            View v = View.inflate(getContext(), R.layout.cell_scrolling_text, null);
            holder.nameTv = (TextView) v.findViewById(R.id.tv);
            v.setTag(holder);
            addView(v, LinearLayout.LayoutParams.MATCH_PARENT, PX_50);
        } else {
            holder = (ViewHolder) getChildAt(position).getTag();
        }
        final String title = titleList.get(position);
        holder.nameTv.setText(title);
    }

    private void resetView() {
        String title = titleList.get(0);
        titleList.remove(0);
        titleList.add(title);

        for (int i = 0; i < 4; i++) {
            addContentView(i);
        }
    }

    public void scrollWait() {
        handler.removeMessages(0);
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, DURING_TIME);
            smoothScroll(0, PX_50);
            resetView();
        };
    };

    public void smoothScroll(int dx, int dy) {
        scroller.startScroll(scroller.getFinalX(), 0, dx, dy, DURING_TIME);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }


}
