package pw.arcticwind.expressnow.activity.startup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import pw.arcticwind.expressnow.R;
import pw.arcticwind.expressnow.activity.DrawerActivity;

//第一次访问时的导航页面
//ViewPager
public class FirstVisitActivity extends Activity {

    private ViewPager viewPager;
    private View view1, view2, view3;
    private List<View> viewList = new ArrayList<>();
    private ImageView page1, page2, page3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_visit);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.activity_first_visit_1, null);
        view2 = inflater.inflate(R.layout.activity_first_visit_2, null);
        view3 = inflater.inflate(R.layout.activity_first_visit_3, null);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        Button startButton = (Button) view3.findViewById(R.id.start_btn);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.clearOnPageChangeListeners();
                Intent intent = new Intent(FirstVisitActivity.this, DrawerActivity.class);
                startActivity(intent);
                SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
                preferences.edit().putBoolean("first", false).apply();
                finish();
            }
        });

        //points
        page1 = (ImageView) findViewById(R.id.page_1);
        page2 = (ImageView) findViewById(R.id.page_2);
        page3 = (ImageView) findViewById(R.id.page_3);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            switch (position) {
                case 0:
                    page1.setImageDrawable(getDrawable(R.drawable.page_indicator_focused));
                    page2.setImageDrawable(getDrawable(R.drawable.page_indicator_unfocused));
                    break;
                case 1:
                    page1.setImageDrawable(getDrawable(R.drawable.page_indicator_unfocused));
                    page2.setImageDrawable(getDrawable(R.drawable.page_indicator_focused));
                    page3.setImageDrawable(getDrawable(R.drawable.page_indicator_unfocused));
                    break;
                case 2:
                    page2.setImageDrawable(getDrawable(R.drawable.page_indicator_unfocused));
                    page3.setImageDrawable(getDrawable(R.drawable.page_indicator_focused));
                    break;
            }
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
