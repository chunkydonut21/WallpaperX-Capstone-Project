package com.example.shivammaheshwari.wallpaperx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.shivammaheshwari.wallpaperx.data.WallpaperContract;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProviderTest {
    private Context mContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void insertPostTest() {
        ContentValues values = new ContentValues();
        values.put(WallpaperContract.ImagesContract.IMAGE_ID, "1");
        values.put(WallpaperContract.ImagesContract.IMAGE_WIDTH, "1200");
        values.put(WallpaperContract.ImagesContract.IMAGE_HEIGHT, "1200");
        values.put(WallpaperContract.ImagesContract.IMAGEURL, "https://");
        values.put(WallpaperContract.ImagesContract.IMG_THUMBNAIL, "https://www.something.com");
        values.put(WallpaperContract.ImagesContract.IMG_PAGEURL, "abcde.com");


        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.getContentResolver().insert(WallpaperContract.ImagesContract.CONTENT_URI, values);

        Cursor cursor = appContext.getContentResolver().query(WallpaperContract.ImagesContract.CONTENT_URI,
                null,
                null,
                null,
                null);


        assert cursor != null;
        cursor.close();
    }

    @Test
    public void deletePostsTest() {
        ContentValues values = new ContentValues();
        values.put(WallpaperContract.ImagesContract.IMAGE_ID, "1");
        values.put(WallpaperContract.ImagesContract.IMAGE_WIDTH, "1200");
        values.put(WallpaperContract.ImagesContract.IMAGE_HEIGHT, "1200");
        values.put(WallpaperContract.ImagesContract.IMAGEURL, "https://");
        values.put(WallpaperContract.ImagesContract.IMG_THUMBNAIL, "https://www.something.com");
        values.put(WallpaperContract.ImagesContract.IMG_PAGEURL, "abcde.com");


        Context appContext = InstrumentationRegistry.getTargetContext();
        appContext.getContentResolver().insert(WallpaperContract.ImagesContract.CONTENT_URI, values);

        Cursor cursor = appContext.getContentResolver().query(WallpaperContract.ImagesContract.CONTENT_URI,
                null,
                null,
                null,
                null);

        assert cursor != null;
        cursor.close();
    }

    @Test
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                WallpaperContract.ImagesContract.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                WallpaperContract.ImagesContract.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assert cursor != null;
        cursor.close();
    }


}