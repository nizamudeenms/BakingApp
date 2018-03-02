package com.example.nizam.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class BakingSteps implements Parcelable{
    private String bakingId;
    private String stepId;
    private String shortDesc;
    private String desc;
    private String videoUrl;
    private String thumbnailUrl;

    public BakingSteps() {
    }

    protected BakingSteps(Parcel in) {
        bakingId = in.readString();
        stepId = in.readString();
        shortDesc = in.readString();
        desc = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<BakingSteps> CREATOR = new Creator<BakingSteps>() {
        @Override
        public BakingSteps createFromParcel(Parcel in) {
            return new BakingSteps(in);
        }

        @Override
        public BakingSteps[] newArray(int size) {
            return new BakingSteps[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            if (bakingId.isEmpty()) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeString(bakingId);
            }
            dest.writeString(shortDesc);
            dest.writeString(desc);
            dest.writeString(videoUrl);
            dest.writeString(thumbnailUrl);
    }
}
