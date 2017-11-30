package com.studio.plaster.tweetporter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.studio.plaster.tweetporter.adapter.PostAdapter;
import com.studio.plaster.tweetporter.model.Post;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private SharedPreferences shareAuth;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList = new ArrayList<>();
    private SharedPreferences sharedSetting;
    private TextView nameHeader;
    private ImageView profileHeader;
    private ImageView bgHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        Toolbar toolbar = findViewById(R.id.toolbar_save);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Saved");
        shareAuth = getSharedPreferences("AUTHEN_PREF", Context.MODE_PRIVATE);
        sharedSetting = getSharedPreferences("Settingkeeper", Context.MODE_PRIVATE);
        navigationView = findViewById(R.id.navigation_save);
        mDrawerLayout = findViewById(R.id.drawer_layout_save);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id) {
                    case R.id.logoutnav_button:
                        logout();
                        return true;
                    case R.id.home_button:
                        finish();
                        return true;
                    case R.id.saved_button:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.postRecycler_save);
        postAdapter = new PostAdapter(this);
        getSaved();
        postAdapter.setSaveMode(true);
        postAdapter.setPostList(postList);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nameHeader = navigationView.getHeaderView(0).findViewById(R.id.nameHeader);
        profileHeader = navigationView.getHeaderView(0).findViewById(R.id.imgProfileHeader);
        bgHeader = navigationView.getHeaderView(0).findViewById(R.id.backgroundHeader);
        String name = sharedSetting.getString("screenName", null);
        String profileImg = sharedSetting.getString("profileImg", null);
        String backGround = sharedSetting.getString("backGround", null);
        nameHeader.setText(name);
        Glide.with(this).load(profileImg).into(profileHeader);
        Glide.with(this).load(backGround).into(bgHeader);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        mDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }


    public void logout(){
        SharedPreferences.Editor editor = shareAuth.edit();
        editor.remove("HAS_LOGIN");
        editor.commit();
        Intent intentLogout = new Intent(this, SplashActivity.class);
        startActivity(intentLogout);
    }

    public void getSaved(){
        SharedPreferences sharedPref = getSharedPreferences("Savedkeeper", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String savedPostList = sharedPref.getString("savedpostlist", null);
        if(savedPostList != null){
            Type type = new TypeToken<List<Post>>(){}.getType();
            postList = gson.fromJson(savedPostList, type);
        }
    }



}
