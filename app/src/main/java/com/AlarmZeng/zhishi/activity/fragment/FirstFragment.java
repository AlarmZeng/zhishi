package com.AlarmZeng.zhishi.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.bean.FirstPage;
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
 * Created by hunter_zeng on 2016/5/18.
 */
public class FirstFragment extends BaseFragment {

    private ViewPager viewPager;

    private FirstPage pageData;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout.fragment_first, null);

        viewPager = (ViewPager) view.findViewById(R.id.vp_first_view_pager);

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
        pageData =gson.fromJson(result, FirstPage.class);

        viewPager.setAdapter(new MyPagerAdapter());
    }

    class MyPagerAdapter extends PagerAdapter {

        BitmapUtils utils;

        public MyPagerAdapter() {

            utils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return pageData.getTop_stories().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = View.inflate(mActivity, R.layout.fragment_first_view_pager, null);

            TextView text = (TextView) view.findViewById(R.id.tv_top_title);
            ImageView image = (ImageView) view.findViewById(R.id.iv_top_image);

            FirstPage.TopStories stories = pageData.getTop_stories().get(position);
            text.setText(stories.getTitle());

            utils.display(image, stories.getImage());

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }
}
