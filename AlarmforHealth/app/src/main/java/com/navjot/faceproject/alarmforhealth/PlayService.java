package com.navjot.faceproject.alarmforhealth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by T00523221 on 2/24/2017.
 */

public class PlayService extends Service {
    public PlayService() {

    }
    MediaPlayer myAudio;
    NotificationManager notificationManager;
    int song;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // myAudio = MediaPlayer.create(this,R.raw.closer);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            song = intent.getIntExtra("song",-1);
        }
        catch(Exception ex){
            Log.d(""+ex,"");
        }

        if(song == 0)
            myAudio = MediaPlayer.create(this,R.raw.ghostreverie);
        else if(song == 1)
            myAudio = MediaPlayer.create(this,R.raw.ghostsetmusictothemoon);
        else if(song == 2)
            myAudio = MediaPlayer.create(this,R.raw.ghostemptyrooms);
        else if(song == 3)
            myAudio = MediaPlayer.create(this,R.raw.ghostliesghostrustconsiderations);
        else
            myAudio = MediaPlayer.create(this,R.raw.ghostreverie);

        myAudio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(mp == myAudio){
                    myAudio.start();
                    myAudio.setLooping(true);
                }
                NotificationCompat.Builder notification = new NotificationCompat.Builder(PlayService.this).
                        setSmallIcon(R.drawable.logo1).
                        setContentTitle("Alarm is off").
                        setVibrate(new long[]{0,300,300}).
                        setContentText("Scan code to Dismiss").
                                setLights(Color.WHITE,1000,5000).
                                setAutoCancel(true).
                                setPriority(NotificationCompat.PRIORITY_HIGH).
                                setStyle(new NotificationCompat.BigTextStyle().bigText("Scan code to Dismiss")).
                                setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);
                notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0,notification.build());
            }
        });
            Intent intentforcam = new Intent().setClass(this, Faceface.class);
            intentforcam.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentforcam);
        Log.e("Service","In service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myAudio.stop();
        myAudio.release();

    }
}
