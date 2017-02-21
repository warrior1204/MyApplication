package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

 class MsgAdapter extends ArrayAdapter<Msg> {
    private int resourceId;

     MsgAdapter(Context context, int textViewResourceId,
                      List<Msg> objects) {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override //在每个子项被滚动到屏幕内的时候会被调用
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Msg msg = getItem(position);//获取当前项的Msg实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (LinearLayout) view.findViewById
                    (R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout) view.findViewById
                    (R.id.right_layout);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
            view.setTag(viewHolder);//将viewHolder存储在view中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取viewHolder
        }

        if ((msg != null ? msg.getType() : 0) == Msg.TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg != null ? msg.getContent() : null);
        } else if (msg.getType() == Msg.TYPE_SENT) {
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMsg.setText(msg.getContent());
        }
        return view;

    }

    private class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
    }

}