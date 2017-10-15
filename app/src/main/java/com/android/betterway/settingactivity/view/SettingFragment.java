package com.android.betterway.settingactivity.view;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;


import com.android.betterway.R;
import com.android.betterway.settingactivity.daggerneed.DaggerSettingFragmentComponent;
import com.android.betterway.settingactivity.daggerneed.SettingFragmentComponent;
import com.android.betterway.settingactivity.daggerneed.SettingFragmentModule;
import com.android.betterway.settingactivity.presenter.SettingPresenterImpel;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置界面碎片
 */
public class SettingFragment extends PreferenceFragment implements SettingView, Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {

    private static final int IMAGE_ROUTE = 1;
    private static final String TAG = "SettingFragment";
    private static final String PREFERENCE = "Setting";
    private static final int VERSION = 19;
    private SettingPresenterImpel mSettingPresenterImpel;
    SwitchPreference useDefault;
    SwitchPreference useLocal;
    SwitchPreference useOnline;
    Preference imageLocation;
    ListPreference warnDuration;
    ListPreference updateDuration;
    private List<SoftReference<Activity>>  mSoftReferenceList = new ArrayList<SoftReference<Activity>>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        useDefault = (SwitchPreference) findPreference("use_default_image");
        useLocal = (SwitchPreference) findPreference("use_local_image");
        useOnline = (SwitchPreference) findPreference("update_image_online");
        imageLocation = findPreference("local_image_location");
        warnDuration = (ListPreference) findPreference("warning_time");
        updateDuration = (ListPreference) findPreference("update_duration");
        useDefault.setOnPreferenceClickListener(this);
        useLocal.setOnPreferenceClickListener(this);
        useOnline.setOnPreferenceClickListener(this);
        imageLocation.setOnPreferenceClickListener(this);
        warnDuration.setOnPreferenceChangeListener(this);
        updateDuration.setOnPreferenceChangeListener(this);
        warnDuration.setSummary(warnDuration.getEntry());
        updateDuration.setSummary(updateDuration.getEntry());
        SettingFragmentComponent settingFragmentComponent = DaggerSettingFragmentComponent.builder()
                .settingFragmentModule(new SettingFragmentModule(this))
                .build();
        settingFragmentComponent.inject(this);
        mSettingPresenterImpel = settingFragmentComponent.getSettingPresenterImpel();
        initData();
    }
    /**
     * 初始化summary
     */
    private void initData() {
        LogUtil.v(TAG, "iniData");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                String data = mSettingPresenterImpel.getStringData("Image_path");
                e.onNext(data);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        imageLocation.setSummary(s);
                    }
                });
    }

    @Override
    public Activity getSettingActivity() {
        if (mSoftReferenceList.size() == 0) {
            SoftReference<Activity> softReference = new SoftReference<>(getActivity());
            mSoftReferenceList.add(softReference);
            return softReference.get();
        } else {
            SoftReference<Activity> softReference = mSoftReferenceList.get(0);
            return softReference.get();
        }

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "use_default_image":
                if (useDefault.isChecked()) {
                    useOnline.setChecked(false);
                    useLocal.setChecked(false);
                }
                break;
            case "use_local_image":
                if (useLocal.isChecked()) {
                    useDefault.setChecked(false);
                    useOnline.setChecked(false);
                }
                break;
            case "update_image_online":
                if (useOnline.isChecked()) {
                    useDefault.setChecked(false);
                    useLocal.setChecked(false);
                }
                break;
            case "local_image_location":
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    // 用户已经同意该权限
                                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                                    intent.setType("image/*");
                                    startActivityForResult(intent, IMAGE_ROUTE);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // 用户拒绝了该权限，那么下次再次启动时，还会提示请求权限的对话框
                                    ToastUtil.show(getContext(), "您已拒绝打开相册，无法选择图片");
                                } else {
                                    // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                                    ToastUtil.show(getContext(), "权限被拒绝，请在设置里面开启相应权限，若无相应权限会影响使用");
                                }
                            }
                        });
            default:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (data == null) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                String imagePath = null;
                if (Build.VERSION.SDK_INT >= VERSION) {
                    imagePath = mSettingPresenterImpel.handleImageOnKitKat(data);
                } else {
                    imagePath = mSettingPresenterImpel.handleImageBeforeKitKat(data);
                }
                mSettingPresenterImpel.putStringData("Image_path", imagePath);
                e.onNext(imagePath);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        imageLocation.setSummary(s);
                    }
                });
    }




    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof ListPreference) {
            //把preference这个Preference强制转化为ListPreference类型
            ListPreference listPreference = (ListPreference) preference;
            //获取ListPreference中的实体内容
            CharSequence[] entries = listPreference.getEntries();
            //获取ListPreference中的实体内容的下标值
            int index = listPreference.findIndexOfValue((String) newValue);
            //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
            listPreference.setSummary(entries[index]);
        }
        return true;
    }
}
