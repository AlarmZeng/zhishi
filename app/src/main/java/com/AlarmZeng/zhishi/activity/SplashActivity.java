package com.AlarmZeng.zhishi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.AlarmZeng.zhishi.R;
import com.AlarmZeng.zhishi.activity.gloable.Constants;
import com.AlarmZeng.zhishi.activity.utils.NetWorkUtils;
import com.AlarmZeng.zhishi.activity.utils.PrefUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 闪屏界面
 */
public class SplashActivity extends Activity {

    private ImageView mSplashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mSplashImage = (ImageView) findViewById(R.id.splash_image);

        initData();
    }

    private void initData() {

        String result = PrefUtils.getString(SplashActivity.this, Constants.SPLASH_URL, null);

        if (!TextUtils.isEmpty(result)) {
            processResult(result);
        }
        else {
            mSplashImage.setImageResource(R.drawable.splash_image);
            initAnimation();
        }

    }

    private void getDataFromServer() {

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.SPLASH_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                String result = responseInfo.result;

                processResult(result);

                PrefUtils.putString(SplashActivity.this, Constants.SPLASH_URL, result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                Toast.makeText(SplashActivity.this, "发生错误", Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void processResult(String result) {

        try {
            JSONObject object = new JSONObject(result);
            String imageUrl = object.getString("img");

            BitmapUtils utils = new BitmapUtils(SplashActivity.this);
            utils.display(mSplashImage, imageUrl);

            initAnimation();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initAnimation() {

        ScaleAnimation scale = new ScaleAnimation(1, 1.2f, 1, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(3000);
        scale.setFillAfter(true);

        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (!NetWorkUtils.isNetworkConnected(SplashActivity.this)) {
                    Toast.makeText(SplashActivity.this, "网络没有连接", Toast.LENGTH_SHORT).show();
                }
                else {
                    getDataFromServer();
                }

                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSplashImage.startAnimation(scale);

    }

}
