package com.xyf.MyApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class LauncherActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
