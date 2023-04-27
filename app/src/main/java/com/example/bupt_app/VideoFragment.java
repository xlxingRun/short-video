package com.example.bupt_app;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bupt_app.tool.VideoBean;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class VideoFragment extends Fragment implements View.OnClickListener{
    private static String key = "XXL";
    private static String POS = "POS";
    private static String TAG = "XXL";
    private VideoBean video;
    private MediaPlayer player;
    private SurfaceView view;
    private ImageButton pause_btn;
    private SeekBar bar;
    private int curr_pos;
    private Timer timer;
    private TimerTask task;
    private boolean isPrepared = false;    //视频资源是否加载完成
    private boolean flag = false;      //设置flag，当前主页

    public static VideoFragment newInstance(VideoBean videoBean, int pos){
        VideoFragment videoFragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putSerializable(key, videoBean);
        args.putInt(POS, pos);
        videoFragment.setArguments(args);
        return videoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container, false);
        Bundle args = getArguments();
        if (args != null) {
            video = (VideoBean) args.getSerializable(key);
            curr_pos = (int) args.get(POS);

            TextView tv = v.findViewById(R.id.video_name);
            tv.setText(video.getNickName());
            TextView des = v.findViewById(R.id.description);
            des.setText(video.getDescription());
//            TextView like = v.findViewById(R.id.like_num);
//            like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "点赞维护中", Toast.LENGTH_SHORT).show();
//                }
//            });

//            Log.d(TAG, "glide in");
//            ImageView image = v.findViewById(R.id.Glide_image);
//            Glide.with(getActivity())
//                    .load(Uri.parse(video.getVideoUrl().replace("http://","https://")))
//                    .into(image);
//            image.setVisibility(View.INVISIBLE);

            pause_btn = v.findViewById(R.id.video_pause);
            pause_btn.setVisibility(View.INVISIBLE);
            pause_btn.setOnClickListener(this);
            playVideo(v);
            addTimer(v);
        }
        return v;
    }

    /*
    进度条跟进
    * */
    private void addTimer(View v) {
        bar = v.findViewById(R.id.percent);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
            }
        });
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if(isPrepared && player.isPlaying())
                    bar.setProgress(player.getCurrentPosition());
//                Log.d(TAG, "task "+ curr_pos + " " + player.getCurrentPosition());
            }
        };
        timer.schedule(task, 0,500);
    }


    private void playVideo(View v) {
        player = new MediaPlayer();
        view = v.findViewById(R.id.media_view);
        view.setOnClickListener(this);
        SurfaceHolder h = view.getHolder();

        h.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                player.setDisplay(h); //设置播放的画布
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                holder.removeCallback(this);
            }
        });

        Context context = getActivity();
        try {
            assert context != null;
//            player.setDataSource(context, Uri.parse(video.getVideoUrl().replace("http://","https://")));
            // TODO 设置数据源
            player.setDataSource(context, Uri.parse(video.getVideoUrl()));
            player.prepareAsync(); //异步prepare资源
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "prepared " + curr_pos);
                bar.setMax(mp.getDuration());
                mp.setLooping(true);
                isPrepared = true;          //资源加载完毕
                if(flag)
                    mp.start();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(isPrepared) {
            if (player.isPlaying())
                player.stop();
            player.release();
            player = null;
        }
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        Log.d("XXL", curr_pos + " onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, curr_pos + " onStart");
//        player.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, curr_pos + " onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = true;
        if(isPrepared && !player.isPlaying()){
            player.start();
            pause_btn.setVisibility(View.INVISIBLE);
        }
        Log.d(TAG, curr_pos + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isPrepared && player.isPlaying()){
            player.pause();
            pause_btn.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, curr_pos + " onPause");
    }


    /*
    暂停和继续播放
    * */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.video_pause || v.getId() == R.id.media_view) {
            if (player.isPlaying()) {
                pause_btn.setVisibility(View.VISIBLE);
                player.pause();
            } else {
                pause_btn.setVisibility(View.INVISIBLE);
                player.start();
            }
        }
    }
}
