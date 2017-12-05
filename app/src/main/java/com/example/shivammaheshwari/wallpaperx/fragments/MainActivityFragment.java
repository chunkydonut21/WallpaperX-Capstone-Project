package com.example.shivammaheshwari.wallpaperx.fragments;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivammaheshwari.wallpaperx.BuildConfig;
import com.example.shivammaheshwari.wallpaperx.R;
import com.example.shivammaheshwari.wallpaperx.adapters.ImageAdapter;
import com.example.shivammaheshwari.wallpaperx.model.Walls;
import com.example.shivammaheshwari.wallpaperx.network.WallpaperLoader;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Walls>> {


    private ImageAdapter imageAdapter;
    private static final String TAG = "MainActivity";
    private String pageNumber = "1";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.loading_indicator)
    View loadingIndicator;
    @BindView(R.id.floating_menuMain)
    FloatingActionMenu materialDesignMain;
    @BindView(R.id.floating_next)
    FloatingActionButton floatingNext;
    @BindView(R.id.floating_previous)
    FloatingActionButton floatingPrevious;

    public MainActivityFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        AdView mAdView = view.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        floatingNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int number = Integer.parseInt(pageNumber);
                if (number > 30) {
                    Toast.makeText(getContext(), R.string.reachedEnd, Toast.LENGTH_SHORT).show();
                } else {
                    number++;
                    pageNumber = String.valueOf(number);
                    getActivity().getLoaderManager().restartLoader(1, null, MainActivityFragment.this);

                }

            }
        });


        floatingPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int number = Integer.parseInt(pageNumber);
                if (number == 1) {
                    Toast.makeText(getContext(), R.string.nothingMore, Toast.LENGTH_SHORT).show();
                } else {
                    number--;
                    pageNumber = String.valueOf(number);
                    getActivity().getLoaderManager().restartLoader(1, null, MainActivityFragment.this);
                }

            }
        });


        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mRecyclerView.setHasFixedSize(true);
        imageAdapter = new ImageAdapter(getContext(), new ArrayList<Walls>());
        mRecyclerView.setAdapter(imageAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);

        //check for connection
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getActivity().getLoaderManager().initLoader(1, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText(R.string.no_connection);

        }

    }

    @Override
    public Loader<List<Walls>> onCreateLoader(int id, Bundle args) {

        String wallpapersUrl = "https://wall.alphacoders.com/api2.0/get.php?auth=" + BuildConfig.MY_API_KEY + "&method=by_views";
        Uri baseUri = Uri.parse(wallpapersUrl);
        Uri.Builder ub = baseUri.buildUpon();
        ub.appendQueryParameter("page", pageNumber);
        Log.e("pageUrl", ub.toString());

        return new WallpaperLoader(getContext(), ub.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Walls>> loader, List<Walls> data) {

        loadingIndicator.setVisibility(View.GONE);

        if (data != null && !data.isEmpty()) {
            imageAdapter.setWallpaper(data);
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setText(R.string.nothing_found);
            emptyView.setVisibility(View.VISIBLE);

            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Walls>> loader) {
        imageAdapter.setWallpaper(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getLoaderManager().restartLoader(1, null, this);
    }
}
