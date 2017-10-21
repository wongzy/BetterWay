package com.android.betterway.AutoScheduleActivity.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.betterway.R;

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
    }
    private void init() {
        setSupportActionBar(mToolbarAutoSchedule);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
