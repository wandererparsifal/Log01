package com.parsifal.log01.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.parsifal.log01.R;
import com.parsifal.log01.ui.adapter.ViewPagerAdapter;
import com.parsifal.log01.ui.fragment.FragmentHome;
import com.parsifal.log01.ui.fragment.FragmentWork;
import com.parsifal.log01.view.StatisticsView;

import java.util.ArrayList;

/**
 * Created by YangMing on 2016/8/24 15:16.
 */
public class StatisticsActivity extends AppCompatActivity implements StatisticsView {

    private ViewPager mViewPager = null;

    private ViewPagerAdapter mAdapter = null;

    private LinearLayout mLayoutDots = null;

    private Button mPreSelectedBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mLayoutDots = (LinearLayout) findViewById(R.id.layout_dots);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        final ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentWork());
        fragments.add(new FragmentHome());
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mPreSelectedBtn.setBackgroundResource(R.drawable.white_dot);
                Button currentBt = (Button) mLayoutDots.getChildAt(position);
                currentBt.setBackgroundResource(R.drawable.blue_dot);
                mPreSelectedBtn = currentBt;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mLayoutDots.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = mLayoutDots.getHeight();
                for (int i = 0; i < fragments.size(); i++) {
                    Button bt = new Button(StatisticsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height, height);
                    layoutParams.leftMargin = height / 4;
                    layoutParams.rightMargin = height / 4;
                    bt.setLayoutParams(layoutParams);
                    bt.setBackgroundResource(R.drawable.white_dot);
                    mLayoutDots.addView(bt);
                }
                mPreSelectedBtn = (Button) mLayoutDots.getChildAt(0);
                mPreSelectedBtn.setBackgroundResource(R.drawable.blue_dot);
                mLayoutDots.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        mViewPager.setCurrentItem(0);
    }
}
