package com.android.betterway.mainactivity.view;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.android.betterway.myview.FloatingActionButtonMenu;
import com.android.betterway.other.ButtonSwith;
import com.android.betterway.utils.BitmapCompress;
import com.android.betterway.utils.BlurUtil;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ScreenShotUtil;
import com.android.betterway.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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
    @BindView(R.id.floatingActionButtonMenu)
    FloatingActionButtonMenu mFloatingActionButtonMenu;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
     *
     * @param view 传递点击事件的view
     */
    @OnClick({R.id.addAutoSchedule, R.id.addNormalSchedule})
    public void onViewClicked(View view) {
        LogUtil.v(TAG, "onViewClicked");
        switch (view.getId()) {
            case R.id.addAutoSchedule:
                LogUtil.d(TAG, "click addAutoSchedule");
                mMainPresenterImpel.addAutoSchedule();
                break;
            case R.id.addNormalSchedule:
                LogUtil.d(TAG, "click addNormalSchedule");
                mMainPresenterImpel.addNormalSchedule();
                break;
            default:
                break;
        }
    }

    /**
     * EventBus接受事件
     * @param buttonSwith 获取开源状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onButtonSwithHandle(ButtonSwith buttonSwith) {
        showBlurBackground(buttonSwith);
    }

    /**
     * 对所在界面截图
     * @param buttonSwith 接收的开关状态
     */
    private void showBlurBackground(ButtonSwith buttonSwith) {
        if (buttonSwith == ButtonSwith.OPEN) {
            final WeakReference reference = new WeakReference(this);
            Observable.create(new ObservableOnSubscribe<Drawable>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Drawable> e) throws Exception {
                    Bitmap compressedBitmap = BitmapCompress
                            .compressScale(ScreenShotUtil.shot((Activity) reference.get()));
                    Bitmap blurBitmap = BlurUtil.blur(compressedBitmap);
                    Drawable drawable = new BitmapDrawable(blurBitmap);
                    e.onNext(drawable);
                    e.onComplete();
                    compressedBitmap = null;
                    blurBitmap = null;
                    drawable = null;
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Drawable>() {
                        @Override
                        public void accept(Drawable drawable) throws Exception {
                            mBlurFragment.setBackground(drawable);
                            ToastUtil.show(getApplicationContext(), "blurbackground worked");
                        }
                    });
        } else {
            mBlurFragment.setBackground(null);
        }
    }
}
