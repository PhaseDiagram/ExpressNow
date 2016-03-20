package pw.arcticwind.expressnow.activity.startup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.Bmob;
import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.activity.DrawerActivity;
import pw.arcticwind.expressnow.utils.KeyStore;

//启动界面
//在此判断是否首次启动, 做一些初始化工作等等
public class StartupActivity extends Activity {

    private static final int TIME_TO_WAIT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bmob.initialize(this, KeyStore.BMOB_KEY);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Runnable runnable = new Runnable() {
            public void run() {
                SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
                Boolean first = preferences.getBoolean("first", true);
                Intent intent = null;
                if (first) {
                    File dirHistory = new File(getFilesDir().toString() + File.separator + "history" + File.separator);
                    if (!dirHistory.mkdirs()) {
                        Toast.makeText(StartupActivity.this, "目录创建失败！", Toast.LENGTH_SHORT).show();
                    }
                    File dirFavor = new File(getFilesDir().toString() + File.separator + "favor" + File.separator);
                    if (!dirFavor.mkdirs()) {
                        Toast.makeText(StartupActivity.this, "目录创建失败！", Toast.LENGTH_SHORT).show();
                    }
                    intent = new Intent(StartupActivity.this, FirstVisitActivity.class);
                } else {
                    intent = new Intent(StartupActivity.this, DrawerActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable, TIME_TO_WAIT);
    }

}
