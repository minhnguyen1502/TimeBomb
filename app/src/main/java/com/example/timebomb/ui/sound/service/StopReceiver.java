package com.example.timebomb.ui.sound.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StopReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Dừng dịch vụ khi người dùng nhấn Stop
        context.stopService(new Intent(context, CrackScreen.class));
    }
}
