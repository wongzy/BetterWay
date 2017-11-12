package com.android.betterway.autoscheduleactivity.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.betterway.R;
import com.android.betterway.other.DeadMessage;
import com.android.betterway.utils.NetworkUtil;
import com.android.betterway.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自动生成路径的类
 */
public class AutoScheduleActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_auto_schedule)
    Toolbar mToolbarAutoSchedule;
    private int type;
    private long datelong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_schedule);
        ButterKnife.bind(this);
        init();
        NetworkUtil.isNetworkConnected(getApplicationContext());
    }

    /**
     * 初始化
     */
    private void init() {
        setSupportActionBar(mToolbarAutoSchedule);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        datelong = intent.getLongExtra("datelong", 20171114);
        type = intent.getIntExtra("type", 2);
        mToolbarAutoSchedule.setSubtitle(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.finish_auto:
                AutoScheduleMainFragment autoScheduleMainFragment = (AutoScheduleMainFragment)
                        getSupportFragmentManager().findFragmentById(R.id.main_fragment);
                autoScheduleMainFragment.setType(type);
                autoScheduleMainFragment.setDatelong(datelong);
                autoScheduleMainFragment.finishPlan();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_auto_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeadMessgeEvent(DeadMessage message) {
        if (message == DeadMessage.FINISH) {
            finish();
        }
    }
}
