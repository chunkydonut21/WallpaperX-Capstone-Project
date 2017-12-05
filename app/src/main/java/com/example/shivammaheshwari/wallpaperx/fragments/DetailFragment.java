package com.example.shivammaheshwari.wallpaperx.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shivammaheshwari.wallpaperx.R;
import com.example.shivammaheshwari.wallpaperx.data.WallpaperContract;
import com.example.shivammaheshwari.wallpaperx.model.Walls;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {


    Walls walls;
    private String imageID, imgWidth, imgHeight, imgUrl, thumbUrl, pageUrl;
    ProgressDialog progressDialog;
    private static final int REQUEST_STORAGE_PERMISSION = 1;


    @BindView(R.id.favorites)
    ImageView favorite;
    @BindView(R.id.setImage)
    ImageView wallpaperSetView;
    @BindView(R.id.floating_menu)
    FloatingActionMenu materialDesignFAM;
    @BindView(R.id.floating_download)
    FloatingActionButton floatingDownload;
    @BindView(R.id.floating_wallpaper)
    FloatingActionButton floatingWallpaper;

    public DetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        AdView mAdView = view.findViewById(R.id.adViewDetail);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        walls = getActivity().getIntent().getParcelableExtra("ParcelWallpapers");

        imageID = walls.getmId();
        imgWidth = walls.getWidth();
        imgHeight = walls.getHeight();

        imgUrl = walls.getImageUrl();

        thumbUrl = walls.getThumbnailUrl();
        pageUrl = walls.getImagePageUrl();


        Picasso.with(getContext())
                .load(imgUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(wallpaperSetView);

        floatingDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Check for the external storage permission
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // If you do not have permission, request it
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_STORAGE_PERMISSION);
                } else {
                    // download the image if the permission exists
                    downloadImage();
                }

            }
        });

        floatingWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask myTask = new MyTask();
                myTask.execute(imgUrl);
            }
        });

        boolean isFav = isFavorite();

        if (isFav) {
            favorite.setImageResource(R.drawable.ic_fav);
        } else {
            favorite.setImageResource(R.drawable.ic_nofav);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean inFavorites = isFavorite();
                if (inFavorites) {
                    favorite.setImageResource(R.drawable.ic_nofav);
                    deleteFromFavorites();
                    Toast.makeText(getContext(), R.string.removedFav, Toast.LENGTH_SHORT).show();

                } else {
                    favorite.setImageResource(R.drawable.ic_fav);
                    addToFavorites();
                    Toast.makeText(getContext(), R.string.addedFav, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, download the wallpaper
                    downloadImage();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(getContext(), R.string.permissionDenied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /*
 * add wallpaper into favorites in the DB
 * */
    private void addToFavorites() {

        ContentValues values = new ContentValues();

        values.put(WallpaperContract.ImagesContract.IMAGE_ID, imageID);
        values.put(WallpaperContract.ImagesContract.IMAGE_WIDTH, imgWidth);
        values.put(WallpaperContract.ImagesContract.IMAGE_HEIGHT, imgHeight);
        values.put(WallpaperContract.ImagesContract.IMAGEURL, imgUrl);
        values.put(WallpaperContract.ImagesContract.IMG_THUMBNAIL, thumbUrl);
        values.put(WallpaperContract.ImagesContract.IMG_PAGEURL, pageUrl);

        getActivity().getContentResolver().insert(WallpaperContract.ImagesContract.CONTENT_URI, values);

    }

    /*
    * delete wallpaper from the favorites in the DB
    * */
    private void deleteFromFavorites() {

        getActivity().getContentResolver().delete(WallpaperContract.ImagesContract.CONTENT_URI, WallpaperContract.ImagesContract.IMAGE_ID + " = ? ",
                new String[]{imageID + ""});
    }

    /*
     * check if the wallpaper is already in database
     */
    public boolean isFavorite() {
        boolean favorite = false;
        Cursor cursor = getActivity().getContentResolver().query(
                WallpaperContract.ImagesContract.CONTENT_URI,
                null,
                WallpaperContract.ImagesContract.IMAGE_ID + " = " + imageID,
                null,
                null
        );
        if (cursor != null) {
            favorite = cursor.getCount() != 0;
            cursor.close();
        }
        return favorite;
    }


    public void downloadImage() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imgUrl));
        request.setDescription(getString(R.string.fileDownloadWait));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String nameOfFile = URLUtil.guessFileName(imgUrl, null, MimeTypeMap.getFileExtensionFromUrl(imgUrl));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

    private class MyTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.settingWallpaper));
            progressDialog.setMessage(getString(R.string.pleaseWait));
            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            String myUrl = strings[0];
            Bitmap result = null;
            try {

                InputStream inputStream = new URL(myUrl).openStream();
                result = BitmapFactory.decodeStream(inputStream);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);


            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            progressDialog.dismiss();

        }
    }

}

