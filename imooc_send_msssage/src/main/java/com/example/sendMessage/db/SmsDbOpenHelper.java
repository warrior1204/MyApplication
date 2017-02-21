package com.example.sendMessage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sendMessage.bean.SendedMsg;


public class SmsDbOpenHelper extends SQLiteOpenHelper {

    private String sql = "create table  " + SendedMsg.TABLE_NAME + " ("
            + "_id integer primary key autoincrement, "
            + SendedMsg.COLUMN_MSG + " text, "
            + SendedMsg.COLUMN_NAME + " text, "
            + SendedMsg.COLUMN_NUMBERS + " text, "
            + SendedMsg.COLUMN_FESNAME + " text, "
            + SendedMsg.COLUMN_DATE + " integer )";


    private SmsDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context.getApplicationContext(), name, factory, version);
    }

    //懒汉式单例模式
    private static SmsDbOpenHelper mHelper;
    public static SmsDbOpenHelper getInstance(Context context, String name, int version) {
        if (mHelper == null) {
            synchronized (SmsDbOpenHelper.class) {
                if (mHelper == null) {
                    mHelper = new SmsDbOpenHelper(context, name, null, version);
                }
            }
        }
        return mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
