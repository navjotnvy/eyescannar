package com.navjot.faceproject.alarmforhealth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by T00523221 on 2/24/2017.
 */

public class Alarms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int song = 0;
        song = intent.getIntExtra("SONG",-1);

        if(intent.getStringExtra("KIND").equals("Start")) {
            Intent intent1 = new Intent(context, PlayService.class);
            intent1.putExtra("song",song);
            context.startService(intent1);
        }

        if(intent.getStringExtra("KIND").equals("Stop")) {
            Log.e("Stop","Stop");
            Intent intent2 = new Intent(context, PlayService.class);
            context.stopService(intent2);
        }
    }
}



