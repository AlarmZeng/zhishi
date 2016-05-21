package com.AlarmZeng.zhishi.activity;

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
import com.AlarmZeng.zhishi.activity.bean.News;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
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
public class NewsContentActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private CollapsingToolbarLayout toolbarLayout;

    private ImageView image;

    private WebView webView;
    private News.NewsStories newsStories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newsStories = (News.NewsStories) getIntent().getSerializableExtra("newsStories");

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_collapsing_toolbar);
        image = (ImageView) findViewById(R.id.image_view);
        webView = (WebView) findViewById(R.id.web_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarLayout.setTitle(newsStories.getTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);

        initData();
    }

    private void initData() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.CONTENT_URL + newsStories.getId(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                String result = responseInfo.result;
                processResult(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void processResult(String result) {

        Gson gson = new Gson();
        Content content = gson.fromJson(result, Content.class);

        BitmapUtils utils = new BitmapUtils(NewsContentActivity.this);
        utils.display(image, content.getImage());

        String html = "<HTML><HEAD><LINK href=\"news.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>" + content.getBody() + "</body></HTML>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
