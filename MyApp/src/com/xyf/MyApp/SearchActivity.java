package com.xyf.MyApp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-8.
 */
public class SearchActivity extends ListActivity implements AdapterView.OnItemClickListener{

    private List<HashMap<String,String>> dbvalues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setOnItemClickListener(this);

        dbvalues = ((MyApplication)getApplication()).getSearchReuslt();

        getListView().setAdapter(new SearchAdapter(this,dbvalues));

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
    public void onItemClick(AdapterView<?> adapterView, View view, final int ii, long l) {
        new AlertDialog.Builder(this).setTitle("notification")
                .setMessage("what do you want to do?")
                .setPositiveButton("call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + dbvalues.get(ii ).get(DBUtils.DBCol.COL_PHONE)));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = dbvalues.get(ii).get(DBUtils.DBCol.COL_EMAIL);
                        if (TextUtils.isEmpty(email)) {
                            ToastUtils.showToast(SearchActivity.this,"no email address to send");
                            return;
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        startActivity(Intent.createChooser(intent,"choose your Email app"));
                    }
                })
                .setCancelable(false)
                .create().show();
    }

}
