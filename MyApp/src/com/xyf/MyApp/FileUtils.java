package com.xyf.MyApp;

import android.content.Context;
import android.os.Environment;

/**
 * Created by sh-xiayf on 15-5-11.
 */
public class FileUtils {

    public static boolean isExendStorageExits(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getExtendPath(){
        try{
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            return path;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
