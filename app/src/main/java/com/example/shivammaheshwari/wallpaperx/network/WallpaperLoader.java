package com.example.shivammaheshwari.wallpaperx.network;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.shivammaheshwari.wallpaperx.model.Walls;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class WallpaperLoader extends AsyncTaskLoader<List<Walls>> {

    private final String mUrl;

    public WallpaperLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Walls> loadInBackground() {
        List<Walls> walls = null;

        if (mUrl == null) {
            return null;
        }
        try {
            walls = Utils.fetchData(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return walls;
    }
}
