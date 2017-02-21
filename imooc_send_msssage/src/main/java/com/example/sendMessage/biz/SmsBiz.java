package com.example.sendMessage.biz;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.SmsManager;

import com.example.sendMessage.bean.SendedMsg;
import com.example.sendMessage.db.SmsProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class SmsBiz {
    private static final String TAG = "SmsBiz";

    private Context context;

    public SmsBiz(Context context) {
        this.context = context;
    }

    //给单个联系人发送短信
    public int sendMsg(String phoneNumber, String msg, PendingIntent sentPi, PendingIntent deliverPi) {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> contents = smsManager.divideMessage(msg);//如果消息过长需要进行分割
        for (String content : contents) {
            //给单个phoneNumber发送单条短信content
            smsManager.sendTextMessage(phoneNumber, null, content, sentPi, deliverPi);
        }
        return contents.size();
    }

    //方法的重载，群发短信（给多个联系人发送短信）
    public int sendMsg(Set<String> numbers, SendedMsg sms, PendingIntent sentPi, PendingIntent deliverPi) {
        save(sms);
        int result = 0;
        for (String number : numbers) {
            int count = sendMsg(number, sms.getMsg(), sentPi, deliverPi);//调用自己的重载方法
            result += count;
        }
        return result;
    }

    private void save(SendedMsg sendedMsg) {
        sendedMsg.setDate(new Date());
        ContentValues values = new ContentValues();
        values.put(SendedMsg.COLUMN_DATE,sendedMsg.getDate().getTime());
        values.put(SendedMsg.COLUMN_FESNAME, sendedMsg.getFestivalName());
        values.put(SendedMsg.COLUMN_MSG, sendedMsg.getMsg());
        values.put(SendedMsg.COLUMN_NAME, sendedMsg.getNames());
        values.put(SendedMsg.COLUMN_NUMBERS, sendedMsg.getNumbers());

        context.getContentResolver().insert(SmsProvider.URI_SMS_ALL, values);
    }

}
