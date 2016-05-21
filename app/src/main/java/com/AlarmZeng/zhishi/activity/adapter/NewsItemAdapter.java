package com.AlarmZeng.zhishi.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.bean.News;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by hunter_zeng on 2016/5/20.
 */
public class NewsItemAdapter extends BaseAdapter {

    private Context context;

    private News news;

    private BitmapUtils utils;

    public NewsItemAdapter(Context context, News news) {

        utils = new BitmapUtils(context);
        this.context = context;
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.getStories().size();
    }

    @Override
    public Object getItem(int position) {
        return news.getStories().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view;
        ViewHolder holder;
        if (convertView == null) {

            view = View.inflate(context, R.layout.item_news_list_view, null);

            holder = new ViewHolder();
            holder.newsTitle = (TextView) view.findViewById(R.id.tv_news_item_title);
            holder.newsImage = (ImageView) view.findViewById(R.id.iv_news_item_image);
            view.setTag(holder);

        }
        else {

            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        News.NewsStories stories = (News.NewsStories) getItem(position);

        holder.newsTitle.setText(stories.getTitle());

        if (stories.getImages() != null) {
            holder.newsImage.setVisibility(View.VISIBLE);
            utils.display(holder.newsImage, stories.getImages().get(0));
        }
        else {
            holder.newsImage.setVisibility(View.GONE);
        }

        /*String images = stories.getImages().get(0);

        if (!TextUtils.isEmpty(images)) {
            utils.display(holder.newsImage, images);
        }*/

        return view;
    }

    class ViewHolder {

        TextView newsTitle;

        ImageView newsImage;
    }
}