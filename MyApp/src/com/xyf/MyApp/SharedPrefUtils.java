package com.xyf.MyApp;

import android.content.Context;

/**
 * Created by sh-xiayf on 15-5-4.
 */
public class SharedPrefUtils {

    private static final String SP_NAME = "app.info";

    private static final String sp_mode = "runningmode";

    public static String getMode(Context mContext){
        return get(mContext,sp_mode,"black");
    }

    public static void saveMode(Context mContext,String value){
        save(mContext,sp_mode,value);
    }

    public static String get(Context myContext,String key,String defalutValue){
        return myContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE)
                .getString(key,defalutValue);
    }

    public static void save(Context myContext,String key,String value){
        myContext.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE)
                .edit().putString(key,value).commit();
    }
}
