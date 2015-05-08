package com.xyf.MyApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-7.
 */
public class ContactsAdapter extends BaseAdapter {

    private List<HashMap<String,String>> contacts;
    private Context myContext;

    public ContactsAdapter(Context myContext,List<HashMap<String,String>> list){
        this.myContext = myContext;
        this.contacts = list;
    }

    public void setContacts(List<HashMap<String, String>> contacts) {
        this.contacts = contacts;
    }

    class ViewHolder {
        TextView txt1;
        TextView txt2;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if (view == null){
            mHolder = new ViewHolder();
            view = LayoutInflater.from(myContext).inflate(android.R.layout.simple_list_item_2,null);
            mHolder.txt1 = (TextView) view.findViewById(android.R.id.text1);
            mHolder.txt2 = (TextView) view.findViewById(android.R.id.text2);
            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }

        HashMap<String,String> current = contacts.get(i);

        mHolder.txt1.setText(current.get(DBUtils.DBCol.COL_NAME));
        mHolder.txt2.setText(current.get(DBUtils.DBCol.COL_PHONE));

        return view;
    }
}
