package com.example.bupt_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bupt_app.tool.ApiService;
import com.example.bupt_app.tool.VideoBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewPagerFragment extends Fragment {

    private View main_view;
    private List<VideoBean> videos; //消息列表
//    private String baseUrl = "https://beiyou.bytedance.com/";

    // TODO 更改BASE_URL
    private String baseUrl = "https://77d491bd-690d-47e6-a878-ea133f3c4446.mock.pstmn.io/";
    private ViewPager2 pager2;
    private MyFragmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        getDataFromNet();
        return main_view;
    }

    private void getDataFromNet() {
        /*
        获取数据源
         */
        Log.d("XXL", "retrofit in");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("XXL", "retrofit out");
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getArticles().enqueue(new Callback<List<VideoBean>>() {
            @Override
            public void onResponse(Call<List<VideoBean>> call, Response<List<VideoBean>> response) {
                if(response.body()!=null){
                    videos = response.body(); //设置数据
                    Log.d("XXL", "success "+videos.size());
                    //网络请求是异步的，通知信息实现同步
                    notifyDataReceive();
                } else {
                    Log.d("XXL", call.toString());
                    Log.d("XXL", response.toString());
                    Log.d("XXL", "没有收到数据");
                }
            }

            @Override
            public void onFailure(Call<List<VideoBean>> call, Throwable t) {
                Log.d("retrofit", "response error "+t.getMessage());
            }
        });
    }

    private void notifyDataReceive() {
        pager2 = main_view.findViewById(R.id.pager2);
        //设置pager2的滑动方向未垂直方向
        pager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        // TODO 设置视频缓存
        pager2.setOffscreenPageLimit(1);
        adapter = new MyFragmentAdapter(this);
        pager2.setAdapter(adapter);
    }

    private class MyFragmentAdapter extends FragmentStateAdapter {

        public MyFragmentAdapter(@NonNull ViewPagerFragment fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return VideoFragment.newInstance(videos.get(position), position);
        }


        @Override
        public int getItemCount() {
            return videos.size();
        }

    }
}
