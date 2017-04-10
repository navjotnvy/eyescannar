package com.navjot.faceproject.alarmforhealth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Button add,profile,createprofile,setting;
    ListView listView;
    int i,j, type;
    String time;
    ArrayList<String> yo;
    int kind;
    TextView textView;
    int count;
    LinearLayout rel;
    SharedPreferences prefs = null;
    public static final String PREFS_NAME = "MyPreferenceFile";
    public static final String PREFS_NAME1 = "MyPreferenceFile1";
    private static final int MY_PERMISSIONS_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rel = (LinearLayout) findViewById(R.id.activity_main);
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
                setTheme(R.style.AppTheme2);
            }
            else if(bground.equals("backgroundrose")){
                rel.setBackgroundResource(R.drawable.backgroundrose);
            }
            else{
                rel.setBackgroundResource(R.drawable.yellowpainting);
            }
        }
        //Toast.makeText(this, bground+"", Toast.LENGTH_SHORT).show();

        final TextView date = (TextView) findViewById(R.id.textView5);
        final TextView date1 = (TextView) findViewById(R.id.textView6);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        kind = settings.getInt("KIND",0);
        profile = (Button) findViewById(R.id.button);
        createprofile = (Button) findViewById(R.id.button5);
        setting = (Button) findViewById(R.id.button2);
        type = 0;
        i = 0;
        yo = new ArrayList<String>();
        j = 0;
        time = readFromFile(getBaseContext());
        textView = (TextView)findViewById(R.id.textView);
        //   Toast.makeText(getBaseContext(),Integer.toString(j),Toast.LENGTH_SHORT).show();
        String k = "";
        String kk = "";
        listView = (ListView)findViewById(R.id.listView);
        Adapter listAdapter = new Adapter(this,R.layout.switch_list,yo);
        listView.setAdapter(listAdapter);
        count = listView.getCount();
        prefs = getSharedPreferences("android.cs.tru.alarm.Set_Alarms", MODE_PRIVATE);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice(Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID))  // My Galaxy Nexus test phone
                .build();
        mAdView.loadAd(request);
        Intent i = getIntent();
        if(i.getBooleanExtra("FUN",false)) {
            Intent intent = new Intent(MainActivity.this,Set_Alarms.class);
            intent.putExtra("ADD",false);
            intent.putExtra("HOUR", i.getStringExtra("HOUR"));
            intent.putExtra("MINUTE", i.getStringExtra("MINUTE"));
            intent.putExtra("COUNT",count);
            intent.putExtra("POSITION",i.getIntExtra("POSITION", -1));
            startActivity(intent);
            finish();
        }
        k = i.getStringExtra("Dismiss_Type");
        kk = i.getStringExtra("Snooze");
        if(k != null) {
            if (k.equals("DISMISS")) {
                Intent i01 = new Intent(MainActivity.this, Set_Alarms.class);
                i01.putExtra("DISMISS", true);
                int chk = 0;
                String str = readFromFile(getBaseContext());
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                String time = Integer.toString(hour) + ":" + Integer.toString(minute);
                while (!str.equals("")) {
                    if (str.substring(0, str.indexOf(".")).equals(time)) {
                        break;
                    } else {
                        chk++;
                    }
                    str = str.substring(str.indexOf(".") + 1, str.length());
                }
                Log.e("For repet", Integer.toString(chk));
                String temp = readFromFile1(getBaseContext());
                try {
                    OutputStreamWriter outputStreamWriter11 = new OutputStreamWriter(getBaseContext().openFileOutput("Type.txt", Context.MODE_PRIVATE));
                    outputStreamWriter11.close();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("Type.txt", Context.MODE_APPEND));
                    int yo = 0;
                    while (!temp.equals("")) {
                        if (yo != chk) {
                            outputStreamWriter.write(temp.substring(0, temp.indexOf(".")) + ".\n");
                        }
                        temp = temp.substring(temp.indexOf(".") + 1, temp.length());
                        yo++;
                    }
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                startActivity(i01);
                finish();
            }
            else if(kk.equals("SNOOZE")){
                Intent i01 = new Intent(MainActivity.this,Set_Alarms.class);
                i01.putExtra("SNOOZE",true);
                //    startActivity(i01);
                //  finish();
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                String current = (String)listView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,Set_Alarms.class);
                intent.putExtra("ADD",false);
                intent.putExtra("POSITION",position);
                String hour = current.substring(0,current.indexOf(":"));
                String minute = current.substring(current.indexOf(":") + 1,current.length());
                intent.putExtra("HOUR", hour);
                intent.putExtra("MINUTE", minute);
                startActivity(intent);
                finish();
            }
        });

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Set_Alarms.class);
                intent.putExtra("ADD",true);
                intent.putExtra("COUNT",count);
                startActivity(intent);
                finish();
            }
        });

        final Handler date_handler = new Handler(getMainLooper());
        date_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                date.setGravity(Gravity.CENTER);
                date.setTextSize(30);
                date1.setGravity(Gravity.CENTER);
                date1.setTextSize(20);
                Calendar c = Calendar.getInstance(TimeZone.getDefault());
                SimpleDateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy G", Locale.CANADA);
                String formattedDate = df.format(c.getTime());
                date.setText(formattedDate);
                SimpleDateFormat df1 = new SimpleDateFormat(" hh:mm:ss a z",Locale.CANADA);
                String formattedDate1 = df1.format(c.getTime());
                //c.get(Calendar.HOUR_OF_DAY);
                date1.setText(formattedDate1);
                date_handler.postDelayed(this, 1000);
            }
        }, 10);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        createprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateProfile.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), settings.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Log.d("Log", "#1 INFO: shouldShowRequestPermissionRationale");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showMessageOKCancel("You need to allow access to Camera. Otherwise, you can't use the app!");
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_CAMERA);
            } else {
                Log.d("Log", "#2 INFO: request the permission - No explanation needed");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_PERMISSIONS_CAMERA);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                Toast.makeText(this, "Please print the QR code in the settings to turn off the alarm", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_CAMERA);
        }
        }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME1, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("IDS", 0);
            editor.putString("SOND_ID","0");
            editor.commit();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
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
                setTheme(R.style.AppTheme2);
            }
            else if(bground.equals("backgroundrose")){
                rel.setBackgroundResource(R.drawable.backgroundrose);
            }
            else{
                rel.setBackgroundResource(R.drawable.yellowpainting);
            }
        }

    }

    public void onPause() {
        super.onPause();
    }

    private void showMessageOKCancel(String message) {
        Log.d("Log", "#5 INFO: call AlertDialog");

        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .create()
                .show();
    }


    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        final int BUTTON_POSITIVE = -1;
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
                    // int which = -1
                    Log.d("Log", "#6 INFO: send a second request");
                    ActivityCompat.requestPermissions( MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_CAMERA);
                    dialog.dismiss();
                    break;
            }
        }
    };


    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("Names.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    if(type == 1) {
                        i++;
                        stringBuilder.append("\n");
                    }
                    if(type == 0){
                        yo.add(receiveString);
                        j++;
                    }
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }

    private String readFromFile1(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("Type.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    if(type == 1) {
                        i++;
                        stringBuilder.append("\n");
                    }
                    if(type == 0){
                        yo.add(receiveString);
                        j++;
                    }
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Log", "#3 INFO: received a response permission was granted");
                    Toast.makeText(this, "Please print the QR code in the settings to turn off the alarm", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("Log", "#4 INFO: received a response permission - denied");
                    Toast.makeText(this, "Please print the QR code in the settings to turn off the alarm", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(

                            MainActivity.this, new String[]{android.Manifest.permission.CAMERA},
                            MY_PERMISSIONS_CAMERA);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }






}

