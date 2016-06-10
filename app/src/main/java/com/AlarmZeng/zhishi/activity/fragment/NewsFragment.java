package com.AlarmZeng.zhishi.activity.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.MainActivity;
import com.AlarmZeng.zhishi.activity.NewsContentActivity;
import com.AlarmZeng.zhishi.activity.adapter.NewsItemAdapter;
import com.AlarmZeng.zhishi.activity.bean.News;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
import com.AlarmZeng.zhishi.activity.utils.NetWorkUtils;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;
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
public class NewsFragment extends BaseFragment {

    private ImageView newsImage;

    private TextView newsTitle;

    private ListView newsListView;

    private String itemId;

    private String title;
    private News news;
    private NewsItemAdapter adapter;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View headerView = View.inflate(mActivity, R.layout.fragment_news_header, null);
        View view = View.inflate(mActivity, R.layout.fragment_news, null);

        newsImage = (ImageView) headerView.findViewById(R.id.iv_news_image);
        newsTitle = (TextView) headerView.findViewById(R.id.tv_news_title);
        newsListView = (ListView) view.findViewById(R.id.lv_news_list);

        newsListView.addHeaderView(headerView);

        final SwipeRefreshLayout refresh = ((MainActivity) mActivity).getRefresh();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    protected void initData() {

        Bundle bundle = getArguments();

        itemId = bundle.getString("itemId");
        title = bundle.getString("title");

        ((MainActivity) mActivity).setToolbarTitle(title);

        if (!NetWorkUtils.isNetworkConnected(mActivity)) {
            String result = PrefUtils.getString(mActivity, Constants.MENU_NEWS_URL + itemId, "");
            if (!TextUtils.isEmpty(result)) {
                processResult(result);
            }
        }
        getNewsDateFromServer(itemId);
    }

    private void getNewsDateFromServer(String itemId) {

        final String url = Constants.MENU_NEWS_URL + itemId;

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                PrefUtils.putString(mActivity, url, result);
                processResult(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "网络发生错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processResult(String result) {

        Gson gson = new Gson();
        news = gson.fromJson(result, News.class);

        BitmapUtils utils = new BitmapUtils(mActivity);
        utils.display(newsImage, news.getBackground());

        newsTitle.setText(news.getDescription());

        adapter = new NewsItemAdapter(mActivity, news);
        newsListView.setAdapter(adapter);

        newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {

                //((MainActivity) mActivity).setSwipeRefreshEnable(false);
                if (newsListView != null && newsListView.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (absListView.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                }
            }
        });

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                int headerCount = newsListView.getHeaderViewsCount();

                if (position < headerCount) {
                    return;
                }

                News.NewsStories newsStories = news.getStories().get(position - headerCount);

                String readId = PrefUtils.getString(mActivity, "is_read", "");

                TextView tvTitle = (TextView) view.findViewById(R.id.tv_news_item_title);
                tvTitle.setTextColor(Color.GRAY);

                if (!readId.contains(newsStories.getId())) {
                    readId = readId + "," + newsStories.getId();
                    PrefUtils.putString(mActivity, "is_read", readId);
                }

                Intent newsContentIntent = new Intent(mActivity, NewsContentActivity.class);
                newsContentIntent.putExtra("newsStories", newsStories);
                startActivity(newsContentIntent);
            }
        });
    }

}
