package pw.arcticwind.expressnow.activity.forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.model.DividerItemDecoration;
import pw.arcticwind.expressnow.model.bmob.Post;
import pw.arcticwind.expressnow.model.bmob.PostAdapter;
import pw.arcticwind.expressnow.model.bmob.Topic;
import pw.arcticwind.expressnow.utils.ParseJSON;

//社区, 帖子详情页
//带 header 的 RecyclerView
public class ForumDetailActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private PostAdapter adapter = new PostAdapter();
    private List<Post> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private String tid;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListFromServer();
    }

    private void initView() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_forum_post);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListFromServer();
            }
        });

        tid = getIntent().getStringExtra("tid");
        title = getIntent().getStringExtra("title");
        setTitle(title);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.list_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void fillListFromServer() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        BmobQuery<Post> query = new BmobQuery<>("Post");
        query.addWhereEqualTo("parent", tid);
        query.findObjects(this, new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                list = ParseJSON.parsePosts(jsonArray);
                adapter.addData(list);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(ForumDetailActivity.this, "获取列表失败!", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void updateListFromServer() {
        fillListFromServer();
    }

    private void post() {
        Intent intent = new Intent(this, PostBodyActivity.class);
        intent.putExtra("tid", tid);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
