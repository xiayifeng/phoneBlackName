package com.xyf.MyApp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        aManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
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
//                    HideFloatView();
                    resetRingerMode();
                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    HashMap<String,String> rslt = DBUtils.getInstances().queryDB(DBUtils.DBCol.COL_PHONE,incomingNumber.substring(incomingNumber.length() - 11));
                    if (rslt == null){
                        rslt = DBUtils.getInstances().queryDB(DBUtils.DBCol.COL_COMPANYPHONE,incomingNumber);

                        if (rslt == null){
                            rslt = DBUtils.getInstances().queryDB(DBUtils.DBCol.COL_COMPANYPHONE,incomingNumber.substring(incomingNumber.length() - 8));
                        }
                    }

                    if (rslt != null){
                        String name = rslt.get(DBUtils.DBCol.COL_NAME);
                        if (SharedPrefUtils.getMode(MainService.this).equals("black")){
                            endCall();
                            return;
                        }
                        if (!TextUtils.isEmpty(name)){
                            Log.e("xyf",String.format("calling(%s)",name));
                            showFloatView(name);
                            serviceHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    HideFloatView();
                                }
                            },5000);
                        }
                        if (SharedPrefUtils.getMode(MainService.this).equals("quiet")){
                            setSlientMode();
                        }
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    HideFloatView();
                    resetRingerMode();
                    break;

                default:

                    break;
            }
        }
    }

    private Handler serviceHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    AudioManager aManager;
    private static int lastMode = -1;
    private static int vibrateMode = -1;

    public void setSlientMode(){
        lastMode = aManager.getRingerMode();
        vibrateMode = aManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
        aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        aManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_OFF);
    }

    public void resetRingerMode(){
        if (lastMode != -1){
            aManager.setRingerMode(lastMode);
        }
        if (vibrateMode != -1){
            aManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,AudioManager.VIBRATE_SETTING_ON);
        }
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


    WindowManager mWindowManager;
    WindowManager.LayoutParams wmParams;
    View mFloatLayout;

    public void showFloatView(String name){
       /* wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        wmParams.x = 100;
        wmParams.y = 100;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.height = 50;
//        wmParams.width = 100;

//        mFloatLayout = new LinearLayout(MainService.this);
//        mFloatLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//        mFloatLayout.setOrientation(LinearLayout.VERTICAL);
//        mFloatLayout.setBackgroundColor(Color.WHITE);
//        TextView txt = new TextView(MainService.this);
//        txt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
//        txt.setText(name);
//        txt.setTextColor(Color.BLACK);
//        mFloatLayout.addView(txt);

        LayoutInflater inflater = LayoutInflater.from(MainService.this);
        mFloatLayout = inflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView txt = (TextView) mFloatLayout.findViewById(android.R.id.text1);
        txt.setText(name);

        mWindowManager.addView(mFloatLayout, wmParams);*/

        mWindowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type=2002;  //type是关键，这里的2002表示系统级窗口，你也可以试试2003。
        wmParams.format=1;
        /**
         *这里的flags也很关键
         *代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
         *40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
         */
        wmParams.flags=40;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(MainService.this);
        mFloatLayout = inflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView txt = (TextView) mFloatLayout.findViewById(android.R.id.text1);
        txt.setText(name);

        mWindowManager.addView(mFloatLayout, wmParams);//创建View
    }

    public void HideFloatView(){
        if (mFloatLayout != null){
            mWindowManager.removeView(mFloatLayout);
            mFloatLayout = null;
        }
    }

}
