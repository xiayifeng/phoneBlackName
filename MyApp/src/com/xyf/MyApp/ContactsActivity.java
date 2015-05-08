package com.xyf.MyApp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-7.
 */
public class ContactsActivity extends ListActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private List<HashMap<String ,String >> dbvalues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);

        if (!DBUtils.getInstances().isDBInit()){
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+dbvalues.get(i).get(DBUtils.DBCol.COL_PHONE)));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int ii, long l) {

        new AlertDialog.Builder(this).setTitle("notification")
                .setMessage("you want to delet it?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            HashMap<String,String> current = dbvalues.get(ii);
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
