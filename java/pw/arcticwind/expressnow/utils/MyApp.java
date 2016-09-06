package pw.arcticwind.expressnow.utils;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/2.
 */
public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static void toast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static float sp2Px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int dp2Px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
