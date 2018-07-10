package com.pixzen.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Alarm extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        // fetch extra strings from intent
        String getStrings = intent.getExtras().getString("extra");

        // create an intent for the sound service
        Intent serviceIntent = new Intent(context, PlaySoundService.class);

        // pass extra string from MainActivity to PlaySound Service
        serviceIntent.putExtra("extra", getStrings);

        // start the service
        context.startService(serviceIntent);
    }
}
