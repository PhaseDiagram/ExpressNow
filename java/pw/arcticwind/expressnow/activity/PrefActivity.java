package pw.arcticwind.expressnow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;

import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.widget.AutoCompleteTextViewWithDel;

//设置页
//设置项: 主页设置, 清除搜索记录, 清除历史记录, 关于
//ActionBar 借鉴了 Android Studio 自动创建 Setting 的代码

//这里没有用 Toolbar, 而是用了 ActionBar. 原因见下:
//Android Studio 会自动创建一个 AppCompatPreferenceActivity, 我借鉴了相关代码改造成了这样
//如果要自己实现 Toolbar + PreferenceFragment, 是比较麻烦的:
//1. PreferenceFragment + AppCompatActivity: Fragment 与 v4.app.Fragment 不能直接混用
//2. PreferenceFragmentCompat + AppCompatActivity: 这两者有互相冲突的主题要求
//3. PreferenceFragment + Activity: 自己造不出来 Toolbar...
//设置页也不需要太复杂的逻辑, ActionBar 足矣.
public class PrefActivity extends Activity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        setupActionBar();

        getFragmentManager().beginTransaction().replace(R.id.content_pref, new PrefFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    public static class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Preference clearHistoryAuto = findPreference("pref_clear_history_autoctv");
            Preference clearHistoryFile = findPreference("pref_clear_history_file");
            Preference about = findPreference("pref_about");
            clearHistoryAuto.setOnPreferenceClickListener(this);
            clearHistoryFile.setOnPreferenceClickListener(this);
            about.setOnPreferenceClickListener(this);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "pref_clear_history_autoctv":
                    AutoCompleteTextViewWithDel.clearHistory(getActivity().getApplicationContext(), "mail_no");
                    Toast.makeText(getActivity().getApplicationContext(), "清除搜索记录!", Toast.LENGTH_SHORT).show();
                    break;
                case "pref_clear_history_file":
                    File file = new File(getActivity().getFilesDir().toString() + File.separator + "history");
                    File[] files = file.listFiles();
                    for (File temp : files) {
                        // TODO: 2016/3/10 异常处理
                        temp.delete();
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "清除历史记录!", Toast.LENGTH_SHORT).show();
                    break;
                case "pref_about":
                    Intent intent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
