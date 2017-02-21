package com.example.sendMessage;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sendMessage.bean.FestivalLab;
import com.example.sendMessage.bean.Msg;
import com.example.sendMessage.fragment.Fragment_festival_category;
import com.imooc.sendMessage.R;



public class ChooseMsgActivity extends AppCompatActivity {

    private ListView mLvMsgs;
    private FloatingActionButton mFabTosend;
    private ArrayAdapter<Msg> mAdapter;
    private int mId_festival;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_msg);
        //得到fragment里girdview点击事件中传递过来的festival的id（用intent传递数据）
        mId_festival = getIntent().getIntExtra(Fragment_festival_category.ID_FESTIVAL, -1);
        mInflater = LayoutInflater.from(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        mFabTosend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMsgActivity.toActivity(ChooseMsgActivity.this, mId_festival, -1);
            }
        });
    }

    private void initView() {
        mLvMsgs = (ListView) findViewById(R.id.id_lv_msg);
        mFabTosend = (FloatingActionButton) findViewById(R.id.id_fab_toSend);

        //ListView的适配器，用来给ListView添加List<Msg>的选项，包括选项的布局、控件及显示内容
        mAdapter = new ArrayAdapter<Msg>(this, -1, FestivalLab.getInstance().getMsgsByfesId(mId_festival)) {
            @NonNull
            @Override //在每个子项被滚动到屏幕内的时候会被调用
            public View getView(final int position, View convertView, ViewGroup parent) {
                Msg msg = getItem(position);// 获取当前项的Msg实例
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_lv_msgcontent, parent, false);
                }
                TextView content = (TextView) convertView.findViewById(R.id.id_tv_msgcontent);
                Button toSend = (Button) convertView.findViewById(R.id.id_btn_tosend);
                content.setText(msg.getContent());

                toSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SendMsgActivity.toActivity(ChooseMsgActivity.this, mId_festival, getItem(position).getId());
                    }
                });
                return convertView;
            }
        };
        mLvMsgs.setAdapter(mAdapter);
    }

}
