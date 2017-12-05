package com.example.shivammaheshwari.wallpaperx.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class WallpaperContract {

    public static final String CONTENT_AUTHORITY = "com.example.shivammaheshwari.wallpaperx";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_IMAGES = "images";

    public static final class ImagesContract implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_IMAGES).build();

        public static final String TABLE_NAME = "images";
        public static final String IMAGE_ID = "id";
        public static final String IMAGE_WIDTH = "imgWidth";
        public static final String IMAGE_HEIGHT = "imgHeight";
        public static final String IMAGEURL = "imgUrl";
        public static final String IMG_THUMBNAIL = "full";
        public static final String IMG_PAGEURL = "regular";


    }

}
