package com.android.betterway.mainactivity.datepicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import com.android.betterway.autoscheduleactivity.view.AutoScheduleActivity;
import com.android.betterway.normalscheduleactivity.view.NormalScheduleActivity;
import com.android.betterway.R;
import com.android.betterway.other.ActivityType;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;

/**
 * 日期DialogFragment类
 */
public class DatePickerFragment extends DialogFragment {
    private static final String TAG = "DatePickerFragment";
    private static ActivityType mActitityType;
    public void setActitityType(ActivityType actitityType) {
        mActitityType = actitityType;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity().getApplication())
                .inflate(R.layout.datepicker, null);
        final DatePicker datePicker = (DatePicker) v;
        datePicker.setMinDate(TimeUtil.getDayLong());
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getContext())
                .setView(datePicker)
                .setTitle(R.string.datepicker_title)
                .setPositiveButton(R.string.button_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (mActitityType) {
                            case ADDAUTOACTIVITY:
                                LogUtil.v(TAG, "add autoActivity");
                                Intent intent = new Intent(getActivity()
                                        .getApplicationContext(), AutoScheduleActivity.class);
                                String date = datePicker.getYear() + "年" + (datePicker.getMonth() + 1)
                                        + "月" + datePicker.getDayOfMonth() + "日";
                                intent.putExtra("Date", date);
                                startActivity(intent);
                                break;
                            case ADDMORNALACTIVITY:
                                LogUtil.v(TAG, "add mornalActitity");
                                Intent intent1 = new Intent(getActivity()
                                        .getApplicationContext(), NormalScheduleActivity.class);
                                long datelong = (long) (datePicker.getYear() * 10000
                                        + (datePicker.getMonth() + 1 ) * 100 +datePicker.getDayOfMonth());
                                String date1 = datePicker.getYear() + "年" + (datePicker.getMonth() + 1)
                                        + "月" + datePicker.getDayOfMonth() + "日";
                                intent1.putExtra("Date", date1);
                                intent1.putExtra("datelong", datelong);
                                startActivity(intent1);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, null);
        AlertDialog dialog = mActitityType == ActivityType.ADDAUTOACTIVITY
                ? dialogbuilder.setIcon(R.drawable.ic_action_addsmart).create()
                : dialogbuilder.setIcon(R.drawable.ic_action_addnormal).create();
        if (dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialogAnim);
        }
        return dialog;
    }
}
