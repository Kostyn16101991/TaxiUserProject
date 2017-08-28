package com.example.vmm408.taxiuserproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.vmm408.taxiuserproject.activities.MainActivity;

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            context.startActivity(new Intent(context, MainActivity.class));
    }
}
