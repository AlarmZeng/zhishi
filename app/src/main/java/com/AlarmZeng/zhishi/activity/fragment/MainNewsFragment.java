package com.AlarmZeng.zhishi.activity.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.MainActivity;
import com.AlarmZeng.zhishi.activity.MainContentActivity;
import com.AlarmZeng.zhishi.activity.adapter.MainNewsItemAdapter;
import com.AlarmZeng.zhishi.activity.adapter.MainNewsPagerAdapter;
import com.AlarmZeng.zhishi.activity.bean.Before;
import com.AlarmZeng.zhishi.activity.bean.MainNews;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hunter_zeng on 2016/5/18.
 */
public class MainNewsFragment extends BaseFragment {

    private ViewPager viewPager;

    private MainNews mainNews;

    private LinearLayout llContainer;

    private ImageView ivRedPoint;

    private ListView listView;

    private boolean mIsPaint;
    private float pointWidth;
    private MainNewsPagerAdapter adapter;
    private MainNewsItemAdapter newsItemAdapter;

    private String loadDate;
    private boolean isLoading = false;

    private Timer timer = new Timer();

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            int current = viewPager.getCurrentItem();

            current += 1;

            if (current == adapter.getCount()) {
                current = 0;
            }

            viewPager.setCurrentItem(current, true);

            //handler.sendEmptyMessageDelayed(0, 4000);
        }

    };


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout.fragment_main_news, null);
        listView = (ListView) view.findViewById(R.id.lv_fragment_main_news);

        View headerView = View.inflate(mActivity, R.layout.fragment_main_news_header, null);
        viewPager = (ViewPager) headerView.findViewById(R.id.vp_first_view_pager);
        llContainer = (LinearLayout) headerView.findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) headerView.findViewById(R.id.iv_red_point);

        listView.addHeaderView(headerView);

        ((MainActivity) mActivity).setToolbarTitle("知事");

        final SwipeRefreshLayout refresh = ((MainActivity) mActivity).getRefresh();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getDataFromServer();

                refresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    protected void initData() {

        String result = PrefUtils.getString(mActivity, Constants.TOP_STORIES_URL, null);

        if (!TextUtils.isEmpty(result)) {
            processResult(result);
        }

        getDataFromServer();
    }

    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.TOP_STORIES_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                String result = responseInfo.result;

                processResult(result);

                PrefUtils.putString(mActivity, Constants.TOP_STORIES_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void processResult(String result) {

        Gson gson = new Gson();
        mainNews = gson.fromJson(result, MainNews.class);

        List<MainNews.Stories> list = mainNews.getStories();
        MainNews.Stories title = new MainNews.Stories();
        title.setTitle("今日热闻");
        title.setType(Constants.MAIN_TOPIC);
        list.add(0, title);

        loadDate = mainNews.getDate();
        initViewPager();
        initListView();
    }

    private void initViewPager() {

        if (!mIsPaint) {
            for (int i = 0; i < mainNews.getTop_stories().size(); i++) {

                ImageView point = new ImageView(mActivity);
                point.setImageResource(R.drawable.shape_circle_defaul);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                if (i != 0) {
                    params.leftMargin = 8;
                }
                point.setLayoutParams(params);

                llContainer.addView(point);
            }

            mIsPaint = true;
        }

        adapter = new MainNewsPagerAdapter(mActivity, mainNews);
        viewPager.setAdapter(adapter);

        pointWidth = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int leftMargin = (int) (position * pointWidth + positionOffset * pointWidth);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = leftMargin;

                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        handler.sendEmptyMessageDelayed(0, 4000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.sendEmptyMessage(0);
            }
        }, 5000, 5000);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacksAndMessages(null);
                        break;

                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(0, 4000);
                        break;

                }

                return false;
            }
        });

    }

    private void initListView() {

        newsItemAdapter = new MainNewsItemAdapter(mActivity, mainNews);
        listView.setAdapter(newsItemAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (listView != null && listView.getChildCount() > 0) {

                    boolean enable = (firstVisibleItem == 0) && (absListView.getChildAt(firstVisibleItem).getTop() == 0);

                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
                }

                if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {

                    loadMore();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                int headerCount = listView.getHeaderViewsCount();
                MainNews.Stories stories = mainNews.getStories().get(position - headerCount);

                if (stories.getType() != Constants.MAIN_TOPIC) {
                    String readId = PrefUtils.getString(mActivity, "is_read", "");
                    TextView tvTitle = (TextView) view.findViewById(R.id.tv_list_content);
                    tvTitle.setTextColor(Color.GRAY);
                    if (!readId.contains(stories.getId())) {

                        readId = readId + "," + stories.getId();
                        PrefUtils.putString(mActivity, "is_read", readId);
                    }

                    Intent mainContentIntent = new Intent(mActivity, MainContentActivity.class);
                    mainContentIntent.putExtra("stories", stories);
                    startActivity(mainContentIntent);
                }
            }
        });
    }

    private void loadMore() {

        isLoading = true;
        getBeforeDataFromServer();

    }

    private void getBeforeDataFromServer() {

        HttpUtils utils = new HttpUtils();

        String url = Constants.BEFORE_NEWS_URL + loadDate;

        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                String result = responseInfo.result;

                processBeforeResult(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void processBeforeResult(String result) {

        Gson gson = new Gson();
        Before before = gson.fromJson(result, Before.class);

        loadDate = before.getDate();

        List<MainNews.Stories> stories = before.getStories();
        MainNews.Stories title = new MainNews.Stories();
        String fd = formatDay(before.getDate());
        title.setTitle(fd);
        title.setType(Constants.MAIN_TOPIC);
        stories.add(0, title);

        newsItemAdapter.addList(stories);

        isLoading = false;

    }

    private String formatDay(String day) {

        int y = Integer.parseInt(day.substring(0, 4));
        int m = Integer.parseInt(day.substring(4, 6));
        int d = Integer.parseInt(day.substring(6, 8));

        if (m == 1) {
            m = 13;
        }

        if (m == 2) {
            m = 14;
        }

        int week = (d + 2 * m + 3 * (m + 1) / 5 + y + y / 4 - y / 100 + y / 400) % 7;

        String fDay = "";

        switch (week) {

            case 1 :
                fDay = m + "月" + d + "日" + "  " + "星期二";
                break;

            case 2 :
                fDay = m + "月" + d + "日" + "  " + "星期三";
                break;

            case 3 :
                fDay = m + "月" + d + "日" + "  " + "星期四";
                break;

            case 4 :
                fDay = m + "月" + d + "日" + "  " + "星期五";
                break;

            case 5 :
                fDay = m + "月" + d + "日" + "  " + "星期六";
                break;

            case 6 :
                fDay = m + "月" + d + "日" + "  " + "星期日";
                break;

            case 7 :
                fDay = m + "月" + d + "日" + "  " + "星期一";
                break;
        }

        return fDay;
    }

    /*public void UpdateBackgroundMode() {

        newsItemAdapter.UpdateBackgroundMode();
    }*/
}
