package com.xyf.MyApp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sh-xiayf on 15-5-7.
 */
public class ToastUtils {

    public static void showToast(Context mContext,String message){
        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
    }

}
