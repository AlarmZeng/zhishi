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
import com.AlarmZeng.zhishi.activity.gloable.Constants;
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

    private boolean isDark = true;

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
            holder.title = (TextView) view.findViewById(R.id.tv_title);
            holder.content = (TextView) view.findViewById(R.id.tv_list_content);
            holder.image = (ImageView) view.findViewById(R.id.iv_list_image);

            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        //view.setBackgroundResource(isDark ? R.drawable.card_background_selector : R.color.colorListItemDark);

        MainNews.Stories stories = (MainNews.Stories) getItem(position);

        String readId = PrefUtils.getString(context, "is_read", "");

        if (stories.getType() == Constants.MAIN_TOPIC) {
            holder.title.setVisibility(View.VISIBLE);
            view.findViewById(R.id.ll_main_list_content).setVisibility(View.GONE);
            holder.title.setText(stories.getTitle());
        }
        else {

            view.findViewById(R.id.ll_main_list_content).setBackgroundResource(isDark ? R.drawable.card_background_selector : R.color.colorListItemDark);
            holder.title.setVisibility(View.GONE);
            holder.content.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            holder.content.setText(stories.getTitle());

            if (readId.contains(stories.getId())) {
                holder.content.setTextColor(Color.GRAY);
            } else {
                holder.content.setTextColor(Color.BLACK);
            }
            utils.display(holder.image, stories.getImages().get(0));
        }

        return view;
    }

    /*public void UpdateBackgroundMode() {

        isDark = ((MainActivity) context).isDark;
        notifyDataSetChanged();
    }*/

    class ViewHolder {

        TextView title;

        TextView content;

        ImageView image;

    }
}
