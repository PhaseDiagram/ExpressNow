package pw.arcticwind.expressnow.activity.forum;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.widget.DividerItemDecoration;
import pw.arcticwind.expressnow.model.bmob.Topic;
import pw.arcticwind.expressnow.model.bmob.TopicAdapter;
import pw.arcticwind.expressnow.utils.ParseJSON;

//社区, 通常讨论页
//TabLayout的一部分; RecyclerView
//没有做缓存处理, 缓存处理请见快递查询部分
public class ForumCommonFragment extends Fragment {

    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    private List<Topic> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;

    public ForumCommonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_forum_common, container, false);
        initView(view);
        updateListFromServer();
        return view;
    }

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_forum_common);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateListFromServer();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.list_common);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        adapter = new TopicAdapter();
        adapter.setOnItemClickListener(new TopicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Topic topic) {
                Intent intent = new Intent(getActivity(), ForumDetailActivity.class);
                intent.putExtra("tid", topic.getObjectId());
                intent.putExtra("title", topic.getTitle());
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTopic();
            }
        });
    }

    private void postTopic() {
        Intent intent = new Intent(getActivity(), PostTopicActivity.class);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            updateListFromServer();
        }
    }


    private void fillListFromServer() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        BmobQuery<Topic> query = new BmobQuery<>("Topic");
        query.addWhereEqualTo("good", false);
        query.findObjects(getActivity(), new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                list = ParseJSON.parseTopics(jsonArray);
                Collections.reverse(list);
                adapter.addData(list);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), "获取列表失败!", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void updateListFromServer() {
        fillListFromServer();
    }

}
