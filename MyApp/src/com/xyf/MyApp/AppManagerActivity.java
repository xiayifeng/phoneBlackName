package com.xyf.MyApp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-18.
 */
public class AppManagerActivity extends ListActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private List<ApplicationInfo> appInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appInfos = getAppInfos();

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        getListView().setAdapter(new AppManagerAdapter(this,appInfos));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private DeleteAppReceiver mDeleteAppReceiver;

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if (mDeleteAppReceiver != null){
                unregisterReceiver(mDeleteAppReceiver);
            }
        }catch (Exception e){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            if (mDeleteAppReceiver == null) {
                mDeleteAppReceiver = new DeleteAppReceiver();
            }
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
            registerReceiver(mDeleteAppReceiver,intentFilter);
        }catch (Exception e){
        }
    }

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    class DeleteAppReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("xyf","refresh ...");
            appInfos = getAppInfos();
            ((AppManagerAdapter)getListView().getAdapter()).setAppInfos(appInfos);
            ((AppManagerAdapter)getListView().getAdapter()).notifyDataSetChanged();
        }
    }

    private List<ApplicationInfo> getAppInfos(){
        PackageManager pm = getPackageManager();

        List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();

        List<PackageInfo> pInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

        for (PackageInfo temp : pInfos){

            if (isNotSystemApp(temp.applicationInfo) && !temp.applicationInfo.packageName.equals(getPackageName())){
                apps.add(temp.applicationInfo);
            }

        }
        return apps;
    }

    public boolean isNotSystemApp(ApplicationInfo appinfo){
        if ((appinfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
            return true;
        }else if((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0){
            return true;
        }

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ApplicationInfo currentInfo = appInfos.get(i);

        try{
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            String pkg = "com.android.settings";
            String cls = "com.android.settings.applications.InstalledAppDetails";

            intent.setComponent(new ComponentName(pkg,cls));
            intent.setData(Uri.parse("package:" + currentInfo.packageName));
            startActivity(intent);
        }catch (Exception e){
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final ApplicationInfo currentInfo = appInfos.get(i);

        new AlertDialog.Builder(this).setTitle("notification")
                .setMessage("do your want to open or uninstall this app?")
                .setNegativeButton("open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            Intent intent = getPackageManager().getLaunchIntentForPackage(currentInfo.packageName);
                            startActivity(intent);
                        }catch (Exception e){
                        }
                    }
                })
                .setPositiveButton("uninstall", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:" + currentInfo.packageName));
                            startActivity(intent);
                        } catch (Exception e) {
                        }
                    }
                })
                .setCancelable(true)
                .create().show();

        return true;
    }
}
