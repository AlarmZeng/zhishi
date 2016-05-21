package com.AlarmZeng.zhishi.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.fragment.MainNewsFragment;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout refresh;

    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        toolbar.setNavigationIcon();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        initView();
        initData();
    }

    private void initView() {

        refresh = (SwipeRefreshLayout) findViewById(R.id.srl_swipe_refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_blue_light,android.R.color.holo_blue_light,android.R.color.holo_blue_light);

    }

    private void initData() {

        MainNewsFragment fragment = new MainNewsFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_fragment_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    public void setSwipeRefreshEnable(boolean enable) {

        refresh.setEnabled(enable);
    }

    public SwipeRefreshLayout getRefresh() {

        return refresh;
    }

    public void setDrawerClose() {

        drawer.closeDrawers();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
