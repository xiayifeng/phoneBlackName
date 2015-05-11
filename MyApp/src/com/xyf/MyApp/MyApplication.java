package com.xyf.MyApp;

import android.app.Application;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-5.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBUtils.getInstances().initDB(this);
        DBUtils.getInstances().update();
    }

    private List<HashMap<String,String>> searchReuslt;

    public List<HashMap<String, String>> getSearchReuslt() {
        return searchReuslt;
    }

    public void setSearchReuslt(List<HashMap<String, String>> searchReuslt) {
        this.searchReuslt = searchReuslt;
    }
}
