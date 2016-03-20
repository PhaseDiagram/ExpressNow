package pw.arcticwind.expressnow.activity.forum;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pw.arcticwind.expressnow.R;

//社区, 公告页
//TabLayout的一部分, 这里只是示例界面
public class ForumGoodFragment extends Fragment {


    public ForumGoodFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forum_good, container, false);
    }

}
