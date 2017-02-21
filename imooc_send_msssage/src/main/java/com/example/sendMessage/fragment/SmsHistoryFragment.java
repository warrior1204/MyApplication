package com.example.sendMessage.fragment;
//使用initloader异步加载数据时，在Content provider中，要调用类似观察者模式中通知的方法，
// 即，在update方法中通知观察者记录改变 notifyDataSetChanged，在query方法中注册观察者setNotificationUri，这样通知来了可接收并处理。
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sendMessage.bean.SendedMsg;
import com.example.sendMessage.db.SmsProvider;
import com.example.sendMessage.view.FlowLayout;
import com.imooc.sendMessage.R;

import java.text.SimpleDateFormat;


public class SmsHistoryFragment extends ListFragment{
    private static final int LOADER_ID = 1;
    private LayoutInflater mInflater;
    private CursorAdapter mCursorAdapter;
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        initLoader();
        setupListAdapter();
    }

    private void initLoader() {
        //启动一个装载器（Loader），用LoaderManager管理。其中参数LOADER_ID：一个唯一ID来标志装载器
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override// CursorLoader，它查询ContentProvider的ContentResolver然后返回一个Cursor。使用这个装载器是从一个ContentProvider异步加载数据的最好方式
                    // new CursorLoader（）相当于调用getContentResolver().query（）方法
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getActivity(), SmsProvider.URI_SMS_ALL, null, null, null, null);
                return loader;
            }

            @Override //每次改变和Loader相关的数据库记录后会调用一次
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (loader.getId() == LOADER_ID) {
                    mCursorAdapter.swapCursor(data);
                }
            }

            @Override //在关闭Activity/Fragment时调用，释放资源
            public void onLoaderReset(Loader<Cursor> loader) {
                mCursorAdapter.swapCursor(null);
            }
        });
    }

    private void setupListAdapter() {
        mCursorAdapter = new CursorAdapter(getActivity(),null,false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = mInflater.inflate(R.layout.item_sended_msg, parent, false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView msg = (TextView) view.findViewById(R.id.id_tv_msg);
                FlowLayout fl = (FlowLayout) view.findViewById(R.id.id_fl_contacts);
                TextView fes = (TextView) view.findViewById(R.id.id_tv_fes);
                TextView date = (TextView) view.findViewById(R.id.id_tv_date);
                
                msg.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_MSG)));
                fes.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_FESNAME)));
                long dateVal = cursor.getLong(cursor.getColumnIndex(SendedMsg.COLUMN_DATE));
                date.setText(parseDate(dateVal));

                String names = cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_NAME));
                if (TextUtils.isEmpty(names)) {
                    return;
                }
                fl.removeAllViews();
                for (String name : names.split(":")) {
                    addTag(name, fl);
                }
            }
        };
        setListAdapter(mCursorAdapter);
    }

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String parseDate(long dateVal) {
        return df.format(dateVal);
    }

    private void addTag(String name, FlowLayout fl) {
        TextView tv = (TextView) mInflater.inflate(R.layout.tag, fl, false);
        tv.setText(name);
        fl.addView(tv);
    }
}
