package com.AlarmZeng.zhishi.activity.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.AlarmZeng.zhishi.R;

/**
 * Created by hunter_zeng on 2016/5/30.
 */
public class SnackbarUtils {

    public static void showSnackbar(View view, String text) {

        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundResource(R.color.colorLightBlue);
        sb.show();
    }
}
