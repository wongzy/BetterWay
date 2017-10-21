package com.android.betterway.mainactivity.datepicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import com.android.betterway.AutoScheduleActivity.view.AutoScheduleActivity;
import com.android.betterway.NormalScheduleActivity.view.NormalScheduleActivity;
import com.android.betterway.R;
import com.android.betterway.other.ActivityType;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;


public class DatePickerFragment extends DialogFragment {
    private static final String TAG = "DatePickerFragment";
    private static ActivityType mActitityType;
    public void setActitityType(ActivityType actitityType) {
        mActitityType = actitityType;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.datepicker, null);
        final DatePicker datePicker = (DatePicker)v;
        datePicker.setMinDate(TimeUtil.getDayLong());
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity())
                .setView(datePicker)
                .setTitle(R.string.datepicker_title)
                .setPositiveButton(R.string.button_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (mActitityType) {
                            case ADDAUTOACTIVITY:
                                LogUtil.v(TAG, "add autoActivity");
                                Intent intent = new Intent(getActivity().getApplicationContext(), AutoScheduleActivity.class);
                                startActivity(intent);
                                break;
                            case ADDMORNALACTIVITY:
                                LogUtil.v(TAG, "add mornalActitity");
                                Intent intent1 = new Intent(getActivity().getApplicationContext(), NormalScheduleActivity.class);
                                startActivity(intent1);
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, null);
        AlertDialog dialog = mActitityType == ActivityType.ADDAUTOACTIVITY?
                dialogbuilder.setIcon(R.drawable.ic_action_addsmart).create():
                dialogbuilder.setIcon(R.drawable.ic_action_addnormal).create();
        if (dialog.getWindow() != null) {
            Window window=dialog.getWindow();
            window.setWindowAnimations(R.style.dialogAnim);
        }
        return dialog;
    }
}
