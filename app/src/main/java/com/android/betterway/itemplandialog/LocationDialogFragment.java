package com.android.betterway.itemplandialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.services.core.LatLonPoint;
import com.android.betterway.R;
import com.android.betterway.data.LocationItemBean;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.MyTime;
import com.android.betterway.itempickeractivity.ItemPickerActivity;
import com.android.betterway.other.MapMarker;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;
import com.android.betterway.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class LocationDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button mEditLocation;
    private EditText mThingStatement;
    private EditText mSpendMoney;
    private TextView mStartTime;
    private EditText mSpendTime;
    private ViewGroup mViewGroup;
    private MyTime myTime;
    private String searchLocation;
    private LatLonPoint mLatLonPoint;
    public static final int TRUECODE = 100;
    public static final int WRONGCODE = 101;
    private boolean isFitst = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewGroup = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.location_dialog_fragment, mViewGroup, false);
        initView(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_action_item_dialog)
                .setView(view)
                .setTitle("请输入信息")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(mEditLocation.getText())) {
                            ToastUtil.show(getContext(), "未选择地点");
                        } else {
                            getLocationPlanData();
                            postSureCode(MapMarker.ADD);
                        }
                    }
                })
                .setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnCancelListener(null);
        if (alertDialog.getWindow() != null) {
            Window window = alertDialog.getWindow();
            window.setWindowAnimations(R.style.dialogAnim);
        }
        return alertDialog;
    }

    /**
     * 初始化控件
     * @param view 容器view
     */
    private void initView(View view) {
        mEditLocation = (Button) view.findViewById(R.id.edit_location);
        mThingStatement = (EditText) view.findViewById(R.id.thing_statement);
        mSpendMoney = (EditText) view.findViewById(R.id.spend_money);
        mStartTime = (TextView) view.findViewById(R.id.start_time);
        mSpendTime = (EditText) view.findViewById(R.id.spend_time);
        TextView startTimeFloag = (TextView) view.findViewById(R.id.start_time_flag);
        myTime = TimeUtil.getMinuteTime();
        mStartTime.setText(myTime.getSingleTime());
        mEditLocation.setOnClickListener(this);
        mStartTime.setOnClickListener(this);
        if (isFitst) {
            mStartTime.setVisibility(View.VISIBLE);
            startTimeFloag.setVisibility(View.VISIBLE);
        } else {
            mStartTime.setVisibility(View.GONE);
            startTimeFloag.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_time:
                BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.clockpicker_dialog, null);
                TimePicker timePicker = (TimePicker) view;
                timePicker.setIs24HourView(true);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        myTime.setHour(hourOfDay);
                        myTime.setMinute(minute);
                        mStartTime.setText(myTime.getSingleTime());
                    }
                });
                dialog.setContentView(view);
                dialog.show();
                break;
            case R.id.edit_location:
                Intent intent = new Intent(getContext(), ItemPickerActivity.class);
                intent.putExtra("location", searchLocation);
                startActivityForResult(intent, TRUECODE);
                break;
            default:
                break;
        }
    }

    public void setSearchLocation(String searchLocation) {
        this.searchLocation = searchLocation;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.e("LocationDialogFragment", "onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TRUECODE && data != null) {
            LocationItemBean locationItemBean = data.getParcelableExtra("getLocation");
            mEditLocation.setText(locationItemBean.getName());
            postLatlngPointEvent(locationItemBean.getLatLonPoint());
            mLatLonPoint = locationItemBean.getLatLonPoint();
        } else if (resultCode == WRONGCODE) {
            ToastUtil.show(getContext(), "请选择一个精确的地点");
        }
    }

    /**
     * 发送选择地点的坐标
     * @param latLonPoint 选择地点的坐标
     */
    private void postLatlngPointEvent(LatLonPoint latLonPoint) {
        LogUtil.e("DialogFragment", "postLatlngPoint");
        EventBus.getDefault().postSticky(latLonPoint);
    }

    /**
     * 发送确定的信号，保存坐标
     * @param mapMarker 信号
     */
    private void postSureCode(MapMarker mapMarker) {
        LogUtil.e("DialogFragment", "PostSureCode");
        EventBus.getDefault().post(mapMarker);
    }

    /**
     * 获得用户输入的数据
     */
    public void getLocationPlanData() {
        LocationPlan locationPlan = new LocationPlan();
        locationPlan.setLocation(mEditLocation.getText().toString());
        if (!TextUtils.isEmpty(mThingStatement.getText())) {
            locationPlan.setStatement(mThingStatement.getText().toString());
        }
        if (!TextUtils.isEmpty(mSpendMoney.getText())) {
            int spendMoney = Integer.parseInt(mSpendMoney.getText().toString());
            locationPlan.setMoneySpend(spendMoney);
        }
        if (!TextUtils.isEmpty(mSpendTime.getText())) {
            int spendTime = Integer.parseInt(mSpendTime.getText().toString());
            locationPlan.setStayMinutes(spendTime);
        }
        if (isFitst) {
            locationPlan.setStartTime(myTime.getLong());
        }
        locationPlan.setLat(mLatLonPoint.getLatitude());
        locationPlan.setLon(mLatLonPoint.getLongitude());
        postLocationPlanData(locationPlan);
    }

    /**
     * 通过事件总线发送LocationPlan
     * @param locationPlan 需要发送的LocationPlan
     */
    private void postLocationPlanData(LocationPlan locationPlan) {
        LogUtil.d("DialogFragment", "postLocationPlanData()");
        EventBus.getDefault().post(locationPlan);
    }

    /**
     * 设置是否为第一个事件
     * @param misFitst MainFragment中传递的booleean
     */
    public void setisFitst(boolean misFitst) {
        isFitst = misFitst;
    }
}
