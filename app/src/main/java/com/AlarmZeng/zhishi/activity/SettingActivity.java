package com.AlarmZeng.zhishi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;

import java.io.File;

/**
 * Created by hunter_zeng on 2016/5/24.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private RelativeLayout rlFirst;

    private boolean isFirstChecked;
    private CheckBox cbFirst;
    private RelativeLayout rlSecond;
    private CoordinatorLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rlFirst = (RelativeLayout) findViewById(R.id.rl_first_item);
        cbFirst = (CheckBox) findViewById(R.id.cb_first_check);
        rlSecond = (RelativeLayout) findViewById(R.id.rl_second_item);
        container = (CoordinatorLayout) findViewById(R.id.snack_bar_container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlFirst.setOnClickListener(this);
        rlSecond.setOnClickListener(this);

        isFirstChecked = PrefUtils.getBoolean(SettingActivity.this, "firstChecked", false);
        cbFirst.setChecked(isFirstChecked);

        initData();
    }

    private void initData() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_first_item :

                isFirstChecked = !isFirstChecked;
                cbFirst.setChecked(isFirstChecked);

                PrefUtils.putBoolean(SettingActivity.this, "firstChecked", isFirstChecked);

                break;

            case R.id.rl_second_item :

                clearCache();

                break;
        }
    }

    private void clearCache() {

        File cacheFile = new File(getCacheDir().getAbsolutePath());
        File dbFile = new File("/data/data/" + getPackageName() + "/databases");

        boolean isClear = false;

        if (cacheFile.exists()) {

            isClear = cacheFile.delete();
        }
        else if(dbFile.exists()) {

            isClear = dbFile.delete();
        }

        if (isClear) {
            Snackbar.make(container, "清理成功", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Snackbar.make(container, "无需清理", Snackbar.LENGTH_SHORT).show();
        }

    }
}
