package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> mDate;
    private TextView tv_chat;
    private TextView tv_friend;
    private TextView tv_find;
    private BadgeView mBadgeView;
    private ImageView mTabline;
    private int mScreen1_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mTabline = (ImageView) findViewById(R.id.id_iv_tabline);

        initImageView_tabline();// 将imageview的tabline设置为屏幕的1/3
        init();
    }

    private void initImageView_tabline() {  // 将imageview的tabline设置为屏幕的1/3
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScreen1_3 = outMetrics.widthPixels/3;
        //重新设置控件布局，一般是宽和高
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline.getLayoutParams();
        lp.width = mScreen1_3;
        mTabline.setLayoutParams(lp);
    }

    private void init() {
        tv_chat = (TextView) findViewById(R.id.tv_chat);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        tv_find = (TextView) findViewById(R.id.tv_find);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mDate = new ArrayList<>();
        Fragment_ChatTab tab1 = new Fragment_ChatTab();
        Fragment_FriendTab tab2 = new Fragment_FriendTab();
        Fragment_FindTab tab3 = new Fragment_FindTab();
        mDate.add(tab1);
        mDate.add(tab2);
        mDate.add(tab3);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int args0) {
                return mDate.get(args0);
            }
            @Override
            public int getCount() {
                return mDate.size();
            }
        };
        mViewPager.setAdapter(mAdapter);

        //viewPager页面滑动时调用此监视器的对应方法，界面切换时实现一些效果
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override //页面在滑动时调用，args0表示当前页面，即你点击滑动的页面；args1表示当前页面偏移的百分比；args2表示当前页面-偏移的像素位置
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { //指示器tabline下划线跟随滑动
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline.getLayoutParams();
                lp.leftMargin = (int) (position*mScreen1_3+positionOffset*mScreen1_3);
                mTabline.setLayoutParams(lp);
            }
            @Override //页面跳转完后得到调用，position为当前选中的页面，即跳转完以后的页面
            public void onPageSelected(int position) { //先将界面切换时字体颜色全部设置为黑色，当滑动到某个页面停留时就将对应字体设置为绿色
                resetTextColor();                      //用badgeview实现消息数目提醒
                int green = ContextCompat.getColor(MainActivity.this, R.color.colorGreen);//法1：自定义颜色：在class里调用colors.xml文件的颜色
                switch (position) {
                    case 0:
                        tv_chat.setTextColor(green);
                        mBadgeView = new BadgeView(MainActivity.this);
                        mBadgeView.setTargetView(tv_chat);
                        mBadgeView.setBadgeCount(3);
                        break;
                    case 1:
                        tv_friend.setTextColor(Color.parseColor("#008000"));//法2(better)：自定义颜色，和法1的效果一模一样
                        mBadgeView = new BadgeView(MainActivity.this);
                        mBadgeView.setTargetView(tv_friend);
                        mBadgeView.setText("帅");
                        mBadgeView.setBadgeGravity(Gravity.CENTER|Gravity.END);
                        break;
                    case 2:
                        tv_find.setTextColor(green);
                        mBadgeView = new BadgeView(MainActivity.this);
                        mBadgeView.setTargetView(tv_find);
                        mBadgeView.setText("+5");
                        mBadgeView.setBackgroundColor(Color.parseColor("#008000"));
                        mBadgeView.setBadgeGravity(Gravity.CENTER|Gravity.END);
                        break;
                }
            }
            @Override //状态改变时调用，state有三种状态（0,1,2），1表示正在滑动，2表示滑动完毕，0表示什么都没做
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void resetTextColor() {
        tv_chat.setTextColor(Color.BLACK);
        tv_friend.setTextColor(Color.BLACK);
        tv_find.setTextColor(Color.BLACK);
    }
}
