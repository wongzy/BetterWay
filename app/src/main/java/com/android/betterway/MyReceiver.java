package com.android.betterway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.betterway.myservice.MyService;

public class MyReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) {
            Intent intent1 = new Intent(context, MyService.class);
            context.startService(intent1);
        }
    }
}
