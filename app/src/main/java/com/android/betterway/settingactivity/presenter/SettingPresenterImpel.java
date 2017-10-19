package com.android.betterway.settingactivity.presenter;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.Preference;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.android.betterway.settingactivity.model.SettingModel;
import com.android.betterway.settingactivity.view.SettingView;
import com.android.betterway.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import dagger.Module;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class SettingPresenterImpel implements SettingPresenter {
    private SettingView mSettingView;
    private final static String TAG = "SettingPresenterImpel";
    @Inject
    public SettingPresenterImpel(SettingView settingView) {
        mSettingView = settingView;
    }

    @Override
    public String handleImageBeforeKitKat(Intent data) {
        LogUtil.d(TAG, "handleImagebeforeKitKat");
        Uri uri = data.getData();
        return getImagePath(uri, null);
    }

    @Override
    public String handleImageOnKitKat(Intent data){
        LogUtil.d(TAG, "handleImageOnkitKat");
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(mSettingView.getSettingActivity().getApplicationContext(), uri)) {
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

    @Override
    public String getImagePath(Uri uri, String selection) {
        LogUtil.d(TAG, "getImagePath");
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = mSettingView.getSettingActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public String getStringData(String key) {
        SettingModel mSettingModel = SettingModel.getInstance();
        return mSettingModel.getStringData(mSettingView.getSettingActivity(), key);
    }

    @Override
    public void putStringData(final String key, final String data) {
        SettingModel mSettingModel = SettingModel.getInstance();
        mSettingModel.putStringData(mSettingView.getSettingActivity(), key, data);
    }
}
