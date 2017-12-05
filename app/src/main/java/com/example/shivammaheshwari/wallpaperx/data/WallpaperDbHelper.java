package com.example.shivammaheshwari.wallpaperx.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shivammaheshwari.wallpaperx.data.WallpaperContract.ImagesContract;

public class WallpaperDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "wallpaperCollection.db";

    public WallpaperDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + ImagesContract.TABLE_NAME + " (" +
                ImagesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ImagesContract.IMAGE_ID + " TEXT UNIQUE NOT NULL," +
                ImagesContract.IMAGE_WIDTH + " TEXT NOT NULL," +
                ImagesContract.IMAGE_HEIGHT + " TEXT NOT NULL," +
                ImagesContract.IMAGEURL + " TEXT NOT NULL," +
                ImagesContract.IMG_THUMBNAIL + " TEXT NOT NULL," +
                ImagesContract.IMG_PAGEURL + " TEXT NOT NULL," +
                "UNIQUE (" + ImagesContract.IMAGE_ID + ") ON CONFLICT IGNORE" +
                " );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ImagesContract.TABLE_NAME);
        onCreate(db);
    }
}
