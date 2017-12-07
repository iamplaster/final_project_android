package com.studio.plaster.tweetporter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.studio.plaster.tweetporter.adapter.PageAdapter;
import com.studio.plaster.tweetporter.model.Post;
import com.studio.plaster.tweetporter.model.TabList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mPager;
    private PageAdapter mPageAdapter;
    private TabLayout mTabLayout;
    private SharedPreferences sharedSetting;
    private GetInfoObject getInfoObject;
    private List<TabList> tabLists;
    private Boolean isDelTab = false;
    private int lastPosition;
    private TwitterAccessObject tao;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NavigationView navigationView;
    private SharedPreferences sharedSaved;
    private SharedPreferences shareAuth;
    private TextView nameHeader;
    private ImageView profileHeader;
    private ImageView bgHeader;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedSetting = getSharedPreferences("Settingkeeper", Context.MODE_PRIVATE);
        sharedSaved = getSharedPreferences("Savedkeeper", Context.MODE_PRIVATE);
        shareAuth = getSharedPreferences("AUTHEN_PREF", Context.MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mTabLayout = findViewById(R.id.tabLayout);
        navigationView = findViewById(R.id.navigation);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("AUTHEN_PREF", Context.MODE_PRIVATE);
        String TWITTER_CONSUMER_KEY = getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        String TWITTER_CONSUMER_SECRET = getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET);
        String twit_access_token = sharedPreferences.getString("PREF_KEY_OAUTH_TOKEN", "");
        String twit_access_token_secret = sharedPreferences.getString("PREF_KEY_OAUTH_SECRET", "");

        getInfoObject = new GetInfoObject();
        tao = new TwitterAccessObject();
        tao.setTWITTER_CONSUMER_KEY(TWITTER_CONSUMER_KEY);
        tao.setTWITTER_CONSUMER_SECRET(TWITTER_CONSUMER_SECRET);
        tao.setTwit_access_token(twit_access_token);
        tao.setTwit_access_token_secret(twit_access_token_secret);
        tao.setup();
        tao.setListener(this);
        tao.getHeaderInfo();



        DbCreator dbCreator = new DbCreator();
        dbCreator.setContext(this);
        dbCreator.setListener(this);
        dbCreator.execute();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id) {
                    case R.id.logoutnav_button:
                        logout();
                        return true;
                    case R.id.home_button:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.saved_button:
                        goToSavedPage();

                }
                return false;
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastPosition = mPager.getCurrentItem();
                isDelTab = true;
                getInfoObject.getTabs();
            }
        });


        mDrawerLayout = findViewById(R.id.drawer_layout);
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

        // Set the drawer toggle as the DrawerListener
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    public void loadPage(){
        getInfoObject.setListener(this);
        getInfoObject.getTabs();


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.corner_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.addtab_button:
                addTabDialog();
                return true;
            case R.id.edittab_button:
                editTabButton();
                return true;
            case R.id.deletetab_button:
                deleteTabButton();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                isDelTab = true;
                getInfoObject.getTabs();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void processFinish(TabMainDb output) {
        getInfoObject.setDb(output);
        loadPage();
    }

    @Override
    public void getFinish(List<TabList> output) {
        tabLists = output;
        tao.updateTabs(tabLists);
    }

    @Override
    public void delFinish(String result, int lastPosition) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        this.lastPosition = lastPosition;
        isDelTab = true;
        getInfoObject.getTabs();

    }

    @Override
    public void updateTabsFinish(List<TabList> tabLists1) {
        tabLists = tabLists1;
        mPager = findViewById(R.id.pager);
        mPageAdapter = new PageAdapter(getSupportFragmentManager());
        mPageAdapter.setTabLists(tabLists);
        mPager.setAdapter(mPageAdapter);
        mTabLayout.setupWithViewPager(mPager);
        if(isDelTab){
            if(tabLists.size() == lastPosition){
                lastPosition--;
            }
            mTabLayout.getTabAt(lastPosition).select();
            isDelTab = false;
        }
        swipeRefreshLayout.setRefreshing(false);
        if(tabLists.size() == 0){
            addTab("Home");
        }

    }

    @Override
    public void getHeaderInfoFinish(List<String> output) {
        SharedPreferences.Editor editor =  sharedSetting.edit();
        if(output.size() == 3){
            editor.putString("screenName", output.get(0));
            editor.putString("profileImg", output.get(1));
            editor.putString("backGround", output.get(2));
            editor.apply();
            nameHeader = findViewById(R.id.nameHeader);
            profileHeader = findViewById(R.id.imgProfileHeader);
            bgHeader = findViewById(R.id.backgroundHeader);
            nameHeader.setText(output.get(0));
            Glide.with(this).load(output.get(1)).into(profileHeader);
            Glide.with(this).load(output.get(2)).into(bgHeader);

        }

    }


    public void addTabDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View prompt = layoutInflater.inflate(R.layout.add_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(prompt);

        final EditText tabName = prompt.findViewById(R.id.setTabName);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addTab(tabName.getText().toString());
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void addTab(String name){
        TabList preTabList = new TabList();
        preTabList.setName(name);
        preTabList.setNotify(false);
        preTabList.setLocation(0);
        List<Post> pl = new ArrayList<>();
        List<String> kwl = new ArrayList<>();
        preTabList.setPostList(pl);
        preTabList.setKeywordList(kwl);
        getInfoObject.addTab(preTabList);
        isDelTab = true;
        lastPosition = mPager.getCurrentItem();
        getInfoObject.getTabs();
    }

    public void logout(){
        SharedPreferences.Editor editor = shareAuth.edit();
        editor.remove("HAS_LOGIN");
        editor.commit();
        Intent intentLogout = new Intent(this, SplashActivity.class);
        startActivity(intentLogout);
        finish();


    }

    public void goToSavedPage(){
        Intent intentSaved = new Intent(this, SavedActivity.class);
        startActivity(intentSaved);
    }

    public void editTabButton(){
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("tablist",tabLists.get(mPager.getCurrentItem()));
        lastPosition = mPager.getCurrentItem();
        startActivityForResult(intent, 1);
    }

    public void deleteTabButton(){
        if(tabLists.size() > 1){
            getInfoObject.deleteTab(tabLists.get(mPager.getCurrentItem()), mPager.getCurrentItem());
        }
        else{
            Toast.makeText(this, "Cannot delete tab anymore", Toast.LENGTH_SHORT).show();
        }
    }



}
