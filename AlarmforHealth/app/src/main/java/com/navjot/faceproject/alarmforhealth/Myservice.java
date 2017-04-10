package com.navjot.faceproject.alarmforhealth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by T00523221 on 3/9/2017.
 */

public class Myservice extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("OnStartCommand"," Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("OnStartCommand"," Destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
