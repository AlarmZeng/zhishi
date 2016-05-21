package com.AlarmZeng.zhishi.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.MainActivity;
import com.AlarmZeng.zhishi.activity.adapter.NewsItemAdapter;
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
 * Created by hunter_zeng on 2016/5/20.
 */
public class NewsFragment extends BaseFragment{

    private ImageView newsImage;

    private TextView newsTitle;

    private ListView newsListView;

    private String itemId;

    private String title;
    private News news;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View headerView = View.inflate(mActivity, R.layout.fragment_news_header, null);
        View view = View.inflate(mActivity, R.layout.fragment_news, null);

        newsImage = (ImageView) headerView.findViewById(R.id.iv_news_image);
        newsTitle = (TextView) headerView.findViewById(R.id.tv_news_title);
        newsListView = (ListView) view.findViewById(R.id.lv_news_list);

        newsListView.addHeaderView(headerView);

        return view;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();

        itemId = bundle.getString("itemId");
        title = bundle.getString("title");

        ((MainActivity) mActivity).setToolbarTitle(title);

        getNewsDateFromServer(itemId);
    }

    private void getNewsDateFromServer(String itemId) {

        String url = Constants.MENU_NEWS_URL + itemId;

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
        news = gson.fromJson(result, News.class);

        BitmapUtils utils = new BitmapUtils(mActivity);
        utils.display(newsImage, news.getBackground());

        newsTitle.setText(news.getDescription());

        NewsItemAdapter adapter = new NewsItemAdapter(mActivity, news);
        newsListView.setAdapter(adapter);

        newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {

                ((MainActivity) mActivity).setSwipeRefreshEnable(false);
            }
        });
    }

}
