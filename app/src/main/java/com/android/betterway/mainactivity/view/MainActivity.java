package com.android.betterway.mainactivity.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.betterway.R;
import com.android.betterway.mainactivity.daggerneed.DaggerMainActivityComponent;
import com.android.betterway.mainactivity.daggerneed.MainPresenterImpelModule;
import com.android.betterway.mainactivity.presenter.MainPresenterImpel;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ToastUtil;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = "MainActivity";


    @Inject
    MainPresenterImpel mMainPresenterImpel;

    @BindView(R.id.app_bar_image)
    ImageView mAppBarImage;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.addAutoSchedule)
    FloatingActionButton mAddAutoSchedule;
    @BindView(R.id.addNormalSchedule)
    FloatingActionButton mAddNormalSchedule;
    @BindView(R.id.blur_fragment)
    FrameLayout mBlurFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.v(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DaggerMainActivityComponent.builder()
                .mainPresenterImpelModule(new MainPresenterImpelModule(this))
                .build()
                .inject(this);
        init();
    }

    /**
     * 初始化菜单等
     */
    private void init() {
        LogUtil.d(TAG, "init");
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogUtil.v(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogUtil.v(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.setting:
                LogUtil.d(TAG, "click setting");
                mMainPresenterImpel.getSet();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void enterSchedule() {

    }

    @Override
    public void notifyRecycler() {

    }

    @Override
    public void updateImage() {

    }

    /**
     * 点击事件
     * @param view 传递点击事件的view
     */
    @OnClick({R.id.recyclerview, R.id.addAutoSchedule, R.id.addNormalSchedule})
    public void onViewClicked(View view) {
        LogUtil.v(TAG, "onViewClicked");
        switch (view.getId()) {
            case R.id.addAutoSchedule:
                LogUtil.d(TAG, "click addAutoSchedule");
                break;
            case R.id.addNormalSchedule:
                LogUtil.d(TAG, "click addNormalSchedule");
                mMainPresenterImpel.addNormalSchedule();
                break;
            default:
                break;
        }
    }

}
