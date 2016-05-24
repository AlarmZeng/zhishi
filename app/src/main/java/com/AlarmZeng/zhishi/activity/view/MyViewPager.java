package com.AlarmZeng.zhishi.activity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hunter_zeng on 2016/5/23.
 */
public class MyViewPager extends ViewPager{

    private int startX;

    private int startY;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewPager(Context context) {
        super(context);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dx) > Math.abs(dy)) {// 左右划
                    if (dx > 0) {
                        if (this.getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        if (getCurrentItem() == this.getAdapter().getCount() - 1) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;

            default:
                break;
        }


        return super.dispatchTouchEvent(ev);
    }
}
