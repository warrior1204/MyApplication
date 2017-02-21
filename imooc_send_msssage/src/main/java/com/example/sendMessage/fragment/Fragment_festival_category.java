package com.example.sendMessage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sendMessage.ChooseMsgActivity;
import com.example.sendMessage.bean.Festival;
import com.example.sendMessage.bean.FestivalLab;
import com.imooc.sendMessage.R;


public class Fragment_festival_category extends Fragment {
    private GridView mGridview;
    private ArrayAdapter<Festival> mAdapter;
    private LayoutInflater mInflater;
    public static final String ID_FESTIVAL = "festival_id";

    @Nullable
    @Override //创建该fragment的视图，返回fragment要显示的view，生命周期中的一个
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festival_category, container, false);//加载布局文件
    }

    @Override  //在oncreateview执行完后立即执行，是一个普通方法
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(getActivity());
        mGridview = (GridView) view.findViewById(R.id.id_gv_festival);

        //GridView的适配器，用来给GridView添加List<Festival>的选项，包括选项的布局、控件及显示内容
        mAdapter = new ArrayAdapter<Festival>(getActivity(), -1, FestivalLab.getInstance().getFestivals()) {
            @NonNull
            @Override //在每个子项被滚动到屏幕内的时候会被调用
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                Festival fes = getItem(position);// 获取当前项的Festival实例
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_gv_festival, parent, false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.id_tv_festival_name);
                tv.setText(fes.getName());
                return convertView;
            }
        };
        mGridview.setAdapter(mAdapter);

        // mGridview的点击事件，点击时跳转到活动ChooseMsgActivity，并传递该festival的id给ChooseMsgActivity
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), ChooseMsgActivity.class);
                intent.putExtra(ID_FESTIVAL, mAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
    }


}
