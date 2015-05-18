package com.xyf.MyApp;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-18.
 */
public class RunningServiceActivity extends ListActivity {

    private List<ApplicationInfo> appinfos;
    private List<ActivityManager.RunningServiceInfo> runningServiceInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appinfos = new ArrayList<ApplicationInfo>();
        runningServiceInfos = new ArrayList<ActivityManager.RunningServiceInfo>();

        ShowProcessDialog(this,"searching ...");
        getRunningServices();
        DismissProcessDialog(this);
        getListView().setAdapter(new RunningServiceAdapter(this,appinfos,runningServiceInfos));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getRunningServices(){
        ActivityManager activityManager = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(Integer.MAX_VALUE);

        appinfos.clear();
        runningServiceInfos.clear();

        for (ActivityManager.RunningServiceInfo temp : serviceList){
            try {
                String packageName = temp.service.getPackageName();
                PackageInfo pInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                if (isNotSystemApp(pInfo.applicationInfo) && !(pInfo.applicationInfo.packageName).equals(getPackageName())){
                    appinfos.add(pInfo.applicationInfo);
                    runningServiceInfos.add(temp);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        Log.e("xyf",String.format("appinfo.size(%d)",appinfos.size()));
        Log.e("xyf",String.format("runningservice.size(%d)",runningServiceInfos.size()));

    }

    public boolean isNotSystemApp(ApplicationInfo appinfo){
        if ((appinfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
            return true;
        }else if((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0){
            return true;
        }

        return false;
    }

    private ProgressDialog mProgressDialog;
    private void DismissProcessDialog(Context mContext){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void ShowProcessDialog(Context mContext,String message){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

}
