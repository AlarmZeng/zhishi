package com.AlarmZeng.zhishi.activity.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.MainActivity;
import com.AlarmZeng.zhishi.activity.bean.MenuListItem;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 侧滑菜单fragment
 * Created by AlarmZeng on 2016/5/17.
 */
public class MenuFragment extends BaseFragment {

    private ListView mListView;

    private List<MenuListItem> mItemList;

    private MenuItemAdapter adapter;

    private TextView firstPage;

    private int itemChecked = -1;
    private View headerView;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, null);
        headerView = inflater.inflate(R.layout.fragment_menu_header, null);

        firstPage = (TextView) headerView.findViewById(R.id.tv_first_page);
        firstPage.setBackgroundColor(getResources().getColor(R.color.itemChecked));

        mListView = (ListView) view.findViewById(R.id.lv_menu_list);
        mListView.addHeaderView(headerView);

        firstPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstPage.setBackgroundColor(getResources().getColor(R.color.itemChecked));

                itemChecked = -1;
                adapter.notifyDataSetChanged();

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fl_fragment_container, new MainNewsFragment());
                transaction.commit();

                ((MainActivity) mActivity).setDrawerClose();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                int headerCount = mListView.getHeaderViewsCount();

                firstPage.setBackgroundColor(Color.WHITE);
                itemChecked = position - headerCount;
                adapter.notifyDataSetChanged();

                if (position >= headerCount) {
                    MenuListItem item = (MenuListItem) adapter.getItem(position - headerCount);
                    String itemId = item.getId();
                    String title = item.getName();

                    NewsFragment newsFragment = new NewsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("itemId", itemId);
                    bundle.putString("title", title);
                    newsFragment.setArguments(bundle);

                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                            .replace(R.id.fl_fragment_container, newsFragment, "news");
                    transaction.commit();
                }

                ((MainActivity) mActivity).setDrawerClose();
            }
        });

        return view;
    }

    @Override
    protected void initData() {

        String result = PrefUtils.getString(mActivity, Constants.MENU_LIST_URL, null);
        if (!TextUtils.isEmpty(result)) {
            processResult(result);
        }

        getDataFromServer();
    }

    private void getDataFromServer() {


        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.MENU_LIST_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                String result = responseInfo.result;

                processResult(result);

                PrefUtils.putString(mActivity, Constants.MENU_LIST_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    private void processResult(String response) {

        try {

            JSONObject object = new JSONObject(response);
            JSONArray array = object.getJSONArray("others");

            mItemList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {

                MenuListItem item = new MenuListItem();
                JSONObject itemObject = array.getJSONObject(i);
                String id = itemObject.getString("id");
                String name = itemObject.getString("name");

                item.setId(id);
                item.setName(name);

                mItemList.add(item);
            }

            adapter = new MenuItemAdapter();
            mListView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MenuItemAdapter extends BaseAdapter {

        private boolean isDark = true;

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            View view;
            if (convertView == null) {
                view = View.inflate(getActivity(), R.layout.item_menu_list, null);
            } else {
                view = convertView;
            }

            view.setBackgroundResource(isDark ? R.drawable.menu_list_selector : R.color.colorMenuListDark);

            if (itemChecked == position) {
                view.setEnabled(true);
            } else {
                view.setEnabled(false);
            }

            MenuListItem item = (MenuListItem) getItem(position);

            TextView title = (TextView) view.findViewById(R.id.tv_menu_title);
            title.setText(item.getName());

            return view;
        }

    }


}
