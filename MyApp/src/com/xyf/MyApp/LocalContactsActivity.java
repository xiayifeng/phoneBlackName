package com.xyf.MyApp;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-12.
 */
public class LocalContactsActivity extends ListActivity implements AdapterView.OnItemClickListener{

    private List<HashMap<String,String>> dbvalues;

    private static final int FUN_KEY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!checkPermission(Manifest.permission.READ_CONTACTS)) {
            ToastUtils.showToast(this,"permission dined");
            finish();
        }

        dbvalues = getLocalContacts();

        if(dbvalues.size() == 0){
            ToastUtils.showToast(this,"no contacts");
            finish();
        }


        getListView().setAdapter(new ContactsAdapter(this,dbvalues,false,true));

        getListView().setOnItemClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean checkPermission(String requestPermission){
        PackageManager pm = getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        String[] requestPermissions = pInfo.requestedPermissions;
        for (String tmp : requestPermissions) {
            if (tmp.equals(requestPermission)){
                return true;
            }
        }

        return false;
    }

    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
    };

    private List<HashMap<String,String>> getLocalContacts(){
        List<HashMap<String,String>> result = new ArrayList<HashMap<String, String>>();

        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION,null,null,null);

        if (c != null){

            while(c.moveToNext()){
                String phone = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                HashMap<String,String> map = new HashMap<String, String>();
                map.put(DBUtils.DBCol.COL_NAME,name);
                map.put(DBUtils.DBCol.COL_PHONE,phone);

                result.add(map);
            }

            c.close();
        }

        return result;
    }

    private List<HashMap<String,String>> searchMessage(String message){
        List<HashMap<String,String>> result = new ArrayList<HashMap<String, String>>();
        for (HashMap<String,String> map : dbvalues){
            String name = map.get(DBUtils.DBCol.COL_NAME);
            String phone = map.get(DBUtils.DBCol.COL_PHONE);

            if (name.contains(message) || phone.contains(message)) {
                result.add(map);
            }
        }

        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        if( i == 0){
            View contentView = LayoutInflater.from(this).inflate(com.xyf.MyApp.R.layout.search,null);
            final EditText searchMessage = (EditText) contentView.findViewById(com.xyf.MyApp.R.id.search_edit);
            new AlertDialog.Builder(this).setTitle("Search")
                    .setView(contentView)
                    .setPositiveButton("search", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String message = searchMessage.getText().toString().trim();
                            if(TextUtils.isEmpty(message)){
                                return;
                            }

                            List<HashMap<String,String>> result = searchMessage(message);
                            if (result.size() > 0){
                                ((MyApplication)getApplication()).setSearchReuslt(result);
                                Intent intent = new Intent(LocalContactsActivity.this,SearchActivity.class);
                                LocalContactsActivity.this.startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }else{
            new AlertDialog.Builder(this).setTitle("notification")
                    .setMessage("what do you want to do?")
                    .setPositiveButton("call", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int ii) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + dbvalues.get(i-FUN_KEY).get(DBUtils.DBCol.COL_PHONE).replaceAll("-","").replaceAll(" ","")));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int ii) {

                        }
                    })
                    .setCancelable(true)
                    .create().show();
        }
    }
}
