package com.AlarmZeng.zhishi.activity;

import android.os.Bundle;
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

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.fragment.MainNewsFragment;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout refresh;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    public boolean isDark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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



                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}