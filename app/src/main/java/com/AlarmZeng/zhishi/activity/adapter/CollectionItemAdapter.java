package com.AlarmZeng.zhishi.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.bean.Collection;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by hunter_zeng on 2016/5/29.
 */
public class CollectionItemAdapter extends BaseAdapter {

    private Context context;
    private List<Collection> collections;
    private BitmapUtils utils;

    public CollectionItemAdapter(Context context, List<Collection> collections) {
        this.context = context;
        this.collections = collections;
        utils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(context, R.layout.item_collections, null);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.tv_list_content);
            holder.image = (ImageView) view.findViewById(R.id.iv_list_image);
            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Collection c = (Collection) getItem(position);
        holder.title.setText(c.getTitle());

        utils.display(holder.image, c.getImage());

        return view;
    }

    public void deleteItem(Collection collection) {

        collections.remove(collection);
        notifyDataSetChanged();
    }

    class ViewHolder {

        TextView title;

        ImageView image;
    }
}
