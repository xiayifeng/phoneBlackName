package com.xyf.MyApp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        LinearLayout oneLayout;
        TextView add;
        RelativeLayout twoLayout;
        TextView txt1;
        TextView txt2;
        TextView txt3;
        TextView txt4;
    }

    @Override
    public int getCount() {
        return contacts.size() + 2;
    }

    @Override
    public Object getItem(int i) {
        if(i == 0 || i == 1){
            return new TextView(myContext);
        }
        return contacts.get(i-2);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
            /*if (i == 0){
                view = LayoutInflater.from(myContext).inflate(android.R.layout.simple_list_item_1,null);
                TextView txt = (TextView) view.findViewById(android.R.id.text1);
                txt.setText("Add");
                return view;
            }else{
                ViewHolder mHolder = null;
                mHolder = new ViewHolder();
                view = LayoutInflater.from(myContext).inflate(android.R.layout.simple_list_item_2,null);
                mHolder.txt1 = (TextView) view.findViewById(android.R.id.text1);
                mHolder.txt2 = (TextView) view.findViewById(android.R.id.text2);

                Log.e("xyf",String.format("__i(%d)__",i));
                HashMap<String,String> current = contacts.get(i-1);

                Log.e("xyf",current == null ? "true":"false");

                mHolder.txt1.setText(current.get(DBUtils.DBCol.COL_NAME));
                mHolder.txt2.setText(current.get(DBUtils.DBCol.COL_PHONE));
                return view;
            }*/
        ViewHolder mHolder = null;
        if (view == null){
            view = LayoutInflater.from(myContext).inflate(R.layout.contacts,null);
            mHolder = new ViewHolder();
            mHolder.oneLayout = (LinearLayout) view.findViewById(R.id.contacts_online);
            mHolder.twoLayout = (RelativeLayout) view.findViewById(R.id.contacts_twoline);
            mHolder.add = (TextView) view.findViewById(R.id.add);
            mHolder.txt1 = (TextView) view.findViewById(R.id.text1);
            mHolder.txt2 = (TextView) view.findViewById(R.id.text2);
            mHolder.txt3 = (TextView) view.findViewById(R.id.text3);
            mHolder.txt4 = (TextView) view.findViewById(R.id.text4);

            view.setTag(mHolder);
        }else{
            mHolder = (ViewHolder) view.getTag();
        }

        if (i == 0){
            mHolder.oneLayout.setVisibility(View.VISIBLE);
            mHolder.twoLayout.setVisibility(View.GONE);
            mHolder.add.setText("Add");
        }else if(i == 1){
            mHolder.oneLayout.setVisibility(View.VISIBLE);
            mHolder.twoLayout.setVisibility(View.GONE);
            mHolder.add.setText("Search");
        }else{
            mHolder.oneLayout.setVisibility(View.GONE);
            mHolder.twoLayout.setVisibility(View.VISIBLE);

            HashMap<String,String> current = contacts.get(i-2);

            String col_name = current.get(DBUtils.DBCol.COL_NAME);
            String col_email = current.get(DBUtils.DBCol.COL_EMAIL);
            String col_phone = current.get(DBUtils.DBCol.COL_PHONE);
            String col_companyphone = current.get(DBUtils.DBCol.COL_COMPANYPHONE);

            if (col_phone.equals("null")){
                col_name = "";
            }

            if (col_companyphone.equals("null")){
                col_companyphone = "";
            }

            if (col_email.equals("null")){
                col_email = "";
            }

            if (col_name.equals("null")){
                col_name = "";
            }

            mHolder.txt1.setText(col_name);
            mHolder.txt2.setText(col_phone);
            mHolder.txt3.setText(col_email);
            mHolder.txt4.setText(col_companyphone);
        }

        return view;
    }
}
