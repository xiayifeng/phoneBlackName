package com.xyf.MyApp;

import android.content.Context;
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
 * Created by sh-xiayf on 15-5-8.
 */
public class SearchAdapter extends BaseAdapter{

    private List<HashMap<String,String>> contacts;
    private Context myContext;

    public SearchAdapter(Context myContext,List<HashMap<String,String>> list){
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

            mHolder.oneLayout.setVisibility(View.GONE);
            mHolder.twoLayout.setVisibility(View.VISIBLE);

            HashMap<String,String> current = contacts.get(i);

            String col_name = current.get(DBUtils.DBCol.COL_NAME);
            String col_email = current.get(DBUtils.DBCol.COL_EMAIL);
            String col_phone = current.get(DBUtils.DBCol.COL_PHONE);
            String col_companyphone = current.get(DBUtils.DBCol.COL_COMPANYPHONE);

            if (col_phone.equals("null")){
                col_name = "";
            }

            if (col_companyphone == null || col_companyphone.equals("null")){
                col_companyphone = "";
            }

            if (col_email == null || col_email.equals("null")){
                col_email = "";
            }

            if (col_name.equals("null")){
                col_name = "";
            }

            mHolder.txt1.setText(col_name);
            mHolder.txt2.setText(col_phone);
            mHolder.txt3.setText(col_email);
            mHolder.txt4.setText(col_companyphone);

        return view;
    }

}
