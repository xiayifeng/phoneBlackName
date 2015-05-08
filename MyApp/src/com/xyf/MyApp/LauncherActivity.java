package com.xyf.MyApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class LauncherActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageView icon = (ImageView) findViewById(R.id.laucher_icon);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setFillAfter(true);
        icon.startAnimation(alphaAnimation);

        Intent intent = new Intent(LauncherActivity.this,MainService.class);
        startService(intent);

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LauncherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);


    }

    Handler uiHandler = new Handler(Looper.getMainLooper());
}
