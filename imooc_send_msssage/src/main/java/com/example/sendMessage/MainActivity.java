package com.example.sendMessage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.sendMessage.fragment.Fragment_festival_category;
import com.example.sendMessage.fragment.SmsHistoryFragment;
import com.imooc.sendMessage.R;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private String mTitles[] = new String[]{"节日祝福 ", "发送记录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mTablayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 1) {
                    return new SmsHistoryFragment();
                }
                return new Fragment_festival_category();
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override //显示tabLayout上的名字
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };
        mViewPager.setAdapter(mAdapter);
        mTablayout.setupWithViewPager(mViewPager);
    }
}
