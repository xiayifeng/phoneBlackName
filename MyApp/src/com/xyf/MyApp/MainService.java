package com.xyf.MyApp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.*;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by sh-xiayf on 15-5-5.
 */
public class MainService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        tm.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);

        return START_STICKY;
    }

    private class MyPhoneStateListener extends PhoneStateListener{

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:

                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    HashMap<String,String> rslt = DBUtils.getInstances().queryDB(DBUtils.DBCol.COL_PHONE,incomingNumber.substring(incomingNumber.length() - 11));
                    if (rslt != null){
                        if (SharedPrefUtils.getMode(MainService.this).equals("black")){
                            endCall();
                        }
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;

                default:

                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    ITelephony iTelephony;
    TelephonyManager tm;


    public void endCall(){
        try{
            Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);

            getITelephonyMethod.setAccessible(true);

            iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[]) null);

            iTelephony.endCall();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
