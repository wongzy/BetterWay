package com.android.betterway.autoscheduleactivity.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.betterway.R;
import com.android.betterway.utils.NetworkUtil;
import com.android.betterway.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AutoScheduleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_auto_schedule)
    Toolbar mToolbarAutoSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_schedule);
        ButterKnife.bind(this);
        init();
        NetworkUtil.isNetworkConnected(getApplicationContext());
    }

    private void init() {
        setSupportActionBar(mToolbarAutoSchedule);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        mToolbarAutoSchedule.setSubtitle(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.finish_auto:
                ToastUtil.show(getApplicationContext(), "Clicked finish");
                break;
            case R.id.clean_all:
                ToastUtil.show(getApplicationContext(), "Clicked clean all");
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
}
