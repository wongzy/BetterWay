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
import com.android.betterway.R;
import com.android.betterway.data.MyTime;
import com.android.betterway.itempickeractivity.ItemPickerActivity;
import com.android.betterway.utils.TimeUtil;
import com.android.betterway.utils.ToastUtil;


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false); //设置点击屏幕Dialog不消失

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
                            ToastUtil.show(getContext(), "未输入地点");
                        } else if (TextUtils.isEmpty(mSpendTime.getText())) {
                            ToastUtil.show(getContext(), "未输入消耗时间");
                        } else {
                            dialog.dismiss();
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
        myTime = TimeUtil.getMinuteTime();
        mStartTime.setText(myTime.getSingleTime());
        mEditLocation.setOnClickListener(this);
        mStartTime.setOnClickListener(this);
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
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
