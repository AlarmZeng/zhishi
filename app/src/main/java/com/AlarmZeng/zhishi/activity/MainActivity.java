package com.AlarmZeng.zhishi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.fragment.MainNewsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SwipeRefreshLayout refresh;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    public boolean isDark = false;
    private long firstClick;
    private CoordinatorLayout snackContainer;
    private ImageView collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        snackContainer = (CoordinatorLayout) findViewById(R.id.snack_bar_container);

        DrawerArrowDrawable arrowDrawable = new DrawerArrowDrawable(MainActivity.this);
        toolbar.setNavigationIcon(arrowDrawable);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }

            }
        });

        initView();
        initData();
    }

    private void initView() {

        collection = (ImageView) findViewById(R.id.iv_collection);
        collection.setOnClickListener(this);

        refresh = (SwipeRefreshLayout) findViewById(R.id.srl_swipe_refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_blue_light, android.R.color.holo_blue_light, android.R.color.holo_blue_light);

    }

    private void initData() {

        MainNewsFragment fragment = new MainNewsFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_fragment_container, fragment, "mainNews");
        transaction.commit();

    }

    public void setSwipeRefreshEnable(boolean enable) {

        refresh.setEnabled(enable);
    }

    public SwipeRefreshLayout getRefresh() {

        return refresh;
    }

    public void setDrawerClose() {

        drawerLayout.closeDrawers();
    }

    public void setToolbarTitle(String title) {

        toolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_dark:
                /*drawerLayout.setBackgroundResource(isDark ? R.color.colorDrawerLayoutLight : R.color.colorDrawerLayoutDark);
                toolbar.setBackgroundResource(isDark ? R.color.colorLightBlue : R.color.colorToolbarDark);
                item.setTitle(!isDark ? "日间模式" : "夜间模式");
                ((MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment)).updateBackgroundMode();

                if (getSupportFragmentManager().findFragmentByTag("mainNews") !=null) {

                    ((MainNewsFragment) getSupportFragmentManager().findFragmentByTag("mainNews")).UpdateBackgroundMode();
                }
                else {

                    ((NewsFragment) getSupportFragmentManager().findFragmentByTag("news")).updateBackGroundMode();
                }

                isDark = !isDark;*/
                break;

            case R.id.action_settings:

                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(settingIntent);

               break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {

            setDrawerClose();
        }
        else {

            Snackbar sb = Snackbar.make(snackContainer, "再按一次退出", Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundResource(R.color.colorLightBlue);
            sb.show();

            if (firstClick > 0) {
                if (System.currentTimeMillis() - firstClick < 1500) {
                    super.onBackPressed();
                    firstClick = 0;
                }
            }

            firstClick = System.currentTimeMillis();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_collection :
                Intent collectionIntent = new Intent(MainActivity.this, CollectionActivity.class);
                startActivity(collectionIntent);
                break;
        }
    }
}