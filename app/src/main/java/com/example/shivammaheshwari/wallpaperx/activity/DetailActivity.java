package com.example.shivammaheshwari.wallpaperx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shivammaheshwari.wallpaperx.R;
import com.example.shivammaheshwari.wallpaperx.fragments.DetailFragment;
import com.example.shivammaheshwari.wallpaperx.model.Walls;

public class DetailActivity extends AppCompatActivity {
    String pageShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Walls walls = getIntent().getParcelableExtra("ParcelWallpapers");
        pageShare = walls.getImagePageUrl();

        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailFragment detailFragment = new DetailFragment();

        fragmentManager.beginTransaction()
                .add(R.id.details, detailFragment)
                .commit();

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (id) {

            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;

            case R.id.share_trailer:
                ShareActionProvider shareActionProvider = new ShareActionProvider(this);
                shareActionProvider.setShareIntent(getShareIntent());
                MenuItemCompat.setActionProvider(item, shareActionProvider);

        }


        return super.onOptionsItemSelected(item);

    }

    public Intent getShareIntent() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this Wallpaper!");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, pageShare);
        return intent;

    }
}