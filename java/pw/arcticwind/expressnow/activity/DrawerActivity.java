package pw.arcticwind.expressnow.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.SaveListener;
import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.activity.forum.ForumActivity;
import pw.arcticwind.expressnow.model.bmob.User;

//使用意义上的主界面
//NavigationDrawer; 社区账号的登陆及注册
public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private View headerView;
    private ImageButton imageButton;
    private TextView headerText;
    private NavigationView navigationView;
    private AlertDialog loginDialog;
    private AlertDialog signupDialog;
    private ProgressDialog progressDialog;

    private boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        initView();
        initUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_drawer, new QueryFragment()).commit();

        SharedPreferences preferences = getSharedPreferences("pw.arcticwind.expressnow_preferences", MODE_PRIVATE);
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        switch (preferences.getString("home_page", "0")) {
            case "0":
                break;
            case "1":
                getSupportFragmentManager().beginTransaction().replace(R.id.content_drawer, new FavoriteFragment()).commit();
                break;
            case "2":
                Intent intent = new Intent(this, ForumActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);
        imageButton = (ImageButton) headerView.findViewById(R.id.nav_image_btn);
        headerText = (TextView) headerView.findViewById(R.id.nav_header_text);
        imageButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍候...");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedOnce) {
            super.onBackPressed();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                backPressedOnce = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_query) {
            fragmentTransaction.replace(R.id.content_drawer, new QueryFragment()).commit();
        } else if (id == R.id.nav_favor) {
            fragmentTransaction.replace(R.id.content_drawer, new FavoriteFragment()).commit();
        } else if (id == R.id.nav_history) {
            fragmentTransaction.replace(R.id.content_drawer, new HistoryFragment()).commit();
        } else if (id == R.id.nav_forum) {
            Intent intent = new Intent(this, ForumActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_image_btn:
                showLoginDialog();
                break;
            default:break;
        }
    }

    private void showLoginDialog() {
        if (signupDialog != null) {
            signupDialog.dismiss();
        }
        if (loginDialog == null) {
            buildLoginDialog();
        }
        loginDialog.show();
    }

    private void showSignupDialog() {
        if (loginDialog != null) {
            loginDialog.dismiss();
        }
        if (signupDialog == null) {
            buildSignupDialog();
        }
        signupDialog.show();
    }

    private void buildLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_login, null);
        builder.setView(view);

        final EditText email = (EditText) view.findViewById(R.id.dialog_login_email);
        final EditText pass = (EditText) view.findViewById(R.id.dialog_login_pass);

        builder.setTitle("请登录");
        builder.setPositiveButton("登陆", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                BmobQuery<User> query = new BmobQuery<User>("User");
                query.addWhereEqualTo("email", email.getText().toString());
                query.addWhereEqualTo("pass", pass.getText().toString());
                query.findObjects(DrawerActivity.this, new FindCallback() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        if (jsonArray.length() == 1) {
                            Toast.makeText(DrawerActivity.this, "登陆成功!", Toast.LENGTH_SHORT).show();
                            login(email.getText().toString());
                        } else {
                            Toast.makeText(DrawerActivity.this, "邮箱或密码错误!", Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(DrawerActivity.this, "登录失败! 错误码:" + i + s, Toast.LENGTH_LONG).show();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Button loginSignupSwitch = (Button) view.findViewById(R.id.dialog_login_signup);
        loginSignupSwitch.setText("没有账号? 点此注册!");
        loginSignupSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignupDialog();
            }
        });

        loginDialog = builder.create();
    }

    private void buildSignupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_login, null);
        builder.setView(view);

        final EditText email = (EditText) view.findViewById(R.id.dialog_login_email);
        final EditText pass = (EditText) view.findViewById(R.id.dialog_login_pass);

        builder.setTitle("请注册");
        builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                final User user = new User();
                user.setEmail(email.getText().toString());
                user.setPass(pass.getText().toString());
                //不需要开新线程, 这个过程已经被包装了.
                user.save(DrawerActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DrawerActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                        login(email.getText().toString());
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(DrawerActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Button loginSignupSwitch = (Button) view.findViewById(R.id.dialog_login_signup);
        loginSignupSwitch.setText("已有账号? 点此登陆!");
        loginSignupSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        signupDialog = builder.create();
    }

    private void login(String userName) {
        headerText.setText(userName);
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_user", userName);
        editor.apply();
    }

    private void initUser() {
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        String user = preferences.getString("current_user", "");
        if (!user.equals("")) {
            headerText.setText(user);
        }
    }
}
