package com.AlarmZeng.zhishi.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.bean.Content;
import com.AlarmZeng.zhishi.activity.bean.MainNews;
import com.AlarmZeng.zhishi.activity.db.WebCacheHelper;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by hunter_zeng on 2016/5/21.
 */
public class MainContentActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private static WebView webView;

    private ImageView image;

    private CollapsingToolbarLayout toolbarLayout;
    private MainNews.TopStories topStories;
    private MainNews.Stories stories;
    private WebCacheHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topStories = (MainNews.TopStories) getIntent().getSerializableExtra("topStories");
        stories = (MainNews.Stories) getIntent().getSerializableExtra("stories");

        image = (ImageView) findViewById(R.id.image_view);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_collapsing_toolbar);

        if (topStories != null) {
            toolbarLayout.setTitle(topStories.getTitle());
        } else {
            toolbarLayout.setTitle(stories.getTitle());
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        boolean isFirstChecked = PrefUtils.getBoolean(MainContentActivity.this, "firstChecked", false);

        webView = (WebView) findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setTextSize(isFirstChecked ? WebSettings.TextSize.LARGER : WebSettings.TextSize.NORMAL);

        helper = WebCacheHelper.getInstance(MainContentActivity.this, 1);

        initData();

    }

    private void initData() {

        SQLiteDatabase db = helper.getWritableDatabase();

        String url;
        Cursor cursor;
        if (topStories != null) {
            url = Constants.CONTENT_URL + topStories.getId();
            cursor = db.query("web_cache", new String[]{"json"}, "newsId = ?", new String[]{topStories.getId()}, null, null, null);
        } else {
            url = Constants.CONTENT_URL + stories.getId();
            cursor = db.query("web_cache", new String[]{"json"}, "newsId = ?", new String[]{stories.getId()}, null, null, null);
        }

        if (cursor.moveToFirst()) {
            do {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                processResult(json);
            }
            while (cursor.moveToNext());
        } else {
            HttpUtils utils = new HttpUtils();
            utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    String result = responseInfo.result;
                    result = result.replaceAll("'", "''");

                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    if (topStories != null) {
                        values.put("newsId", topStories.getId());
                    } else {
                        values.put("newsId", stories.getId());
                    }
                    values.put("json", result);
                    db.insert("web_cache", null, values);
                    db.close();

                    processResult(result);
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }
        cursor.close();
    }

    private void processResult(String result) {

        Gson gson = new Gson();
        Content content = gson.fromJson(result, Content.class);

        BitmapUtils utils = new BitmapUtils(MainContentActivity.this);
        utils.display(image, content.getImage());

        /*StringBuilder sb = new StringBuilder();
        sb.append("<HTML><HEAD><LINK href=\"news.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
        sb.append(content.getBody());
        sb.append("</body></HTML>");*/

        String html = "<HTML><HEAD><LINK href=\"news.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>" + content.getBody() + "</body></HTML>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
