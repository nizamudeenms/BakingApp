package com.example.nizam.bakingapp;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class BakingSteps {
    private String stepId;
    private String shortDesc;
    private String desc;
    private String videoUrl;
    private String thumbnailUrl;

    public String getId() {
        return stepId;
    }

    public void setId(String id) {
        this.stepId = id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumnailUrl() {
        return thumbnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        this.thumbnailUrl = thumnailUrl;
    }
}
