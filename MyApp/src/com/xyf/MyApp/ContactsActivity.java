package com.xyf.MyApp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-7.
 */
public class ContactsActivity extends ListActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private List<HashMap<String ,String >> dbvalues;

    private static final int FUN_KEY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        if (!DBUtils.getInstances().isDBInit()){
            finish();
            return;
        }

        if (DBUtils.getInstances().isDBNull()){
            finish();
            return;
        }

        dbvalues = DBUtils.getInstances().getAllContacts();
        setListAdapter(new ContactsAdapter(this,dbvalues));
    }

    private void parseListView(){
        if (!DBUtils.getInstances().isDBInit()){
            return;
        }
        dbvalues = DBUtils.getInstances().getAllContacts();

        ((ContactsAdapter)getListView().getAdapter()).setContacts(dbvalues);
        ((ContactsAdapter)getListView().getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private List<HashMap<String,String>> searchMessage(String message){
        List<HashMap<String,String>> result = new ArrayList<HashMap<String, String>>();
        for (HashMap<String,String> map : dbvalues){
            String name = map.get(DBUtils.DBCol.COL_NAME);
            String phone = map.get(DBUtils.DBCol.COL_PHONE);
            String email = map.get(DBUtils.DBCol.COL_EMAIL);

            if (name.contains(message) || phone.contains(message) || email.contains(message)) {
                result.add(map);
            }
        }

        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int ii, long l) {
        if ( ii == 0 ){
            View contentView = LayoutInflater.from(this).inflate(com.xyf.MyApp.R.layout.inputdialog,null);
            final EditText username = (EditText) contentView.findViewById(com.xyf.MyApp.R.id.inputdialog_name);
            final EditText telephone = (EditText) contentView.findViewById(com.xyf.MyApp.R.id.inputdialog_phone);
            new AlertDialog.Builder(this).setTitle("input contacts")
                    .setView(contentView)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = username.getText().toString().trim();
                            String phone = telephone.getText().toString().trim();
                            if (TextUtils.isEmpty(phone)){
                                ToastUtils.showToast(ContactsActivity.this,"phone is can not be empty");
                                return;
                            }

                            ContentValues cValue = new ContentValues();
                            cValue.put(DBUtils.DBCol.COL_NAME,name);
                            cValue.put(DBUtils.DBCol.COL_PHONE, phone);
                            DBUtils.getInstances().insertDB(cValue);
                            parseListView();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }else if(ii == 1){
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
                                Intent intent = new Intent(ContactsActivity.this,SearchActivity.class);
                                ContactsActivity.this.startActivity(intent);
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
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+dbvalues.get(ii-FUN_KEY).get(DBUtils.DBCol.COL_PHONE)));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String email = dbvalues.get(ii - FUN_KEY).get(DBUtils.DBCol.COL_EMAIL);
                            if (TextUtils.isEmpty(email)) {
                                return;
                            }
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("message/rfc822");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                            startActivity(Intent.createChooser(intent,"choose your Email app"));
                        }
                    })
                    .setCancelable(true)
                    .create().show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int ii, long l) {

        if (ii == 0 ){
            return true;
        }
        new AlertDialog.Builder(this).setTitle("notification")
                .setMessage("you want to delet it?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            HashMap<String,String> current = dbvalues.get(ii-FUN_KEY);
                            ContentValues cvalue = new ContentValues();
                            cvalue.put(DBUtils.DBCol.COL_NAME,current.get(DBUtils.DBCol.COL_NAME));
                            cvalue.put(DBUtils.DBCol.COL_PHONE,current.get(DBUtils.DBCol.COL_PHONE));
                            DBUtils.getInstances().deleteDB(cvalue);
                            parseListView();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .create().show();

        return true;
    }
}
