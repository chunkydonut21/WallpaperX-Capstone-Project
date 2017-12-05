package com.example.shivammaheshwari.wallpaperx.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.example.shivammaheshwari.wallpaperx.data.WallpaperContract.ImagesContract.TABLE_NAME;


public class WallpaperProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;


    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        //this method will be called first when activity is launched
        sUriMatcher.addURI(WallpaperContract.CONTENT_AUTHORITY, WallpaperContract.PATH_IMAGES, MOVIES);
        sUriMatcher.addURI(WallpaperContract.CONTENT_AUTHORITY, WallpaperContract.PATH_IMAGES + "/#", MOVIES_WITH_ID);

    }

    private WallpaperDbHelper mTaskDbHelper;

    @Override
    public boolean onCreate() {

        mTaskDbHelper = new WallpaperDbHelper(getContext());
        return true;
    }

    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(WallpaperContract.ImagesContract.CONTENT_URI, id);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIES_WITH_ID:

                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedUtcDateString};

                retCursor = mTaskDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        WallpaperContract.ImagesContract.TABLE_NAME,
                        projection,
                        WallpaperContract.ImagesContract.IMAGE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }


        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }


    // Implement delete to delete a single row of data
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {

            case MOVIES:
                tasksDeleted = db.delete(
                        WallpaperContract.ImagesContract.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return tasksDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksUpdated;

        switch (match) {

            case MOVIES:
                tasksUpdated = db.update(
                        WallpaperContract.ImagesContract.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;

            case MOVIES_WITH_ID:
                //update a single task by getting the id
                String id = uri.getPathSegments().get(1);
                //using selections
                tasksUpdated = mTaskDbHelper.getWritableDatabase().update(TABLE_NAME, values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            //set notifications if a task was updated
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // return number of tasks updated
        return tasksUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                // directory
                return "vnd.android.cursor.dir" + "/" + WallpaperContract.CONTENT_AUTHORITY + "/" + WallpaperContract.PATH_IMAGES;
            case MOVIES_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + WallpaperContract.CONTENT_AUTHORITY + "/" + WallpaperContract.PATH_IMAGES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }
}
