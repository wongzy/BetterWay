package com.android.betterway.settingactivity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;


import com.android.betterway.R;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;


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
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {

    private static final int IMAGE_ROUTE = 1;
    private static final String TAG = "SettingFragment";
    private static final int VERSION = 19;
    SwitchPreference useDefault;
    SwitchPreference useLocal;
    SwitchPreference useOnline;
    Preference imageLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        useDefault = (SwitchPreference) findPreference("use_default_image");
        useLocal = (SwitchPreference) findPreference("use_local_image");
        useOnline = (SwitchPreference) findPreference("update_image_online");
        imageLocation = (Preference) findPreference("local_image_location");
        useDefault.setOnPreferenceClickListener(this);
        useLocal.setOnPreferenceClickListener(this);
        useOnline.setOnPreferenceClickListener(this);
        imageLocation.setOnPreferenceClickListener(this);
        initData();
    }

    /**
     * 初始化summary
     */
    private void initData() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                SharedPreferences data = getPreferenceManager().getSharedPreferences();
                String imagePath = data.getString("Image_path", "无");
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
                    imagePath = handleImageOnKitKat(data);
                } else {
                    imagePath = handleImageBeforeKitKat(data);
                }
                SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                editor.putString("Image_path", imagePath);
                editor.apply();
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

    /**
     * api19及之后的处理方法
     * @param data 传入的数据
     * @return 图片路径
     */
    @TargetApi(19)
    private String handleImageOnKitKat(Intent data) {
        LogUtil.d(TAG, "handleImageOnkitKat");
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity().getApplicationContext(), uri)) {
            //如果是document类型的Uri，则通过documentid处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + '=' + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contenUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contenUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    /**
     * 在api19前的处理方法
     * @param data 传入的数据
     * @return 图片路径
     */
    private String handleImageBeforeKitKat(Intent data) {
        LogUtil.d(TAG, "handleImagebeforeKitKat");
        Uri uri = data.getData();
        return getImagePath(uri, null);
    }

    /**
     * 获得图片的存储路径
     * @param uri 返回的uri
     * @param selection 类型
     * @return 图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        LogUtil.d(TAG, "getImagePath");
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
