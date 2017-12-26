package com.example.nizam.bakingapp;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class BakingSteps {
    private String bakingId;
    private String stepId;
    private String shortDesc;
    private String desc;
    private String videoUrl;
    private String thumbnailUrl;

    public String getBakingId() {
        return bakingId;
    }

    public void setBakingId(String bakingId) {
        this.bakingId = bakingId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
