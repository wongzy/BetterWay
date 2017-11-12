package com.android.betterway.showscheduleactivity.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.android.betterway.R;
import com.android.betterway.data.MyDate;
import com.android.betterway.data.NewPlan;
import com.android.betterway.showscheduleactivity.Impel.ShowScheduleImpel;
import com.android.betterway.showscheduleactivity.daggerneed.DaggerShowScheduleImpelCompont;
import com.android.betterway.showscheduleactivity.daggerneed.ShowScheduleImpelCompont;
import com.android.betterway.showscheduleactivity.daggerneed.ShowScheduleImpelModule;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowScheduleActivity extends AppCompatActivity implements ShowScheduleView,
        ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener{

    @BindView(R.id.toolbar_show_schedule)
    Toolbar mToolbarShowSchedule;
    @BindView(R.id.tabs_layout)
    TabLayout mTabsLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private long key;
    private ShowScheduleImpel mShowScheduleImpel;
    private ScheduleDetailFragment mScheduleDetailFragment;
    private ScheduleMapFragment mScheduleMapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_schedule);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        setSupportActionBar(mToolbarShowSchedule);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        ArrayList<NewPlan> newPlans = intent.getParcelableArrayListExtra("list");
        long date = intent.getLongExtra("datelong", 20171111);
        MyDate myDate = TimeUtil.intToMyDate((int)date);
        String city = intent.getStringExtra("city");
        mToolbarShowSchedule.setSubtitle(myDate.toSingleString() + "(" + city + ")");
        ShowScheduleImpelCompont showScheduleImpelCompont = DaggerShowScheduleImpelCompont.builder()
                .showScheduleImpelModule(new ShowScheduleImpelModule(this))
                .build();
        showScheduleImpelCompont.inject(this);
        mShowScheduleImpel = showScheduleImpelCompont.getShowScheduleImpel();
        mShowScheduleImpel.initData(newPlans);
        Bundle bundleDetail = new Bundle();
        bundleDetail.putParcelableArrayList("NewPlanList", mShowScheduleImpel.getNewPlanList());
        mScheduleDetailFragment = new ScheduleDetailFragment();
        mScheduleDetailFragment.setArguments(bundleDetail);
        Bundle bundleMap = new Bundle();
        bundleMap.putParcelableArrayList("latlnglist", mShowScheduleImpel.getLatLngList());
        mScheduleMapFragment = new ScheduleMapFragment();
        mScheduleMapFragment.setArguments(bundleMap);
        mViewpager.addOnPageChangeListener(this);
        mTabsLayout.addOnTabSelectedListener(this);
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return mScheduleDetailFragment;
                    case 1:
                        return mScheduleMapFragment;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_schedule_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                break;
            case R.id.delete:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public long getEditFinishTime() {
        LogUtil.i("ShowScheduleActivity", "key = " + key);
        return key;
    }

    @Override
    public Bitmap getScreenShot() {
        return null;
    }

    @Override
    public Context returnContext() {
        return getApplicationContext();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTabsLayout.getTabAt(position);
    }
    @Override
    public void onPageSelected(int position) {
        mTabsLayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
