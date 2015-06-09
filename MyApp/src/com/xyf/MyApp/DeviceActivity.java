package com.xyf.MyApp;

import android.app.ListActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shxiayf on 2015/6/9.
 */
public class DeviceActivity extends ListActivity{

    public TelephonyManager getTM(){
        return (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    }

    public String getPhone(){

        String number =  getTM().getLine1Number();

        if (!TextUtils.isEmpty(number)){
            return number.substring(number.length() - 11);
        }

        return number;
    }

    public String getImsi(){
        return getTM().getSubscriberId();
    }

    public String getImei(){
        return getTM().getDeviceId();
    }

    public String getCPUABI(){
        return Build.CPU_ABI;
    }

    public String getOSVersion(){
        return Build.VERSION.SDK;
    }

    public String getHardWare(){
        return Build.HARDWARE;
    }

    public String getUser(){
        return Build.USER;
    }

    public String getDisplay(){
        return Build.DISPLAY;
    }

    public String getProduct(){
        return Build.PRODUCT;
    }

    public String getBoard(){
        return Build.BOARD;
    }

    public String getDevice(){
        return Build.DEVICE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("xyf","DeviceActivity onCreate");

        List<String> deviceMsg = new ArrayList<String>();

        deviceMsg.add("telephone:"+getPhone());
        deviceMsg.add("IMSI:"+getImsi());
        deviceMsg.add("IMEI:"+getImei());
        deviceMsg.add("CPU_ABI:"+getCPUABI());
        deviceMsg.add("OS:"+getOSVersion());
//        deviceMsg.add("HardWare:"+getHardWare());
//        deviceMsg.add("User:"+getUser());
//        deviceMsg.add("Display:"+getDisplay());
//        deviceMsg.add("Product:"+getProduct());
//        deviceMsg.add("Board:"+getBoard());
//        deviceMsg.add("Device:"+getDevice());

        String[] StrMsg = new String[deviceMsg.size()];
        int i = 0;
        for (String tmp : deviceMsg){
            StrMsg[i++] = tmp;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,StrMsg);

        getListView().setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
