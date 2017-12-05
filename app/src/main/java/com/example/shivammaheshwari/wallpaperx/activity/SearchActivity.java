package com.example.shivammaheshwari.wallpaperx.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.shivammaheshwari.wallpaperx.R;
import com.example.shivammaheshwari.wallpaperx.fragments.DrawerFragment;

public class SearchActivity extends AppCompatActivity {

    String saving;
    String displaying;
    String getQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getQuery = getIntent().getStringExtra("IntentSearch");

        FragmentManager fragmentManager = getSupportFragmentManager();
        DrawerFragment drawerFragment = new DrawerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("BundleSearch", getQuery);
        drawerFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.frameSearch, drawerFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
