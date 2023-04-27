package com.example.bupt_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class AddFragment extends Fragment {
    private View main_view;
    private static String TAG = "XXL";
    private String[] permissionsArray = new String[]{
            Manifest.permission.CAMERA, //相机权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //写外部文件
            Manifest.permission.READ_EXTERNAL_STORAGE, //读外部文件
            Manifest.permission.RECORD_AUDIO //音频录制
    };
    private int REQUEST_CODE = 277;
    private int REQUEST_CAMERA = 305;
    private File videoPath;
    private Boolean permissionOK = false;
    private LinearLayout camera;
    private LinearLayout upload;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_add, container, false);

        camera = main_view.findViewById(R.id.shoot_layout);
        upload = main_view.findViewById(R.id.local_layout);

        //1、第一件事获取或者验证用户本地文件读写权限和摄像头权限
        if(!checkPermissionAllGranted(permissionsArray)){
            requestPermissions(permissionsArray, REQUEST_CODE);
        }else{
            permissionOK = true;
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permissionOK)
                    openSystemCamera();
                else {
                    Toast.makeText(getActivity(), "请您先授权",Toast.LENGTH_SHORT).show();
                    requestPermissions(permissionsArray, REQUEST_CODE);
                    if(permissionOK)
                        openSystemCamera();
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "系统正在维护，敬请期待",Toast.LENGTH_SHORT).show();
            }
        });

        return main_view;
    }


    /*
    打开系统相机
     */
    private void openSystemCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        String videoName = String.format(Locale.getDefault(),"video_%d.mp4", System.currentTimeMillis());
        videoPath = new File(getActivity().getExternalCacheDir(), videoName);

        Uri outUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", videoPath);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA); //跳转相机拍摄activity
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            Log.d(TAG, "权限申请回调");
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PERMISSION_DENIED)
                    return;
            }
            permissionOK = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "拍摄完毕，返回AddFragment");
    }

}
