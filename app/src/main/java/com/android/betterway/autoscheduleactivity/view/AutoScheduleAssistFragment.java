package com.android.betterway.autoscheduleactivity.view;


import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.betterway.R;
import com.android.betterway.data.LocationBean;
import com.android.betterway.other.MapMarker;
import com.android.betterway.other.MarkerControl;
import com.android.betterway.utils.JsonUtil;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ToastUtil;
import com.bigkoo.pickerview.OptionsPickerView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutoScheduleAssistFragment extends Fragment implements AMap.OnMyLocationChangeListener,
        GeocodeSearch.OnGeocodeSearchListener {
    private static final String TAG = "AutoScheduleAssistFragment";
    private AMap aMap;
    private LatLng mLatLng;
    private List<Marker> mMarkerList = new ArrayList<Marker>();
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mMapview.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnMyLocationChangeListener(this);
        acquirePower();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auto_schedule_assist, container, false);
        unbinder = ButterKnife.bind(this, view);
        mMapview.onCreate(savedInstanceState);
        initMap();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapview.onDestroy();
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
                //返回的分别是两个级别的选中位置
                String city = options2Items.get(options1).get(options2);
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(options2);
                mLocationText.setText(tx);
                sendSearchLocation(city);
                doSearchGeo(tx, city);
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
    }

    /**
     * 请求定位权限
     */
    private void acquirePower() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            MyLocationStyle myLocationStyle;
                            myLocationStyle = new MyLocationStyle();
                            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                            myLocationStyle.showMyLocation(false);
                            aMap.setMyLocationStyle(myLocationStyle); //设置定位蓝点的Style
                            aMap.setMyLocationEnabled(true);
                            aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，那么下次再次启动时，还会提示请求权限的对话框
                            ToastUtil.show(getContext(), "您已拒绝定位申请，无法定位到您的位置");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                            ToastUtil.show(getContext(), "权限被拒绝，请在设置里面开启相应权限，否则将无法定位到您的位置");
                        }
                    }
                });
    }

    @Override
    public void onMyLocationChange(Location location) {
        LogUtil.e(TAG, "onMyLocationChange");
        GeocodeSearch geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch.setOnGeocodeSearchListener(this);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 10000, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        LogUtil.e(TAG, "onRegeocodeSearched");
        String getLocation = regeocodeResult.getRegeocodeAddress().getProvince()
                    + regeocodeResult.getRegeocodeAddress().getCity();
        sendSearchLocation(regeocodeResult.getRegeocodeAddress().getCity());
        mLocationText.setText(getLocation);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        LogUtil.e(TAG, "onGeocodeSearched");
        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
        LatLng latLng = new LatLng(geocodeAddress.getLatLonPoint().getLatitude(),
                geocodeAddress.getLatLonPoint().getLongitude());
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    /**
     * 由城市名查询地理位置
     * @param address 地址
     * @param name 城市名字
     */
    private void doSearchGeo(String address, String name) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch.setOnGeocodeSearchListener(this);
        GeocodeQuery query = new GeocodeQuery(address, name);
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 取得获得的坐标并移动地图到该坐标
     * @param latLonPoint 获得的坐标
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onLatLngPointEvent(LatLonPoint latLonPoint) {
        LogUtil.e(TAG, "moveToLatLngPoint()");
        mLatLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(mLatLng));
    }

    /**
     * 记录获得点的坐标
     * @param mapMarker 记录的信号
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onIntEvent(MapMarker mapMarker) {
        LogUtil.e(TAG, "addToMap()");
        if (mapMarker == MapMarker.ADD) {
            Marker marker = aMap.addMarker(new MarkerOptions());
            marker.setPosition(mLatLng);
            mMarkerList.add(marker);
            LogUtil.d(TAG, "ListLeng =" + mMarkerList.size());
        }
    }

    /**
     * 删除地图上的标记
     * @param markerControl 传递消息的对象
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteMarker(MarkerControl markerControl) {
        LogUtil.d(TAG, "code = " + markerControl.getControl());
        int code = markerControl.getControl();
        if (code == -100) {
            for (int i = 0; i <= mMarkerList.size() - 1; i++) {
                mMarkerList.get(i).destroy();
            }
            mMarkerList.clear();
        } else {
            Marker marker = mMarkerList.get(code);
            mMarkerList.remove(code);
            marker.destroy();
        }
    }
    /**
     * 发送选择的城市
     * @param city 选择的城市
     */
    private void sendSearchLocation(String city) {
        EventBus.getDefault().post(city);
    }
}
