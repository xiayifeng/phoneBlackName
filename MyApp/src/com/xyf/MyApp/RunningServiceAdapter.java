package com.xyf.MyApp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sh-xiayf on 15-5-18.
 */
public class RunningServiceAdapter extends BaseAdapter {

    private Context myContext;
    private List<ApplicationInfo> appinfos;
    private List<ActivityManager.RunningServiceInfo> runningServiceInfos;

    public RunningServiceAdapter(Context mContext,List<ApplicationInfo> apps,List<ActivityManager.RunningServiceInfo> services){
        this.myContext = mContext;
        this.appinfos = apps;
        this.runningServiceInfos = services;
    }

    public void setAppinfos(List<ApplicationInfo> appinfos) {
        this.appinfos = appinfos;
    }

    public void setRunningServiceInfos(List<ActivityManager.RunningServiceInfo> runningServiceInfos) {
        this.runningServiceInfos = runningServiceInfos;
    }

    class ViewHolder{
        ImageView icon;
        TextView label;
        TextView servicename;
    }

    @Override
    public int getCount() {
        return appinfos.size();
    }

    @Override
    public Object getItem(int i) {
        return appinfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if (view == null){
            mHolder = new ViewHolder();
            view = LayoutInflater.from(myContext).inflate(R.layout.appinfo,null);
            mHolder.icon = (ImageView) view.findViewById(R.id.appinfo_icon);
            mHolder.label = (TextView) view.findViewById(R.id.appinfo_name);
            mHolder.servicename = (TextView) view.findViewById(R.id.appinfo_package);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }

        ApplicationInfo currentAppInfo = appinfos.get(i);
        ActivityManager.RunningServiceInfo currentService = runningServiceInfos.get(i);

        Drawable icon = currentAppInfo.loadIcon(myContext.getPackageManager());
        String label = currentAppInfo.loadLabel(myContext.getPackageManager()).toString();
        String classname = currentService.service.getClassName();

        mHolder.icon.setBackgroundDrawable(icon);
        mHolder.label.setText(label);
        mHolder.servicename.setText(classname);

        return view;
    }
}
