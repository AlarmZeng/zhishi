package com.AlarmZeng.zhishi.activity.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hunter_zeng on 2016/5/17.
 */
public class PrefUtils {

    public static void putString(Context context, String key, String value){

        SharedPreferences pref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String def) {

        SharedPreferences pref = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        return pref.getString(key, def);
    }
}
