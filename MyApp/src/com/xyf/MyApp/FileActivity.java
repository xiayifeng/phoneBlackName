package com.xyf.MyApp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by sh-xiayf on 15-5-5.
 */
public class FileActivity extends ListActivity implements AdapterView.OnItemClickListener{

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public String CURRENTPATH = ROOT_PATH;

    private File[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setOnItemClickListener(this);

        files = getPathFiles(CURRENTPATH);
        setAdapter(files);

    }

    public void update(){
        Log.e("xyf",String.format("path(%s)",CURRENTPATH));
        files = getPathFiles(CURRENTPATH);

        int len = 0;
        if (CURRENTPATH.equals(ROOT_PATH)){
            len = files.length;
        }else{
            len = files.length + 1;
        }
        String[] names = new String[len];
        int i = 0;
        if(!CURRENTPATH.equals(ROOT_PATH)){
            names[i++] = "...";
        }
        for (File f : files){
            names[i++] = f.getName();
        }

        ((FileAdapter)getListView().getAdapter()).setNames(names);
        ((FileAdapter)getListView().getAdapter()).notifyDataSetChanged();
    }

    public void setAdapter(File[] files){
        int len = 0;
        if (CURRENTPATH.equals(ROOT_PATH)){
            len = files.length;
        }else{
            len = files.length + 1;
        }
        String[] names = new String[len];
        int i = 0;
        if(!CURRENTPATH.equals(ROOT_PATH)){
            names[i++] = "...";
        }
        for (File f : files){
            names[i++] = f.getName();
        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        setListAdapter(new FileAdapter(this,names));
    }

    public File[] getPathFiles(String path){
        File f = new File(path);
        return f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getName().startsWith(".")){
                    return false;
                }
                return true;
            }
        });
    }

    public void process(String  currentFilePath){
        File currentFile = new File(currentFilePath);
        if (currentFile.isFile()){
            Intent intent = new Intent();
            intent.putExtra("path",currentFile.getAbsolutePath());
            setResult(Activity.RESULT_OK,intent);
            finish();
        } else {
            CURRENTPATH = currentFile.getAbsolutePath();
            update();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0){
            if (CURRENTPATH.equals(ROOT_PATH)){
                File currentFile = files[i];
                process(currentFile.getAbsolutePath());
            } else {
                CURRENTPATH = CURRENTPATH.substring(0,CURRENTPATH.lastIndexOf("/"));
                Log.e("xyf","CURRENTPATH="+CURRENTPATH);
                process(CURRENTPATH);
            }
        }else{
            File currentFile = files[i];
            process(currentFile.getAbsolutePath());
        }
    }
}
