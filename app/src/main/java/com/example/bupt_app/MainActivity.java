package com.example.bupt_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/*
    主界面就是ViewPager2
 */
public class MainActivity extends AppCompatActivity{

    private ViewPager2 pager2;
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragments;
    private MyFragmentAdapter adapter;
//    private List<VideoBean> videos; //消息列表
//    private String baseUrl = "https://beiyou.bytedance.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bnv);

        //监听事件，底部按钮和pager同步
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.item_home:
                            pager2.setCurrentItem(0);
                            break;
                        case R.id.item_add:
                            pager2.setCurrentItem(1);
                            break;
                        case R.id.item_my:
                            pager2.setCurrentItem(2);
                            break;
                    }
                    return true;     //返回值决定是否被选中
            }
        });

        fragments = new ArrayList<Fragment>();
        fragments.add(new ViewPagerFragment());
        fragments.add(new AddFragment());
        fragments.add(new MyInfoFragment());

        pager2 = findViewById(R.id.vp2);
        adapter = new MyFragmentAdapter(this);
        pager2.setAdapter(adapter);

        //监听事件，底部按钮和pager同步
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                Log.d("XXL","onPageSelected "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

//        pager2.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//            @Override
//            public void onViewAttachedToWindow(View v) {
//                if(v.getId() == R.id.me)
//                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
//                else if(v.getId() == R.id.pager2)
//                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
//            }
//            @Override
//            public void onViewDetachedFromWindow(View v) {
//
//            }
//        });
        /*
        获取数据源
         */

//        Log.d("XXL", "retrofit in");
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Log.d("XXL", "retrofit out");
//        ApiService apiService = retrofit.create(ApiService.class);
//        apiService.getArticles().enqueue(new Callback<List<VideoBean>>() {
//            @Override
//            public void onResponse(Call<List<VideoBean>> call, Response<List<VideoBean>> response) {
//                if(response.body()!=null){
//                    videos = response.body(); //设置数据
//                    Log.d("XXL", "success "+videos.size());
//                    //网络请求是异步的，通知信息实现同步
//                    notifyDataReceive();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<VideoBean>> call, Throwable t) {
//                Log.d("retrofit", "response error"+t.getMessage());
//            }
//        });

    }

//    private void notifyDataReceive() {
//        pager2 = findViewById(R.id.vp2);
//        //设置pager2的滑动方向未垂直方向
//        pager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
//        adapter = new MyFragmentAdapter(this);
//        pager2.setAdapter(adapter);
//    }

//    private class MyFragmentAdapter extends FragmentStateAdapter{
//
//        public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
//            super(fragmentActivity);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            return VideoFragment.newInstance(videos.get(position), position);
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return videos.size();
//        }
//
//    }


    /*
    =========================================================
     */
    private class MyFragmentAdapter extends FragmentStateAdapter{

        public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }


        @Override
        public int getItemCount() {
            return fragments.size();
        }

    }
}
