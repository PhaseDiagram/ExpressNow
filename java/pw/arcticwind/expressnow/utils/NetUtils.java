package pw.arcticwind.expressnow.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//判断网络情况的工具类
//都是供调用的静态方法
public class NetUtils {

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }
}
