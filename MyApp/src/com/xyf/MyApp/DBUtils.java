package com.xyf.MyApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sh-xiayf on 15-5-5.
 */
public class DBUtils {

    private DBOpenHelper dbHelper;

    private DBUtils(){

    }

    private static DBUtils instances;

    public static DBUtils getInstances(){
        if (instances == null){
            instances = new DBUtils();
        }
        return instances;
    }

    public void initDB(Context mContext){
        if (dbHelper == null){
            dbHelper = new DBOpenHelper(mContext);
        }
    }

    public boolean isDBInit(){
        if(dbHelper == null){
            return false;
        }

        return true;
    }

    public void insertDB(ContentValues contentValues){
        HashMap<String,String> reuslt = queryDB(DBCol.COL_PHONE,contentValues.getAsString(DBCol.COL_PHONE));

        if(reuslt == null){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(DBCol.TABLENAME, DBCol.COL_NAME, contentValues);
            db.close();
        }else{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            updateDB(contentValues);
        }
    }

    public void deleteDB(ContentValues contentValues){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBCol.TABLENAME,DBCol.COL_PHONE + "= ? ", new String[]{contentValues.getAsString(DBCol.COL_PHONE)});
        db.close();
    }

    public void updateDB(ContentValues contentValues){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(DBCol.TABLENAME,contentValues,DBCol.COL_PHONE + "= ?",new String[] {contentValues.getAsString(DBCol.COL_PHONE)});
        db.close();
    }

    public boolean isDBNull(){
        boolean flag = true;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(DBCol.TABLENAME,null,null,null,null,null,null,null);

        if (c.moveToFirst()){
            flag = false;
        }

        c.close();
        db.close();

        return flag;
    }

    public List<HashMap<String,String>> getAllContacts(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(DBCol.TABLENAME,null,null,null,null,null,null,null);

        if(c.moveToFirst()){
            List<HashMap<String,String>> result = new ArrayList<HashMap<String, String>>();
            do{
                HashMap<String,String> map = new HashMap<String, String>();
                String username = c.getString(c.getColumnIndex(DBCol.COL_NAME));
                String userphone = c.getString(c.getColumnIndex(DBCol.COL_PHONE));

                map.put(DBCol.COL_NAME,username);
                map.put(DBCol.COL_PHONE,userphone);
                result.add(map);
            }while (c.moveToNext());

            c.close();
            db.close();

            return result;
        }

        c.close();
        db.close();

        return null;
    }

    public HashMap<String,String> queryDB(String name,String value){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(DBCol.TABLENAME, null, name + " = ?", new String[]{value}, null, null, null);

        if(c.moveToFirst()){
            String username = c.getString(c.getColumnIndex(DBCol.COL_NAME));
            String userphone = c.getString(c.getColumnIndex(DBCol.COL_PHONE));

            HashMap<String,String> result = new HashMap<String, String>();
            result.put(DBCol.COL_NAME,username);
            result.put(DBCol.COL_PHONE,userphone);

            c.close();
            db.close();

            return result;
        }

        c.close();
        db.close();

        return null;
    }


    public class DBCol implements BaseColumns{
        public static final String DBNAME = "phone.db";
        public static final String TABLENAME = "companyphone";
        public static final String COL_NAME = "username";
        public static final String COL_PHONE = "userphone";
    }


    class DBOpenHelper extends SQLiteOpenHelper{

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DBOpenHelper(Context context) {
            super(context, DBCol.DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "CREATE TABLE " + DBCol.TABLENAME + " ("
                    + DBCol._ID + " INTEGER PRIMARY KEY,"
                    + DBCol.COL_NAME + " TEXT,"
                    + DBCol.COL_PHONE + " TEXT NOT NULL)";
            sqLiteDatabase.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            String sql = "DROP TABLE " + DBCol.TABLENAME + " IF EXITS";
            sqLiteDatabase.execSQL(sql);
            onCreate(sqLiteDatabase);
        }


    }
}
