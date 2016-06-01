package com.AlarmZeng.zhishi.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.adapter.CollectionItemAdapter;
import com.AlarmZeng.zhishi.activity.bean.MainNews;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
import com.AlarmZeng.zhishi.activity.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_zeng on 2016/5/29.
 */
public class CollectionActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;

    private List<MainNews.Stories> collections;
    private ListView listView;
    private PopupWindow window;
    private View contentView;
    private AnimationSet set;
    private CollectionItemAdapter adapter;
    private MainNews.Stories currentCollection;
    private CoordinatorLayout snackbarContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        toolbar = (Toolbar) findViewById(R.id.collection_toolbar);
        listView = (ListView) findViewById(R.id.lv_collections);
        snackbarContainer = (CoordinatorLayout) findViewById(R.id.snack_bar_container);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
        initListView();
    }

    private void initData() {

        Cursor cursor = getContentResolver().query(Constants.URI_COLLECTION_QUERY, null, null, null, null, null);

        collections = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                MainNews.Stories c = new MainNews.Stories();

                List<String> imageList = new ArrayList<>();
                imageList.add(cursor.getString(cursor.getColumnIndex("images")));
                c.setImages(imageList);
                c.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                c.setId(cursor.getString(cursor.getColumnIndex("content_id")));
                c.setType(cursor.getInt(cursor.getColumnIndex("type")));

                collections.add(c);

            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void initListView() {

        adapter = new CollectionItemAdapter(CollectionActivity.this, collections);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainNews.Stories currentItem = collections.get(position);

                Intent mainIntent = new Intent(CollectionActivity.this, MainContentActivity.class);
                mainIntent.putExtra("stories", currentItem);
                startActivity(mainIntent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentCollection = (MainNews.Stories) adapter.getItem(position);
                showPopupWindow(view);
                return true;
            }
        });
    }

    private void showPopupWindow(View view) {

        if (window == null) {

            contentView = View.inflate(CollectionActivity.this, R.layout.popup_window_layout, null);
            TextView collect = (TextView) contentView.findViewById(R.id.tv_collect);
            collect.setText("删除");
            collect.setOnClickListener(this);
            window = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            window.setBackgroundDrawable(new ColorDrawable());

            //弹窗动画效果
            set = new AnimationSet(false);

            ScaleAnimation scale = new ScaleAnimation(0.2f, 1, 0.2f, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(500);

            AlphaAnimation alpha = new AlphaAnimation(0.2f, 1);
            alpha.setDuration(500);

            set.addAnimation(scale);
            set.addAnimation(alpha);
        }

        window.showAsDropDown(view, view.getWidth() / 2, -view.getHeight());
        contentView.startAnimation(set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_collect :
                window.dismiss();
                int number = getContentResolver().delete(Constants.URI_COLLECTION_DELETE, "content_id = ?", new String[] {currentCollection.getId()});
                if (number > 0) {
                    adapter.deleteItem(currentCollection);
                    SnackbarUtils.showSnackbar(snackbarContainer, "删除成功");
                }
                else {
                    SnackbarUtils.showSnackbar(snackbarContainer, "删除失败");
                }

                break;
        }
    }
}
