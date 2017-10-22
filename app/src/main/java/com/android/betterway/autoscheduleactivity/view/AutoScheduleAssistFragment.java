package com.android.betterway.autoscheduleactivity.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.android.betterway.R;
import com.android.betterway.data.LocationBean;
import com.android.betterway.utils.JsonUtil;
import com.android.betterway.utils.LogUtil;
import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutoScheduleAssistFragment extends Fragment {
    private static final String TAG = "AutoScheduleAssistFragment";
    AMap aMap;
    @BindView(R.id.location_text)
    TextView mLocationText;
    @BindColor(R.color.accent)
    int accent;
    @BindColor(R.color.primary)
    int primary;
    Unbinder unbinder;
    @BindView(R.id.mapview)
    MapView mMapview;
    private ArrayList<LocationBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initJson();
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mMapview.getMap();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auto_schedule_assist, container, false);
        mMapview.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initMap();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 按钮点击时间
     */
    @OnClick(R.id.location_text)
    public void onViewClicked() {
        showPickerView();
    }

    /**
     * 解析Json
     */
    private void initJson() {
        Observable.create(new ObservableOnSubscribe<ArrayList<LocationBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<LocationBean>> e) throws Exception {
                ArrayList<LocationBean> locationBeanArrayList = JsonUtil.getJsonData(getContext(), "province.json");
                LogUtil.d(TAG, "initJson subser");
                e.onNext(locationBeanArrayList);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<ArrayList<LocationBean>>() {
                    @Override
                    public void accept(ArrayList<LocationBean> locationBeen) throws Exception {
                        options1Items = locationBeen;
                        for (int i = 0; i < locationBeen.size(); i++) { //遍历省份
                            ArrayList<String> mCityList = new ArrayList<>(); //该省的城市列表（第二级）
                            for (int c = 0; c < locationBeen.get(i).getCity().size(); c++) { //遍历该省份的所有城市
                                String mCityName = locationBeen.get(i).getCity().get(c).getName();
                                mCityList.add(mCityName); //添加城市
                            }
                            options2Items.add(mCityList);
                        }
                        LogUtil.d(TAG, "initJson finish");
                    }
                });
    }

    /**
     * 弹出地点选择器
     */
    private void showPickerView() { // 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(
                getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(options2);
                mLocationText.setText(tx);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("请选择地点")//标题
                .setTitleColor(primary)//标题文字颜色
                .setSubmitColor(accent)//确定按钮文字颜色
                .setCancelColor(accent)//取消按钮文字颜色
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvOptions.setPicker(options1Items, options2Items); //二级选择器
        pvOptions.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapview.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapview.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapview.onDestroy();
    }
}
