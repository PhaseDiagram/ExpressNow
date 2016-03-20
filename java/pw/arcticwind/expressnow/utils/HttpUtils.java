package pw.arcticwind.expressnow.utils;

import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//HTTP 请求的工具类
//都是供调用的静态方法
public class HttpUtils {
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }


                    if (listener != null) {
                        listener.onHttpFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onHttpError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void queryFromServer(final String expressCom, final String num, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://api.ickd.cn/?id=" + KeyStore.API_ID + "&secret=" + KeyStore.API_KEY +
                        "&com=" + expressCom + "&nu=" + num + "&type=json&encode=utf8&ord=desc";
                sendHttpRequest(url, listener);
            }
        }).start();
    }

    public interface HttpCallbackListener {

        void onHttpFinish(String response);

        void onHttpError(Exception e);

    }
}
