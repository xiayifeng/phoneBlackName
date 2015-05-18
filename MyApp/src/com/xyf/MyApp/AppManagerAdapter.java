package com.xyf.MyApp;

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
public class AppManagerAdapter extends BaseAdapter {

    private List<ApplicationInfo> appInfos;
    private Context myContext;

    public AppManagerAdapter(Context mContext,List<ApplicationInfo> infos){
        this.myContext = mContext;
        this.appInfos = infos;
    }

    class ViewHolder {
        ImageView icon;
        TextView txtname;
        TextView packagename;
    }

    public void setAppInfos(List<ApplicationInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return appInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if (view == null) {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(myContext).inflate(R.layout.appinfo,null);
            mHolder.icon = (ImageView) view.findViewById(R.id.appinfo_icon);
            mHolder.txtname = (TextView) view.findViewById(R.id.appinfo_name);
            mHolder.packagename = (TextView) view.findViewById(R.id.appinfo_package);
            view.setTag(mHolder);
        } else {
          mHolder = (ViewHolder) view.getTag();
        }

        ApplicationInfo current = appInfos.get(i);
        String name = current.loadLabel(myContext.getPackageManager()).toString();
        Drawable icon = current.loadIcon(myContext.getPackageManager());
        String packageName = current.packageName;

        mHolder.icon.setBackgroundDrawable(icon);
        mHolder.txtname.setText(name);
        mHolder.packagename.setText(packageName);

        return view;
    }
}
