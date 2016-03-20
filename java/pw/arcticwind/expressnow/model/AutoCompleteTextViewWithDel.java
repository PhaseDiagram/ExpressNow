package pw.arcticwind.expressnow.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import pw.arcticwind.expressnow.R;

//这个AutoCompleteTextView 能保存历史记录, 有清空当前输入标识
public class AutoCompleteTextViewWithDel extends AutoCompleteTextView {
    /*
    android:dropDownHeight="360dp"
    android:completionThreshold="1"
    */

    private Drawable drawable;
    private Context context;
    private static final int MAX_HISTORY = 50;
    private static final String FILE_NAME = "auto_history";

    private ArrayAdapter<String> adapter;


    public AutoCompleteTextViewWithDel(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AutoCompleteTextViewWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AutoCompleteTextViewWithDel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        drawable = ContextCompat.getDrawable(context, R.drawable.ic_clear_black_24dp);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (length() == 0) {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //水平方向上，点击的位置在 控件右 - 图标宽 且图标与控件右部的缝隙也考虑了
        //图标与控件之间的缝隙也纳入范围。不纳入的话，最后一项改为 (event.getX() < (getWidth() - getPaddingRight())
        if (event.getAction() == MotionEvent.ACTION_UP && event.getX() > (getWidth() - getTotalPaddingRight()) && event.getX() < getWidth()) {
            setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    //参数 field 是键值对的键名
    public void initAutoCTV(String field) {

        setAdapterFromHistory(field);

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextViewWithDel view = (AutoCompleteTextViewWithDel) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }

    public void saveAutoCTV(String field) {
        String string = getText().toString().trim();
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String historyLong = preferences.getString(field, "");
        if (!historyLong.contains(string + ",")) {
            historyLong = historyLong + string + ",";
            preferences.edit().putString(field, historyLong).apply();
        }
    }

    public static void clearHistory(Context context, String field) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(field).apply();

    }

    private void setAdapterFromHistory(String field) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String[] history = preferences.getString(field, "").split(",");
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, history);
        if (history.length > MAX_HISTORY) {
            String[] historyNew = new String[MAX_HISTORY];
            System.arraycopy(history, 0, historyNew, 0, MAX_HISTORY);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, historyNew);
        }
        setAdapter(adapter);
    }

    public void updateAdapter(String field) {
        setAdapterFromHistory(field);
        adapter.notifyDataSetChanged();
    }
}
