package com.AlarmZeng.zhishi.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.adapter.MainNewsItemAdapter;
import com.AlarmZeng.zhishi.activity.adapter.MainNewsPagerAdapter;
import com.AlarmZeng.zhishi.activity.bean.MainNews;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by hunter_zeng on 2016/5/18.
 */
public class MainNewsFragment extends BaseFragment {

    private ViewPager viewPager;

    private MainNews pageData;

    private LinearLayout llContainer;

    private ImageView ivRedPoint;

    private ListView listView;

    private boolean mIsPaint;
    private float pointWidth;
    private MainNewsPagerAdapter adapter;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            int current = viewPager.getCurrentItem();

            current++;

            if (current >= adapter.getCount()) {
                current = 0;
            }

            viewPager.setCurrentItem(current);

            handler.sendEmptyMessageDelayed(0, 4000);
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

        return view;
    }

    @Override
    protected void initData() {

        String result = PrefUtils.getString(mActivity, Constants.TOP_STORIES_URL, null);
        if (!TextUtils.isEmpty(result)) {
            processResult(result);
        }

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
        pageData =gson.fromJson(result, MainNews.class);

        initViewPager();
        initListView();
    }

    private void initViewPager() {

        if (!mIsPaint) {
            for (int i = 0; i < pageData.getTop_stories().size(); i++) {

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

        adapter = new MainNewsPagerAdapter(mActivity, pageData);
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

        handler.sendEmptyMessageDelayed(0, 4000);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN :
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

    private void initListView () {

        MainNewsItemAdapter adapter = new MainNewsItemAdapter(mActivity, pageData);
        listView.setAdapter(adapter);
    }
}
