package com.example.bupt_app.tool;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoBean implements Serializable {
    @SerializedName("feedurl")
    private String videoUrl;      //mp4的url
    @SerializedName("nickname")
    private String nickName;      //视频名称
    @SerializedName("description")
    private String description;   //描述
    @SerializedName("likecount")
    private int likeCount;        //点赞数
    @SerializedName("avatar")
    private String avatar;        //封面图片url

    public VideoBean(String videoUrl, String nickName, String description, int likeCount, String avatar) {
        this.videoUrl = videoUrl;
        this.nickName = nickName;
        this.description = description;
        this.likeCount = likeCount;
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDescription() {
        return description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "videoUrl='" + videoUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", description='" + description + '\'' +
                ", likeCount=" + likeCount +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
