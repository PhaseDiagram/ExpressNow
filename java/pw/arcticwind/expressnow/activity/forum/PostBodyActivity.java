package pw.arcticwind.expressnow.activity.forum;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;
import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.model.bmob.Post;
import pw.arcticwind.expressnow.model.bmob.Topic;

//社区, 发表回复
//暂时没有做用户验证, 后台没写API
public class PostBodyActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_body);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = (EditText) findViewById(R.id.forum_post_body);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_body:
                postToServer();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postToServer() {
        final Post post = new Post();
        post.setBody(editText.getText().toString());
        post.setParent(getIntent().getStringExtra("tid"));

        post.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(PostBodyActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
