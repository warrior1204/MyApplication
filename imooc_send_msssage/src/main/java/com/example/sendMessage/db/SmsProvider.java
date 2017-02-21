package com.example.sendMessage.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.sendMessage.bean.SendedMsg;


public class SmsProvider extends ContentProvider {

    private static final String TAG = "SmsProvider";
    private static final String DB_NAME = "sms.db";
    private static final int DB_VERSION = 1;

    private static final int SMS_ALL = 0;
    private static final int SMS_ONE = 1;
    private static final String AUTHORITY = "com.example.sendMessage.provider";
    private static UriMatcher matcher;
    private SmsDbOpenHelper mHelper;
    private SQLiteDatabase mDb;
//    public static final Uri URI_SMS_ALL = Uri.parse("content://" + AUTHORITY + "/sms");
    public static final Uri URI_SMS_ALL = Uri.parse("content://" + AUTHORITY + "/"+SendedMsg.TABLE_NAME);


    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, SendedMsg.TABLE_NAME, SMS_ALL);
        matcher.addURI(AUTHORITY, SendedMsg.TABLE_NAME+"/#", SMS_ONE);
    }


    @Override
    public boolean onCreate() {
        mHelper = SmsDbOpenHelper.getInstance(getContext(), DB_NAME, DB_VERSION);
        return true;
    }

    @Nullable//在Content provider中，要调用类似观察者模式中通知的方法，
    @Override//即，在query方法中注册观察者，在insert,update,delete方法中通知观察者记录改变，这样通知来了可接收并处理。
    public Cursor query(Uri uri, String[] projection, String selection,
String[] selectionArgs, String sortOrder) {
        mDb = mHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (matcher.match(uri)) {
            case SMS_ALL:
                cursor = mDb.query(SendedMsg.TABLE_NAME, projection, selection,
selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),URI_SMS_ALL);
                break;
            case SMS_ONE:
                String smsId = uri.getPathSegments().get(1);
                cursor = mDb.query(SendedMsg.TABLE_NAME, projection, "id=?",
new String[]{smsId}, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),URI_SMS_ALL);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI:" + uri);
        }
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d("SmsBiz","start");
       mDb = mHelper.getReadableDatabase();
        Uri uriReturn = null;
        switch (matcher.match(uri)) {
            case SMS_ALL:
            case SMS_ONE:
                long newSmsId = mDb.insert(SendedMsg.TABLE_NAME, null, values);
                if (newSmsId > 0) {
                    notifyDataSetChanged();//通知uri记录发生了改变
                    uriReturn = Uri.parse("content://" + AUTHORITY + "/"+SendedMsg.TABLE_NAME+"/" + String.valueOf(newSmsId));
//                    uriReturn = ContentUris.withAppendedId(uri, String.valueOf(newSmsId));
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI:" + uri);
        }
        return uriReturn;

   /*     int match = matcher.match(uri);
        if (match != SMS_ALL) {
            throw new IllegalArgumentException("Wrong URI:" + uri);
        }
        mDb = mHelper.getWritableDatabase();
        long rowId = mDb.insert(SendedMsg.TABLE_NAME, null, values);
        if (rowId > 0) {
            Log.d(TAG, "insert db succeed");
            notifyDataSetChanged();
            return ContentUris.withAppendedId(uri, rowId);
        }
        return null;*/
    }



    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    private void notifyDataSetChanged() {
        getContext().getContentResolver().notifyChange(URI_SMS_ALL, null);
    }
}
