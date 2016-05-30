package com.AlarmZeng.zhishi.activity;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.adapter.CollectionItemAdapter;
import com.AlarmZeng.zhishi.activity.bean.Collection;
import com.AlarmZeng.zhishi.activity.gloable.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_zeng on 2016/5/29.
 */
public class CollectionActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;

    private List<Collection> collections;
    private ListView listView;
    private PopupWindow window;
    private View contentView;
    private AnimationSet set;
    private CollectionItemAdapter adapter;
    private Collection currentCollection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        toolbar = (Toolbar) findViewById(R.id.collection_toolbar);
        listView = (ListView) findViewById(R.id.lv_collections);
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

                Collection c = new Collection();

                c.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                c.setImage(cursor.getString(cursor.getColumnIndex("images")));
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                currentCollection = (Collection) adapter.getItem(position);
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
                    Toast.makeText(CollectionActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(CollectionActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
