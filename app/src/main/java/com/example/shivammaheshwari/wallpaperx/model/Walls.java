package com.example.shivammaheshwari.wallpaperx.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Walls implements Parcelable {


    private String mId;
    private String mWidth;
    private String mHeight;
    private String mImageUrl;
    private String mThumbnailUrl;
    private String mImagePageUrl;


    public Walls(String mId, String mWidth, String mHeight, String mImageUrl, String mThumbnailUrl, String mImagePageUrl) {
        this.mId = mId;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mImageUrl = mImageUrl;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mImagePageUrl = mImagePageUrl;

    }

    protected Walls(Parcel in) {
        mId = in.readString();
        mWidth = in.readString();
        mHeight = in.readString();
        mImageUrl = in.readString();
        mThumbnailUrl = in.readString();
        mImagePageUrl = in.readString();
    }

    public static final Creator<Walls> CREATOR = new Creator<Walls>() {
        @Override
        public Walls createFromParcel(Parcel in) {
            return new Walls(in);
        }

        @Override
        public Walls[] newArray(int size) {
            return new Walls[size];
        }
    };

    public String getmId() {
        return mId;
    }

    public String getWidth() {
        return mWidth;
    }

    public String getHeight() {
        return mHeight;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public String getImagePageUrl() {
        return mImagePageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mWidth);
        dest.writeString(mHeight);
        dest.writeString(mImageUrl);
        dest.writeString(mThumbnailUrl);
        dest.writeString(mImagePageUrl);

    }
}
