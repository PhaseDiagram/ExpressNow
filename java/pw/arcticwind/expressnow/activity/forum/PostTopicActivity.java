package pw.arcticwind.expressnow.activity.forum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;
import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.model.bmob.Post;
import pw.arcticwind.expressnow.model.bmob.Topic;

//社区, 发表主题
public class PostTopicActivity extends AppCompatActivity {

    private EditText body;
    private EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        body = (EditText) findViewById(R.id.forum_post_topic_body);
        title = (EditText) findViewById(R.id.forum_post_topic_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_topic:
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
        final Topic topic = new Topic();
        final Post post = new Post();
        topic.setTitle(title.getText().toString());
        post.setBody(body.getText().toString());
        topic.setGood(false);

        topic.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                post.setParent(topic.getObjectId());
                post.save(PostTopicActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(PostTopicActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(PostTopicActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
