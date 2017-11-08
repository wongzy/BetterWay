package com.android.betterway.autoscheduleactivity.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.betterway.R;
import com.android.betterway.autoscheduleactivity.daggerneed.AutoScheduleMainFragmentComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.AutoSchedulePresenterImpelModule;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerAutoScheduleMainFragmentComponent;
import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.itemplandialog.LocationDialogFragment;
import com.android.betterway.other.MarkerControl;
import com.android.betterway.recyclerview.LocationPlanAdapter;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutoScheduleMainFragment extends Fragment implements AutoScheduleView {
    private static final int BIKE = 1;
    private static final int BUS = 2;
    private static final int CAR = 3;
    private WeakHashMap<String,WeakReference<Context>> mWeakHashMap = new WeakHashMap<>();
    @BindString(R.string.location_text_default)
    String defaultString;

    Unbinder unbinder;
    @BindView(R.id.locationplanrecyclerview)
    RecyclerView mLocationplanrecyclerview;
    private AutoSchedulePresenterImpel mAutoSchedulePresenterImpel;
    private String searchLocation;
    private static final String TAG = "AutoScheduleMainFragment";
    private LocationPlanAdapter mLocationPlanAdapter;
    private List<SoftReference<LocationPlanAdapter>>
            getLocationPlanAdapterList = new ArrayList<SoftReference<LocationPlanAdapter>>();
    private ViewGroup mViewGroup;
    private ProgressDialog mProgressDialog;
    private BottomSheetDialog bottomSheetDialog;
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

    @Override
    public String returnSearchLocation() {
        return searchLocation;
    }

    /**
     * 接收到dialogfragment发送的LocationPlan后的操作
     *
     * @param locationPlan 接收到的LocationPlain
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationPlanEvent(LocationPlan locationPlan) {
        LogUtil.e(TAG, "onLocationPlanEvent");
        mAutoSchedulePresenterImpel.addPlan(locationPlan);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewGroup = container;
        View view = inflater.inflate(R.layout.fragment_auto_schedule_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        initView();
        return view;
    }

    /**
     * 初始化各个组件
     */
    private void init() {
        AutoScheduleMainFragmentComponent autoScheduleMainFragmentComponent =
                DaggerAutoScheduleMainFragmentComponent.builder()
                        .autoSchedulePresenterImpelModule(new AutoSchedulePresenterImpelModule(this))
                        .build();
        autoScheduleMainFragmentComponent.inject(this);
        mAutoSchedulePresenterImpel = autoScheduleMainFragmentComponent.getAutoAutoSchedulePresenterImpel();
        ItemTouchHelper.SimpleCallback simpleCallback = new
                ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return recyclerView.getAdapter().getItemCount() == viewHolder.getAdapterPosition();
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                LogUtil.d(TAG, "position = " + position);
                if (mLocationPlanAdapter.getItemCount() != 1 && position == 0) {
                    mAutoSchedulePresenterImpel.deleteAllPlan();
                    postMarkerControl(-100);
                } else if (mLocationPlanAdapter.getItemCount() != 1) {
                    mAutoSchedulePresenterImpel.deletePlan(position);
                    postMarkerControl(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (recyclerView.getAdapter().getItemCount() - 1 == viewHolder.getAdapterPosition()) {
                        viewHolder.itemView.setAlpha(1);
                        viewHolder.itemView.setTranslationY(0);
                    } else {
                        //滑动时改变Item的透明度
                        final float alpha = 1 - Math.abs(dY) / (float) viewHolder.itemView.getHeight();
                        viewHolder.itemView.setAlpha(alpha);
                        viewHolder.itemView.setTranslationY(dY);
                    }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mLocationplanrecyclerview);
    }
    @Override
    public LocationPlanAdapter getLocationPlanAdapter() {
        if (getLocationPlanAdapterList.size() == 0) {
            SoftReference<LocationPlanAdapter> locationPlanAdapterSoftReference =
                    new SoftReference<LocationPlanAdapter>(mLocationPlanAdapter);
            getLocationPlanAdapterList.add(locationPlanAdapterSoftReference);
            return locationPlanAdapterSoftReference.get();
        } else {
            return getLocationPlanAdapterList.get(0).get();
        }
    }

    /**
     * 初始化recyclerview
     */
    private void initView() {
        LogUtil.e(TAG, "initView()");
        mLocationPlanAdapter = new LocationPlanAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
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
        LogUtil.e(TAG, "onDestroyView()");
        super.onDestroyView();
        mAutoSchedulePresenterImpel.deleteAllPlan();
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
            locationDialogFragment.setisFitst(mAutoSchedulePresenterImpel.isFitst());
            locationDialogFragment.show(manager, "locationDialogFragment");
        }
    }

    /**
     * 发送传递消息的事件
     * @param position 地点所在list中的位置
     */
    private void postMarkerControl(int position) {
        MarkerControl markerControl = new MarkerControl(position);
        EventBus.getDefault().post(markerControl);
    }

    /**
     * 点击完成按钮后的动作
     */
    public void finishPlan() {
        if (mAutoSchedulePresenterImpel.isAdded()) {
            bottomSheetDialog = new BottomSheetDialog(getContext());
            View view = getLayoutInflater().inflate(R.layout.chose_approach_to_go, mViewGroup, false);
            LinearLayout bus, bycycle, taxi;
            bus = (LinearLayout) view.findViewById(R.id.go_out_by_bus);
            bycycle = (LinearLayout) view.findViewById(R.id.go_out_by_bycycle);
            taxi = (LinearLayout) view.findViewById(R.id.go_out_by_car);
            bus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAutoSchedulePresenterImpel.finishAddedLocationPlan(BUS);
                }
            });
            bycycle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAutoSchedulePresenterImpel.finishAddedLocationPlan(BIKE);
                }
            });
            taxi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAutoSchedulePresenterImpel.finishAddedLocationPlan(CAR);
                }
            });
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();
        } else {
            ToastUtil.show(getContext(), "请至少添加3个地点，其中第一个地点为起点");
        }
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("正在自动生成计划，请稍等");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void dismissBottomSheet() {
        bottomSheetDialog.dismiss();
    }

    @Override
    public Context returnContext() {
        if (mWeakHashMap.get("Context") == null) {
            WeakReference<Context> reference = new WeakReference<Context>(getContext());
            mWeakHashMap.put("Context", reference);
            return reference.get();
        } else {
            return mWeakHashMap.get("Context").get();
        }
    }
}
