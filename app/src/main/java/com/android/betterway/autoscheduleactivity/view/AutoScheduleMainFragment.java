package com.android.betterway.autoscheduleactivity.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.betterway.R;
import com.android.betterway.autoscheduleactivity.daggerneed.AutoScheduleMainFragmentComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.AutoSchedulePresenterImpelModule;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerAutoScheduleMainFragmentComponent;
import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.itemplandialog.LocationDialogFragment;
import com.android.betterway.recyclerview.LocationPlanAdapter;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutoScheduleMainFragment extends Fragment implements AutoScheduleView {
    @BindString(R.string.location_text_default)
    String defaultString;

    Unbinder unbinder;
    @BindView(R.id.locationplanrecyclerview)
    RecyclerView mLocationplanrecyclerview;
    private AutoSchedulePresenterImpel mAutoSchedulePresenterImpel;
    private String searchLocation;
    private static final String TAG = "AutoScheduleMainFragment";
    private LocationPlanAdapter mLocationPlanAdapter;
    List<SoftReference<LocationPlanAdapter>> mSoftReferenceList = new ArrayList<SoftReference<LocationPlanAdapter>>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoScheduleMainFragmentComponent autoScheduleMainFragmentComponent =
                DaggerAutoScheduleMainFragmentComponent.builder()
                        .autoSchedulePresenterImpelModule(new AutoSchedulePresenterImpelModule(this))
                        .build();
        mAutoSchedulePresenterImpel = autoScheduleMainFragmentComponent
                .getAutoAutoSchedulePresenterImpel();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 获得选择的城市并记录
     *
     * @param location 选择的城市
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLocationEvent(String location) {
        searchLocation = location;
    }

    /**
     * 接收到dialogfragment发送的LocationPlan后的操作
     *
     * @param locationPlan 接收到的LocationPlain
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onLocationPlanEvent(LocationPlan locationPlan) {
        LogUtil.d(TAG, "onLocationPlanEvent");
        mAutoSchedulePresenterImpel.addPlan(locationPlan);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_schedule_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public LocationPlanAdapter getLocationPlanAdapter() {
        if (mSoftReferenceList.size() == 0) {
            SoftReference<LocationPlanAdapter> softReference =
                    new SoftReference<LocationPlanAdapter>(mLocationPlanAdapter);
            return softReference.get();
        } else {
            return mSoftReferenceList.get(0).get();
        }
    }

    /**
     * 初始化recyclerview
     */
    private void initView() {
        mLocationPlanAdapter = new LocationPlanAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mLocationplanrecyclerview.setLayoutManager(linearLayoutManager);
        mLocationPlanAdapter.setAddLocationButton(new LocationPlanAdapter.AddLocationButton() {
            @Override
            public void buttonClick() {
                onViewClicked();
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        mLocationplanrecyclerview.setAdapter(mLocationPlanAdapter);
        mLocationplanrecyclerview.setItemAnimator(defaultItemAnimator);
    }

    @Override
    public void showFinishedDialog() {

    }

    @Override
    public void showClearAllDialog() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 点击添加事件按钮之后的逻辑
     */
    public void onViewClicked() {
        if (searchLocation == null) {
            ToastUtil.show(getContext(), "请选择地点");
            return;
        }
        if (searchLocation.equals(defaultString)) {
            ToastUtil.show(getContext(), "请选择地点");
        } else {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            LocationDialogFragment locationDialogFragment = new LocationDialogFragment();
            locationDialogFragment.setSearchLocation(searchLocation);
            locationDialogFragment.show(manager, "locationDialogFragment");
        }
    }
}
