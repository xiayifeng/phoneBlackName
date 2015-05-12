package com.xyf.MyApp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by sh-xiayf on 15-5-12.
 */
public class BootCompletedReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("xyf","boot completed...");
        if (isServiceRunning(context,"com.xyf.MyApp.MainService")) {
            Log.e("xyf","service has started ...");
        }else {
            Log.e("xyf","start service ...");
            Intent intent1 = new Intent(context,MainService.class);
            context.startService(intent1);
        }
    }

    private boolean isServiceRunning(Context mContext,String className){
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);

        if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
