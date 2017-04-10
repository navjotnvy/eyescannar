package com.navjot.faceproject.alarmforhealth;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Pushups extends AppCompatActivity implements SensorEventListener {
    DBAdapter myDb;
    private TextView light_sensor;
    private SensorManager sensorMan;
    private Sensor accelerometer, light;
    int count;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    String fname, lname, weight, height1, walkprofile, pushupsprofile;
    int id;
    String cnt;
    long wakeup;
    Boolean check=false;
    Cursor cursor;
    RelativeLayout rel;
Button exit, back;
    int a=0;
// In onCreate method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushups);
        light_sensor = (TextView)findViewById(R.id.secondsensor);
        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = sensorMan.getDefaultSensor(Sensor.TYPE_LIGHT);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        rel = (RelativeLayout) findViewById(R.id.activity_pushups);
        openDB();

        SharedPreferences backpref = getSharedPreferences("prefback", Context.MODE_PRIVATE);
        String bground = backpref.getString("themm","");
        if(bground.equals("")){
            rel.setBackgroundResource(R.drawable.yellowpainting);
        }else{
            if(bground.equals("deadLeaf")) {
                rel.setBackgroundResource(R.drawable.deadleaf);
            }
            else if(bground.equals("antique")){
                rel.setBackgroundResource(R.drawable.antique);
            }
            else if(bground.equals("backgroundyelwrite")){
                rel.setBackgroundResource(R.drawable.backgroundyelwrite);
            }
            else if(bground.equals("abstract1")){
                rel.setBackgroundResource(R.drawable.abstract1);
            }
            else if(bground.equals("backgroundrose")){
                rel.setBackgroundResource(R.drawable.backgroundrose);
            }
            else{
                rel.setBackgroundResource(R.drawable.yellowpainting);
            }
        }

        exit = (Button)findViewById(R.id.button16);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                //Toast.makeText(Pushups.this, "App Exit", Toast.LENGTH_SHORT).show();
            }
        });

        back = (Button)findViewById(R.id.button15);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Pushups.this,MainActivity.class);
                //startActivity(intent);
                a=1;
                finish();
            }
        });


        cursor = myDb.getAllRows();
        if(cursor.getCount() == 0){

        }else {
            id = cursor.getInt(DBAdapter.COL_ROWID);
            fname = cursor.getString(DBAdapter.COL_FNAME);
            lname = cursor.getString(DBAdapter.COL_LNAME);
            wakeup = cursor.getInt(DBAdapter.COL_WAKEUP);
            weight = cursor.getString(DBAdapter.COL_WEIGHT);
            height1 = cursor.getString(DBAdapter.COL_HEIGHT);
            walkprofile = cursor.getString(DBAdapter.COL_WALK);
            pushupsprofile = cursor.getString(DBAdapter.COL_PUSHUPS);
            String message = "id=" + id
                    + ", fname=" + fname
                    + ", lname=" + lname
                    + ", #=" + wakeup
                    + ", weight=" + weight
                    + ", height" + height1
                    + ", walk" + walkprofile
                    + ", pushupsprofile" + pushupsprofile
                    + "\n";
            //Toast.makeText(this, message+"", Toast.LENGTH_SHORT).show();
        }


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice(Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID))  // My Galaxy Nexus test phone
                .build();
        mAdView.loadAd(request);
    }
// And these:

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorMan.registerListener(this, light,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cnt = ""+count;
        Intent walkintent = new Intent(Pushups.this, Profile.class);
        walkintent.putExtra("pushup",cnt);
        startActivity(walkintent);

        try {
            if(cursor.getCount() == 0){
                long newId = myDb.insertRow("", "", 0, "", "","","");
                cursor = myDb.getRow(newId);
            }
            else
            {
                long id = cursor.getLong(DBAdapter.COL_ROWID);
                myDb.updateRow(id,fname, lname, wakeup, weight, height1,walkprofile, cnt);
                //Toast.makeText(this, "updateRow Ran", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        }
        catch (NullPointerException ex)
        {
            Log.d("TAG_walk_NULL_POINTER",""+ex);
        }

        if(a==1){
            finish();
        }
        sensorMan.unregisterListener(this);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            if (mAccel > 0.4) {
                count=0;
                Toast.makeText(this, "You are cheating. Please keep the device still and Start again", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
        else if ((event.sensor.getType()== Sensor.TYPE_LIGHT)&&(mAccel<=0.4)) {
            Log.d("Light", count + " - " + event.sensor.getName()+check + " - VALUE: x = " + event.values[0]);
            if (event.values[0] >=20) {
                light_sensor.setText("Push up Counter");
                light_sensor.setBackgroundColor(Color.GRAY);
                check = true;
            } else if ((event.values[0] >0)&&(event.values[0] <20)){
                if((check)&&(light_sensor.getText().toString().equals("Push up Counter"))){
                    light_sensor.setBackgroundColor(Color.YELLOW);
                    count++;
                    light_sensor.setText("Pushup "+count);
                    check = false;
                }
                else {
                    check = false;
                }
            }
            else{

            }
        }
        else {

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

}
