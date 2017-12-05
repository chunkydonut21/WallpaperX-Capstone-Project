package com.example.shivammaheshwari.wallpaperx.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shivammaheshwari.wallpaperx.R;
import com.example.shivammaheshwari.wallpaperx.fragments.DrawerFragment;
import com.example.shivammaheshwari.wallpaperx.fragments.FavouritesFragment;
import com.example.shivammaheshwari.wallpaperx.fragments.MainActivityFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private MenuItem searchMenuItem;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static final int RC_SIGN_IN = 1;
    private String yourQuery, displaying, saving;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mFirebaseAuth = FirebaseAuth.getInstance();


        //this listener is triggered whenever user authentication state changes like signing in or logging out
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    View hView = navigationView.getHeaderView(0);
                    TextView nav_user = hView.findViewById(R.id.userNameSetText);
                    nav_user.setText(user.getDisplayName());

                    TextView nav_email = hView.findViewById(R.id.userMailSetText);
                    nav_email.setText(user.getEmail());

                    ImageView nav_image = hView.findViewById(R.id.userImageSet);
                    Picasso.with(getApplicationContext())
                            .load(user.getPhotoUrl())
                            .into(nav_image);

                } else {
                    // User is signed out


                    FragmentManager fragmentManager = getSupportFragmentManager();
                    MainActivityFragment mainActivityFragment = new MainActivityFragment();

                    fragmentManager.beginTransaction()
                            .remove(mainActivityFragment)
                            .commit();

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                    );

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MainActivityFragment mainActivityFragment = new MainActivityFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.containerFragment, mainActivityFragment)
                    .commit();

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.newest) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleWalls", "newest");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment).commit();


        } else if (id == R.id.most_popular) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleWalls", "highest_rated");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment).commit();

        } else if (id == R.id.favouritesWalls) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FavouritesFragment favouritesFragment = new FavouritesFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, favouritesFragment)
                    .commit();

        } else if (id == R.id.abstractWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "1");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();


        } else if (id == R.id.animalWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "2");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();

        } else if (id == R.id.animeWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "3");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.celebWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "7");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.fantasyWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "11");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.foodWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "12");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.gameWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "14");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.movieWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "20");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.musicWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "22");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.patternWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "23");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.photoWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "24");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.sciFiWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "27");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.sportsWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "28");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.techWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "30");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.videoGameWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "32");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.womenWall) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            DrawerFragment drawerFragment = new DrawerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("BundleCategory", "33");
            drawerFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.containerFragment, drawerFragment)
                    .commit();
        } else if (id == R.id.logOut) {

            mFirebaseAuth.signOut();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(saving, yourQuery);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        displaying = savedInstanceState.getString(saving);
        yourQuery = displaying;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        yourQuery = query.toLowerCase().trim();
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("IntentSearch", yourQuery);
        startActivity(intent);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;

    }
}
