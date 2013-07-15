package com.junyi.perngant.receiver;


import com.junyi.perngant.AlarmAlert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class NaonaoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AlarmAlert.class);  
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        context.startActivity(i);
    }

}
