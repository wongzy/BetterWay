package com.android.betterway.showscheduleactivity.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.betterway.R;
import com.android.betterway.data.MyDate;
import com.android.betterway.utils.TimeUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowScheduleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_show_schedule)
    Toolbar mToolbarShowSchedule;
    @BindView(R.id.tabs_layout)
    TabLayout mTabsLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

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
        long date = intent.getLongExtra("datelong", 20171111);
        MyDate myDate = TimeUtil.intToMyDate((int)date);
        String city = intent.getStringExtra("city");
        mToolbarShowSchedule.setSubtitle(myDate.toSingleString() + "(" + city + ")");
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
}
