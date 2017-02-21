package com.example.sendMessage;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sendMessage.bean.Festival;
import com.example.sendMessage.bean.FestivalLab;
import com.example.sendMessage.bean.Msg;
import com.example.sendMessage.bean.SendedMsg;
import com.example.sendMessage.biz.SmsBiz;
import com.example.sendMessage.view.FlowLayout;
import com.imooc.sendMessage.R;

import java.util.HashSet;

public class SendMsgActivity extends AppCompatActivity {

    private static final String KEY_FESTIVAL_ID = "festival_id";
    private static final String KEY_MSG_ID = "msg_id";
    private static final int CODE_REQUEST = 1;
    private int mFestivalId;
    private int msgId;
    private Festival mFestival;
    private Msg mMsg;

    private EditText mEtMsg;
    private Button mBtnAdd;
    private FlowLayout mFlContacts;
    private FloatingActionButton mFabSend;
    private FrameLayout mLayoutLoading;

    private HashSet<String> mContactNames = new HashSet<>();
    private HashSet<String> mContactNums = new HashSet<>();
    private LayoutInflater mInflater;

    public static final String ACTION_SEND_MSG = "ACTION_SEND_MSG";
    public static final String ACTION_DELIVER_MSG = "ACTION_DELIVER_MSG";
    private PendingIntent mSendPi;
    private PendingIntent mDeliverPi;
    private BroadcastReceiver mSendBroadcastReceiver;
    private BroadcastReceiver mDeliverBroadcastReceiver;
    private SmsBiz mSmsBiz;

    private int mTotalCount;
    private int mSendMsgCount;
    private int mDeliverMsgCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
        mInflater = LayoutInflater.from(this);
        mSmsBiz = new SmsBiz(this);
        initViews();
        initDatas();
        initEvents();
        initReceiver();
    }

    private void initReceiver() {
        Intent sendIntent = new Intent(ACTION_SEND_MSG);
        mSendPi = PendingIntent.getBroadcast(this, 0, sendIntent, 0);
        Intent deliverIntent = new Intent(ACTION_DELIVER_MSG);
        mDeliverPi = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);

        registerReceiver(mSendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mSendMsgCount++;
                if (getResultCode() == RESULT_OK) {
                    Log.e("TAG", "短信发送成功" + (mSendMsgCount + "/" + mTotalCount));
                } else {
                    Log.e("TAG", "短信发送失败");
                }
                Toast.makeText(SendMsgActivity.this, (mSendMsgCount + "/" + mTotalCount) + "短信发送成功", Toast.LENGTH_SHORT).show();
                if (mSendMsgCount == mTotalCount) {
                    finish();
                }
            }
        }, new IntentFilter(ACTION_SEND_MSG));

        registerReceiver(mDeliverBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mDeliverMsgCount++;
                Log.e("TAG", "短信已被联系人接收");
                Toast.makeText(SendMsgActivity.this, (mDeliverMsgCount + "/" + mTotalCount) + "短信已被联系人接收", Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(ACTION_DELIVER_MSG));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSendBroadcastReceiver);
        unregisterReceiver(mDeliverBroadcastReceiver);
    }

    private void initViews() {
        mEtMsg = (EditText) findViewById(R.id.id_et_content);
        mBtnAdd = (Button) findViewById(R.id.id_btn_add);
        mFlContacts = (FlowLayout) findViewById(R.id.id_fl_contacts);
        mFabSend = (FloatingActionButton) findViewById(R.id.id_fab_send);
        mLayoutLoading = (FrameLayout) findViewById(R.id.id_layout_loading);
        mLayoutLoading.setVisibility(View.GONE);
    }

    private void initDatas() {
        mFestivalId = getIntent().getIntExtra(KEY_FESTIVAL_ID, -1);
        msgId = getIntent().getIntExtra(KEY_MSG_ID, -1);
        if (msgId != -1) {
            mMsg = FestivalLab.getInstance().getMsgById(mFestivalId, msgId);
            mEtMsg.setText(mMsg.getContent());
        }
        mFestival = FestivalLab.getInstance().getFestivalById(mFestivalId);
        setTitle(mFestival.getName());

    }

    private void initEvents() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动系统的通讯录app，用startActivityForResult,在通讯录活动页面销毁前会回调onActivityResult方法
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CODE_REQUEST);
            }
        });

        mFabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = mEtMsg.getText().toString();
                if (mContactNums.size() == 0) {
                    Toast.makeText(SendMsgActivity.this, "请先选择联系人", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(SendMsgActivity.this, "短信内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mLayoutLoading.setVisibility(View.VISIBLE);
                mTotalCount = mSmsBiz.sendMsg(mContactNums, buildSendMsg(msg), mSendPi, mDeliverPi);
                mSendMsgCount = 0;
            }
        });
    }

    private SendedMsg buildSendMsg(String msg) {
        SendedMsg sendedMsg = new SendedMsg();
        sendedMsg.setMsg(msg);
        sendedMsg.setFestivalName(mFestival.getName());
        String names = "";
        for (String name : mContactNames) {
            names += name + ":";
        }
        sendedMsg.setNames(names.substring(0, names.length() - 1));
        String numbers = "";
        for (String number : mContactNums) {
            numbers += number + ":";
        }
        sendedMsg.setNumbers(numbers.substring(0, numbers.length() - 1));
        return sendedMsg;
    }

    @Override //得到intent访问系统通讯录app返回的联系人姓名和号码
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST) {
            if (RESULT_OK == resultCode) {
                Uri contactUri = data.getData();
                Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                cursor.moveToFirst();
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactNumber = getContactNumber(cursor);
                if (!TextUtils.isEmpty(contactNumber)) {
                    mContactNums.add(contactNumber);
                    mContactNames.add(contactName);
                    addTag(contactName);
                }
            }
        }
    }

    private void addTag(String contactName) {
        TextView view = (TextView) mInflater.inflate(R.layout.tag, mFlContacts, false);
        view.setText(contactName);
        mFlContacts.addView(view);
    }

    private String getContactNumber(Cursor cursor) {
        int numCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        String number = null;
        if (numCount > 0) {
            int contactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, null, null);
            phoneCursor.moveToFirst();
            number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneCursor.close();
        }
        cursor.close();
        return number;
    }

    //把跳转活动的方法写到目标活动里面，传递参数时不易搞错，值得借鉴。
    public static void toActivity(Context context, int fesrivalId, int msgId) {
        Intent intent = new Intent(context, SendMsgActivity.class);
        intent.putExtra(KEY_FESTIVAL_ID, fesrivalId);
        intent.putExtra(KEY_MSG_ID, msgId);
        context.startActivity(intent);
    }

}
