package com.android.betterway.mainactivity.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.betterway.AutoScheduleActivity.view.AutoScheduleActivity;
import com.android.betterway.R;
import com.android.betterway.mainactivity.daggerneed.DaggerMainActivityComponent;
import com.android.betterway.mainactivity.daggerneed.MainActivityComponent;
import com.android.betterway.mainactivity.daggerneed.MainPresenterImpelModule;
import com.android.betterway.mainactivity.datepicker.DatePickerFragment;
import com.android.betterway.mainactivity.presenter.MainPresenterImpel;
import com.android.betterway.myview.FloatingActionButtonMenu;
import com.android.betterway.other.ActivityType;
import com.android.betterway.other.ButtonSwith;
import com.android.betterway.utils.StatusBarCompat;
import com.android.betterway.utils.BitmapCompress;
import com.android.betterway.utils.BlurUtil;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ScreenShotUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

import butterknife.BindColor;
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
public class MainActivity extends AppCompatActivity implements MainView, View.OnTouchListener {

    private static final String TAG = "MainActivity";
    private MainPresenterImpel mMainPresenterImpel;
    private List<WeakReference<Activity>> mWeakReferenceList = new ArrayList<WeakReference<Activity>>();
    @BindColor(R.color.primary)
    int primaryColor;
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
    @BindView(R.id.app_bar_image)
    ImageView mAppBarImage;
    @BindView(R.id.notification_background)
    CollapsingToolbarLayout mNotificationBackground;

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
        MainActivityComponent mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainPresenterImpelModule(new MainPresenterImpelModule(this))
                .build();
        mainActivityComponent.inject(this);
        mMainPresenterImpel = mainActivityComponent.getMainPresenterImpel();
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化菜单等
     */
    private void init() {
        LogUtil.d(TAG, "init");
        setSupportActionBar(mToolbar);
        mBlurFragment.setOnTouchListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogUtil.v(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mBlurFragment.getBackground() == null) {
            return false;
        }
        closeBlurBackground();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public Activity getActivity() {
        if (mWeakReferenceList.size() == 0) {
            WeakReference<Activity> weakReference = new WeakReference<Activity>(this);
            mWeakReferenceList.add(weakReference);
            return weakReference.get();
        } else {
            WeakReference<Activity> weakReference = mWeakReferenceList.get(0);
            LogUtil.v(TAG, "list woeked!");
            return weakReference.get();
        }
    }

    @Override
    public void enterSchedule() {

    }

    @Override
    public void notifyRecycler() {

    }

    @Override
    public void updateImage() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext(mMainPresenterImpel.getUrl());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        switch (s) {
                            case "DEFAULT":
                                Glide.with(getApplicationContext()).load(R.drawable.bg).into(mAppBarImage);
                                setThemeColor(primaryColor);
                                break;
                            case "NONE":
                                Glide.with(getApplicationContext()).load(null).into(mAppBarImage);
                                setThemeColor(primaryColor);
                                break;
                            default:
                                Glide.with(getApplicationContext()).asBitmap().load(s)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(final Bitmap resource,
                                                                        final Transition<? super Bitmap> transition) {
                                                mAppBarImage.setImageBitmap(resource);
                                                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                                    @Override
                                                    public void onGenerated(Palette palette) {
                                                        Palette.Swatch swatch = palette.getMutedSwatch();
                                                        int color;
                                                        if (swatch != null) {
                                                            color = swatch.getRgb();
                                                        } else {
                                                            color = primaryColor;
                                                        }
                                                        setThemeColor(color);
                                                    }
                                                });
                                            }
                                        });
                                break;
                        }
                    }
                });
    }

    /**
     * 点击事件
     * @param view 传递点击事件的view
     */
    @OnClick({R.id.addAutoSchedule, R.id.addNormalSchedule})
    public void onViewClicked(View view) {
        LogUtil.v(TAG, "onViewClicked");
        switch (view.getId()) {
            case R.id.addAutoSchedule:
                LogUtil.d(TAG, "click addAutoSchedule");
                showDatePickerDialog(ActivityType.ADDAUTOACTIVITY);
                closeBlurBackground();
                break;
            case R.id.addNormalSchedule:
                LogUtil.d(TAG, "click addNormalSchedule");
                showDatePickerDialog(ActivityType.ADDMORNALACTIVITY);
                closeBlurBackground();
                break;
            default:
                break;
        }
    }

    /**
     * EventBus接受事件
     * @param buttonSwith 获取开关状态
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
            Observable.create(new ObservableOnSubscribe<Drawable>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Drawable> e) throws Exception {
                    Bitmap compressedBitmap = BitmapCompress
                            .compressScale(ScreenShotUtil.shot(getActivity()));
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
                            LogUtil.d(TAG, "blur worked");
                        }
                    });
        } else {
            mBlurFragment.setBackground(null);
            LogUtil.d(TAG, "blur free");
        }
    }

    /**
     * 设置当前活动的主题色
     * @param mColor 要设置的颜色
     */
    private void setThemeColor(int mColor) {
        mNotificationBackground.setContentScrimColor(mColor);
        StatusBarCompat.compat(MainActivity.this, mColor);
    }

    /**
     * 取消毛玻璃特效背景
     */
    private void closeBlurBackground() {
        mBlurFragment.setBackground(null);
        mFloatingActionButtonMenu.closeMenu();
    }

    private void showDatePickerDialog(ActivityType activityType) {
        FragmentManager manager = getSupportFragmentManager();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setActitityType(activityType);
        datePickerFragment.show(manager, "DatePickerFragment");
    }
}
