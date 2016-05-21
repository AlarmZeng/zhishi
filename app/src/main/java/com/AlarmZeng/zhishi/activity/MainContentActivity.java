package com.AlarmZeng.zhishi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.bean.Content;
import com.AlarmZeng.zhishi.activity.bean.MainNews;
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
public class MainContentActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private WebView webView;

    private ImageView image;

    private CollapsingToolbarLayout toolbarLayout;
    private MainNews.TopStories topStories;
    private MainNews.Stories stories;

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
        }
        else {
            toolbarLayout.setTitle(stories.getTitle());
        }

        webView = (WebView) findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);

        initData();

    }

    private void initData() {

        String url;
        if (topStories != null) {
            url = Constants.MAIN_CONTNET_URL + topStories.getId();
        }
        else {
            url = Constants.MAIN_CONTNET_URL + stories.getId();
        }

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
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
}
