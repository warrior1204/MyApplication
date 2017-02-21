package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView msgListView;
    private EditText inputText;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = (EditText) findViewById(R.id.input_text);
        Button send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);

        initMsgs();
        adapter = new MsgAdapter(MainActivity.this,R.layout.msg, msgList);
        msgListView.setAdapter(adapter);

		/*--------------给listview添加点击事件-----------------*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();//当有新消息时，刷新listView中的显示
                    msgListView.setSelection(msgList.size());//将listView定位到最后一行
                    inputText.setText("");//清空输入框中的内容
                }
            }
        });


    }

    private void initMsgs() {
        Msg msg1 = new Msg("Hello guy.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello shabi",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("FUCK!",Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
}
