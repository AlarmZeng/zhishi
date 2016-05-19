package com.AlarmZeng.zhishi.activity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hunter_zeng on 2016/5/17.
 */
public class NetWorkUtils {

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {

            return info.isAvailable();
        }
        else {
            return false;
        }
    }
}
