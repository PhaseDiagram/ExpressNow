package pw.arcticwind.expressnow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.model.FavorCell;
import pw.arcticwind.expressnow.model.FavorCellAdapter;
import pw.arcticwind.expressnow.utils.ParseJSON;

//历史页, 收藏页的父类, 从文件获取数据, 并按时间排序.
//RecyclerView + CardVIew
abstract class FavorCellFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<FavorCell> list;
    private FavorCellAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void initView(View view) {
        setTitle();
        recyclerView = (RecyclerView) view.findViewById(R.id.list_favor);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        fillList();
        adapter = new FavorCellAdapter();
        adapter.addData(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FavorCellAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, FavorCell favorCell) {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("expressCom", favorCell.getComPinyin());
                intent.putExtra("num", favorCell.getNum());
                startActivity(intent);
            }
        });
    }

    abstract void setTitle();


    private void updateView() {
        list.clear();
        fillList();
        adapter.modData(list);
    }

    private void fillList() {
        list = new ArrayList<>();
        File file = getDir();
        List fileListTemp = Arrays.asList(file.listFiles());
        Collections.sort(fileListTemp, new FileModTimeComparator());
        File[] files = (File[]) fileListTemp.toArray();
        for (File fileTemp : files) {
            list.add(ParseJSON.parseLatest(getContext(), fileTemp));
        }
    }

    abstract File getDir();

    public class FileModTimeComparator implements Comparator<File> {
        public int compare(File file1, File file2) {
            return file1.lastModified() > file2.lastModified() ? -1 : 1;
        }
    }
}
