package com.AlarmZeng.zhishi.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.bean.MainNews;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by hunter_zeng on 2016/5/19.
 */
public class MainNewsItemAdapter extends BaseAdapter {

    private MainNews pageData;

    private Context context;

    private BitmapUtils utils;

    public MainNewsItemAdapter(Context context, MainNews pageData) {

        this.context = context;
        this.pageData = pageData;
        utils = new BitmapUtils(context);
    }

    public void addList(List<MainNews.Stories> stories) {

        pageData.getStories().addAll(stories);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pageData.getStories().size();
    }

    @Override
    public Object getItem(int position) {
        return pageData.getStories().get(position);
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

            view = View.inflate(context, R.layout.item_main_list_view, null);

            holder = new ViewHolder();
            holder.content = (TextView) view.findViewById(R.id.tv_list_content);
            holder.image = (ImageView) view.findViewById(R.id.iv_list_image);

            view.setTag(holder);

        }
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        MainNews.Stories stories = (MainNews.Stories) getItem(position);

        holder.content.setText(stories.getTitle());

        String readId = PrefUtils.getString(context, "is_read", "");
        if (readId.contains(stories.getId())) {
            holder.content.setTextColor(Color.GRAY);
        }
        utils.display(holder.image, stories.getImages().get(0));

        return view;
    }

    class ViewHolder {

        TextView content;

        ImageView image;

    }
}
