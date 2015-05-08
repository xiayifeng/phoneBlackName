package com.xyf.MyApp;

import android.app.Application;

/**
 * Created by sh-xiayf on 15-5-5.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBUtils.getInstances().initDB(this);
    }


}
