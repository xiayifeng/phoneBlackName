package com.xyf.MyApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by sh-xiayf on 15-5-7.
 */
public class FileAdapter extends BaseAdapter {

    private Context myContext;
    private String[] names;

    public FileAdapter(Context mContext,String...name){
        this.names = name;
        this.myContext = mContext;
    }

    class ViewHolder {
        TextView v;
    }

    public void setNames(String...names){
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return names[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view == null){
            view = LayoutInflater.from(myContext).inflate(android.R.layout.simple_list_item_1,null);
            mHolder = new ViewHolder();
            mHolder.v = (TextView) view.findViewById(android.R.id.text1);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }

        mHolder.v.setText(names[i]);

        return view;
    }
}
