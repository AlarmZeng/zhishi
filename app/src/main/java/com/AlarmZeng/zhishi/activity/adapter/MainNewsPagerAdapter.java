package com.AlarmZeng.zhishi.activity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.bean.MainNews;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by hunter_zeng on 2016/5/20.
 */
public class MainNewsPagerAdapter extends PagerAdapter {

    private BitmapUtils utils;

    private Context context;

    private MainNews mainNews;

    public MainNewsPagerAdapter(Context context, MainNews mainNews) {

        this.context = context;
        this.mainNews = mainNews;
        utils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return mainNews.getTop_stories().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = View.inflate(context, R.layout.item_main_view_pager, null);

        TextView text = (TextView) view.findViewById(R.id.tv_top_title);
        ImageView image = (ImageView) view.findViewById(R.id.iv_top_image);

        MainNews.TopStories stories = mainNews.getTop_stories().get(position);
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
