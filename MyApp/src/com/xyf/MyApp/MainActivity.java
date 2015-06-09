package com.xyf.MyApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-4-30.
 */
public class MainActivity extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private EditText filepath;
    private Button importbutton;
    private Button exportButton;
    private Button showlist;
    private Button localcontacts;
    private Button AppManager;
    private Button RunningService;
    private Button deviceInfo;
    private RadioGroup mode_rg;
    private RadioButton black_mode;
    private RadioButton white_mode;
    private RadioButton quiet_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);

        filepath = (EditText) findViewById(R.id.filepath);
        importbutton = (Button) findViewById(R.id.importbutton);
        exportButton = (Button) findViewById(R.id.exportbutton);
        showlist = (Button) findViewById(R.id.contactlist);
        localcontacts = (Button) findViewById(R.id.localcontacts);
        AppManager = (Button) findViewById(R.id.appmanager);
        RunningService = (Button) findViewById(R.id.runningservice);
        deviceInfo = (Button) findViewById(R.id.deivceinfo);
        mode_rg = (RadioGroup) findViewById(R.id.mode_rg);
        black_mode = (RadioButton) findViewById(R.id.mode_rb_black);
        white_mode = (RadioButton) findViewById(R.id.mode_rb_white);
        quiet_mode = (RadioButton) findViewById(R.id.mode_rb_quiet);

        importbutton.setOnClickListener(this);
        exportButton.setOnClickListener(this);
        showlist.setOnClickListener(this);
        filepath.setOnClickListener(this);
        localcontacts.setOnClickListener(this);
        AppManager.setOnClickListener(this);
        RunningService.setOnClickListener(this);
        deviceInfo.setOnClickListener(this);

        mode_rg.setOnCheckedChangeListener(this);

        //init
        String mode = SharedPrefUtils.getMode(this);
        if(mode.equals("")){
            SharedPrefUtils.saveMode(this, "black");
            black_mode.setChecked(true);
        }else if(mode.equals("black")){
            black_mode.setChecked(true);
        }else if(mode.equals("white")){
            white_mode.setChecked(true);
        }else if(mode.equals("quiet")){
            quiet_mode.setChecked(true);
        }


        if (DBUtils.getInstances().isDBInit() && DBUtils.getInstances().isDBNull() && isAssetsFileExits("phone")){
            showImportToast();
        }

    }

    private boolean isAssetsFileExits(String totalPath){
        try {
            String[] files = getAssets().list("");
            for (String tmp : files){
                if (tmp.equals(totalPath)){
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void showImportToast(){
        new AlertDialog.Builder(this).setTitle("notification")
                .setMessage("your contacts is null ,do you import default contacts ?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            importContacts(getAssets().open("phone"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setCancelable(false).
                create().show();
    }

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    private void importContacts(final InputStream is){
        ShowProcessDialog(MainActivity.this, "importing ...");
        Log.e("xyf","importing ...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("xyf","runner...");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    while ((line = br.readLine()) != null){
                        String[] users = line.split(",");
                        Log.e("xyf",String.format("line(%s)users.length(%d)",line,users.length));
                        if (users.length != 4){
                            continue;
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBUtils.DBCol.COL_NAME,users[0]);
                        contentValues.put(DBUtils.DBCol.COL_PHONE,users[1]);
                        contentValues.put(DBUtils.DBCol.COL_EMAIL,users[2]);
                        contentValues.put(DBUtils.DBCol.COL_COMPANYPHONE,users[3]);
                        DBUtils.getInstances().insertDB(contentValues);
                    }
                    br.close();
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            DismissProcessDialog(MainActivity.this);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("xyf","do import exception");
                    DismissProcessDialog(MainActivity.this);
                }
            }
        }).start();
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
    public void onClick(View view) {
        if (view.getId() == R.id.importbutton){
            Log.e("xyf","do import");
            File f = new File(filepath.getText().toString().trim());
            if (f.exists()){
                try {
                    importContacts(new FileInputStream(f));
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast(this,"can't parse this file");
                }
            }else {
                ToastUtils.showToast(this,"file not exit");
            }
        }else if(view.getId() == R.id.contactlist){
            Intent intent = new Intent(MainActivity.this,ContactsActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.filepath){
            Intent intent =  new Intent(MainActivity.this,FileActivity.class);
            startActivityForResult(intent,LIST_CODE);
        }else if(view.getId() == R.id.exportbutton){
            if (FileUtils.isExendStorageExits() && !TextUtils.isEmpty(FileUtils.getExtendPath()) && !DBUtils.getInstances().isDBNull()){
                exportContacts();
            }
        }else if(view.getId() == R.id.localcontacts){
            Intent intent = new Intent(MainActivity.this,LocalContactsActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.appmanager){
            Intent intent = new Intent(MainActivity.this,AppManagerActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.runningservice){
            Intent intent = new Intent(MainActivity.this,RunningServiceActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.deivceinfo){
            Intent intent = new Intent(MainActivity.this,DeviceActivity.class);
            startActivity(intent);
        }
    }

    public void exportContacts(){
        final String path = FileUtils.getExtendPath();
        new AlertDialog.Builder(this).setTitle("notification")
                .setMessage("contacts while export to \""+ path +"/contacts.csv\"?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ShowProcessDialog(MainActivity.this,"exporting ...");
                        try{
                            Log.e("xyf","begin export");
                            List<HashMap<String,String>> result = DBUtils.getInstances().getAllContacts();
                            File dstFile = new File(path + File.separator + "contact.csv");
                            if(dstFile.exists()){
                                dstFile.delete();
                            }
                            dstFile.createNewFile();
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dstFile)));
                            for (HashMap<String,String> map : result){
                                String name = map.get(DBUtils.DBCol.COL_NAME);
                                String phone = map.get(DBUtils.DBCol.COL_PHONE);
                                String email = map.get(DBUtils.DBCol.COL_EMAIL);
                                String compayphone = map.get(DBUtils.DBCol.COL_COMPANYPHONE);
                                String content = name + "," + phone + "," + email + "," + compayphone + "\n";
                                bw.write(content);
                            }
                            bw.flush();
                            bw.close();
                            Log.e("xyf","end export");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        DismissProcessDialog(MainActivity.this);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .create().show();
    }

    private static final int LIST_CODE = 1101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIST_CODE && resultCode == Activity.RESULT_OK){
            String path = data.getStringExtra("path");
            filepath.setText(path);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup.getCheckedRadioButtonId() == R.id.mode_rb_white){
            SharedPrefUtils.saveMode(this,"white");
        }else if(radioGroup.getCheckedRadioButtonId() == R.id.mode_rb_black){
            SharedPrefUtils.saveMode(this,"black");
        }else if(radioGroup.getCheckedRadioButtonId() == R.id.mode_rb_quiet){
            SharedPrefUtils.saveMode(this,"quiet");
        }
    }


    private ProgressDialog mProgressDialog;
    private void DismissProcessDialog(Context mContext){
       if (mProgressDialog != null){
           mProgressDialog.dismiss();
           mProgressDialog = null;
       }
    }

    private void ShowProcessDialog(Context mContext,String message){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    private void setProcessMessage(String message){
        if(mProgressDialog != null){
            mProgressDialog.setMessage(message);
        }
    }
}
