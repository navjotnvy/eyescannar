package com.navjot.faceproject.alarmforhealth;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class Set_Alarms extends AppCompatActivity {
    Button set,cancel,delete,downloadbutton;
    TimePicker timePicker;
    int hour, minute;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Intent intent;
    boolean kind;
    int id,count;
    int position;
    int initial_hour, initial_minute;
    String snooze;
    RadioButton repeat_on, repeat_off;
    RadioGroup group;
    boolean repeat;
    Spinner spinner;
    Bitmap bitmap;
    Bitmap finalBitmap;
    int song;
    private static final int MY_PERMISSIONS_STORAGE = 1;
   // int request_code = 1;
    LinearLayout rel;
    public static final String PREFS_NAME1 = "MyPreferenceFile1";
    public static final String PREFS_NAME = "MyPreferenceFile";
    NotificationManager notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__alarms);

        timePicker = (TimePicker)findViewById(R.id.timePicker);
        set = (Button)findViewById(R.id.set);
        cancel = (Button)findViewById(R.id.unset);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        delete = (Button)findViewById(R.id.delete1);
        repeat_on = (RadioButton)findViewById(R.id.radioButton);
        repeat_off = (RadioButton)findViewById(R.id.radioButton2);
        group = (RadioGroup)findViewById(R.id.group);
        spinner = (Spinner)findViewById(R.id.spinner);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("Log", "#1 INFO: shouldShowRequestPermissionRationale");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showMessageOKCancel("We need to access the Storage Location to save the QRCODE");
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_STORAGE);
            } else {
                Log.d("Log", "#2 INFO: request the permission - No explanation needed");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                Toast.makeText(this, "Please print the QR code You will need it to turn off the alarm", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_STORAGE);
        }


        rel = (LinearLayout) findViewById(R.id.activity_set_alarms);
        downloadbutton = (Button)findViewById(R.id.button13);
        downloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean b = isExternalStorageWritable();
                if(b) {
                    saveFileInPublicDirectory("qrcode.jpeg");
                }
                else{
                    Toast.makeText(Set_Alarms.this, "Not enough Space Or permission not Granted", Toast.LENGTH_LONG).show();
                }
            }
    });


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

        final Intent intent = new Intent(Set_Alarms.this, Alarms.class);
        Intent i = this.getIntent();
        position = i.getIntExtra("POSITION", -1);
        count = i.getIntExtra("COUNT",0);
        snooze = "";
        Intent ii = this.getIntent();
        String kk = getIntent().getStringExtra("Snooze");
        //  Toast.makeText(getBaseContext(),Integer.toString(count),Toast.LENGTH_SHORT).show();
        if(i.getBooleanExtra("DISMISS", false)){
            intent.putExtra("KIND","Stop");
            sendBroadcast(intent);
            finish();
        }

        else if(i.getBooleanExtra("ADD",true)){
            cancel.setText("Cancel");
            delete.setVisibility(View.GONE);
            repeat_off.setChecked(true);
            kind = false;
        }
        else
        {//(i.getBooleanExtra("ADD",false)){

            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                timePicker.setHour(Integer.parseInt(i.getStringExtra("HOUR")));
            }
            //Current
            else {
                timePicker.setCurrentHour(Integer.parseInt(i.getStringExtra("HOUR")));
            }
            String m1 = i.getStringExtra("MINUTE");
            m1 = m1.substring(0,m1.length() - 1);
            if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                timePicker.setMinute(Integer.parseInt(m1));
            }else {
                timePicker.setCurrentMinute(Integer.parseInt(m1));
            }
            initial_hour = Integer.parseInt(i.getStringExtra("HOUR"));
            initial_minute = Integer.parseInt(m1);
            cancel.setVisibility(View.GONE);
            set.setText("Save Changes");
            get_Repeat();
            kind = true;
        }

        songs();
        snooze = i.getStringExtra("Snooze");
        if(snooze != null) {
            if (snooze.equals("SNOOZE")) {
                intent.putExtra("KIND", "Stop");
                id = getID();
                sendBroadcast(intent);
                intent.putExtra("KIND", "Start");
                intent.putExtra("SONG",0);
                pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                60 * 5000, pendingIntent);
                finish();
            }
        }

        repeat_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat = true;
            }
        });

        repeat_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repeat = false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                song = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                song = 0;
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentApiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    //c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                if (kind) {
                    if (hour != initial_hour || minute != initial_minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(calendar.HOUR_OF_DAY, initial_hour);
                        calendar.set(calendar.MINUTE, initial_minute);
                        calendar.set(Calendar.SECOND, 0);
                        intent.putExtra("KIND", "Stop");
                        id = getID();
                        pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        sendBroadcast(intent);
                        calendar.set(calendar.HOUR_OF_DAY, hour);
                        calendar.set(calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        new_song();
                        int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int MINUTE = Calendar.getInstance().get(Calendar.MINUTE);
                        //Toast.makeText(Set_Alarms.this, HOUR + hour + "", Toast.LENGTH_SHORT).show();
                        Log.e("THE WORD: ", Integer.toString(hour) +" " + Integer.toString(minute) + " " + Integer.toString(HOUR) + " " +Integer.toString(MINUTE));
                        if(hour < HOUR){
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                        }
                        else if(HOUR == hour && minute < MINUTE){
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                        }
                        intent.putExtra("KIND", "Start");
                        intent.putExtra("SONG",song);
                        id = getID();
                        pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if(repeat) {
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                        else if(!repeat) {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("KIND", 1);
                        //editor.putString("email", email.getText().toString());
                        editor.commit();
                    }

                    else {
                        //   Toast.makeText(getBaseContext(),"yo2",Toast.LENGTH_SHORT).show();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(calendar.HOUR_OF_DAY, initial_hour);
                        calendar.set(calendar.MINUTE, initial_minute);
                        calendar.set(Calendar.SECOND, 0);

                        int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int MINUTE = Calendar.getInstance().get(Calendar.MINUTE);

                        Log.e("THE WORD: ", Integer.toString(hour) +" " + Integer.toString(minute) + " " + Integer.toString(HOUR) + " " +Integer.toString(MINUTE));
                        if(hour < HOUR){
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                        }
                        else if(HOUR == hour && minute < MINUTE){
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                        }
                        intent.putExtra("KIND", "Start");
                        new_song();
                        intent.putExtra("SONG",song);
                        id = getID();
                        //   Toast.makeText(getBaseContext(),Integer.toString(id),Toast.LENGTH_SHORT).show();
                        pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        // alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        if(repeat) {
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                        else if(!repeat) {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        }
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("KIND", 1);
                        editor.commit();
                    }
                    try {
                        OutputStreamWriter outputStreamWriter11 = new OutputStreamWriter(getBaseContext().openFileOutput("Type.txt", Context.MODE_APPEND));
                        outputStreamWriter11.write(Integer.toString(position) +  ".\n");
                        outputStreamWriter11.close();
                    }
                    catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }

                    try {
                        String r = readFromFile(getBaseContext());
                        //  Toast.makeText(getBaseContext(), r, Toast.LENGTH_SHORT).show();
                        OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(getBaseContext().openFileOutput("Names.txt", Context.MODE_PRIVATE));
                        outputStreamWriter1.close();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("Names.txt", Context.MODE_APPEND));
                        int i = 0;

                        while (!r.equals("")) {
                            if (i != position) {

                                outputStreamWriter.write(r.substring(0, r.indexOf(".")) + ".\n");
                            } else {
                                outputStreamWriter.write(hour + ":" + minute + ".\n");
                            }
                            r = r.substring(r.indexOf(".") + 1, r.length());
                            i++;
                        }
                        outputStreamWriter.close();
                        Intent i01 = new Intent(Set_Alarms.this, MainActivity.class);
                        startActivity(i01);
                        finish();
                    } catch (Exception e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                    if(repeat){
                        write_repeat(1);
                    }
                    else{
                        write_repeat(0);
                    }
                } else if(!kind){
                    //  Toast.makeText(getBaseContext(),"yo", Toast.LENGTH_SHORT).show();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.HOUR_OF_DAY, hour);
                    calendar.set(calendar.MINUTE, minute);
                    Calendar c = Calendar.getInstance();
                    int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    int MINUTE = Calendar.getInstance().get(Calendar.MINUTE);
                    Log.e("THE WORD: ", Integer.toString(hour) +" " + Integer.toString(minute) + " " + Integer.toString(HOUR) + " " +Integer.toString(MINUTE));
                    if(hour < HOUR){
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                    }
                    else if(HOUR == hour && minute < MINUTE){
                        calendar.add(Calendar.DAY_OF_MONTH,1);
                    }
                    intent.putExtra("KIND", "Start");
                    new_song();
                    intent.putExtra("SONG",song);
                    //  Toast.makeText(getBaseContext(),Integer.toString(song),Toast.LENGTH_SHORT).show();
                    id = getID();
                    Log.e("ID before: ", Integer.toString(id));
                    Write_to_file(Integer.toString(id));
                    putInt(id);
                    int in = getID();
                    Log.e("ID after: ", Integer.toString(in));
                    pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if(repeat) {
                        Log.e("In", "repeat");
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        write_repeat(1);
                    }
                    else if(!repeat) {
                        Log.e("OUT", "repeat");
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        write_repeat(0);
                    }
                    writeToFile(Integer.toString(hour), Integer.toString(minute), getBaseContext());
                    Intent i = new Intent(Set_Alarms.this, MainActivity.class);
                    //i.putExtra("FUN",true);
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("KIND", 1);
                    //editor.putString("email", email.getText().toString());
                    editor.commit();

                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("Type.txt", Context.MODE_APPEND));
                        outputStreamWriter.write(Integer.toString(count) + ".\n");
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                    startActivity(i);
                    finish();
                }
            }
        });




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!kind) {
                    Intent i1 = new Intent(Set_Alarms.this, MainActivity.class);
                    startActivity(i1);
                    finish();
                }
                else{
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.HOUR_OF_DAY, hour);
                    calendar.set(calendar.MINUTE, minute);
                    intent.putExtra("KIND","Stop");
                    id = (int) System.currentTimeMillis();
                    pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    sendBroadcast(intent);
                    Intent i1 = new Intent(Set_Alarms.this, MainActivity.class);
                    startActivity(i1);
                    finish();
                    //  Toast.makeText(getBaseContext(),"yo",Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.HOUR_OF_DAY, hour);
                calendar.set(calendar.MINUTE, minute);
                intent.putExtra("KIND","Stop");
                id = get_del_ID();
                Log.e("ID deleted", Integer.toString(id));
                pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this,id,intent, PendingIntent.FLAG_UPDATE_CURRENT);

                delete_ID(position);

                alarmManager.cancel(pendingIntent);
                sendBroadcast(intent);
                del();
                delete_repeat(position);
                delete_song();
                //  Toast.makeText(getBaseContext(),"yo",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMessageOKCancel(String message) {
        Log.d("Log", "#5 INFO: call AlertDialog");

        new AlertDialog.Builder(Set_Alarms.this)
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
                    ActivityCompat.requestPermissions( Set_Alarms.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_STORAGE);
                    dialog.dismiss();
                    break;
            }
        }
    };

    public void write_repeat(int data)
    {
        try {
            if (!kind) {
                String l = read_repeats();
                //    Toast.makeText(getBaseContext(), l, Toast.LENGTH_SHORT).show();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("Repeat_IDs.txt", Context.MODE_APPEND));
                outputStreamWriter.write(data + ".\n");
                outputStreamWriter.close();
                l = read_repeats();
                //     Toast.makeText(getBaseContext(), l, Toast.LENGTH_SHORT).show();
            }

            if(kind) {
                String repeats = read_repeats();
                OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(getBaseContext().openFileOutput("Repeat_IDs.txt", Context.MODE_PRIVATE));
                outputStreamWriter1.close();
                //  Toast.makeText(getBaseContext(), repeats, Toast.LENGTH_SHORT).show();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("Repeat_IDs.txt", Context.MODE_APPEND));
                if (repeats.equals("")) {
                    outputStreamWriter.write(Integer.toString(data) + ".\n");
                    outputStreamWriter.close();
                } else {
                    int i_ = 0;
                    while (!repeats.equals("")) {
                        if (i_ != position) {
                            // Toast.makeText(getBaseContext(), "hello1", Toast.LENGTH_SHORT).show();
                            outputStreamWriter.write(repeats.substring(0, repeats.indexOf(".")) + ".\n");
                        }
                        if (i_ == position) {
                            // Toast.makeText(getBaseContext(), "hello2", Toast.LENGTH_SHORT).show();
                            outputStreamWriter.write(Integer.toString(data) + ".\n");
                        }
                        i_++;
                        repeats = repeats.substring(repeats.indexOf(".") + 1, repeats.length());
                    }
                    outputStreamWriter.close();
                }
                repeats = read_repeats();
                // Toast.makeText(getBaseContext(), repeats, Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public void songs()
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME1, 0);
        String s = settings.getString("SOND_ID","0");
        // Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();

        if(!kind){
            spinner.setSelection(0);
        }
        else{
            int i = 0;
            String ss = "";
            while(!s.equals("")){
                ss = s.substring(0,s.indexOf("."));
                if(i == position){
                    spinner.setSelection(Integer.parseInt(ss));
                }
                i++;
                s = s.substring(s.indexOf(".") + 1, s.length());
            }
        }
    }

    public void delete_song()
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME1, 0);
        SharedPreferences.Editor editor = settings.edit();
        String s = settings.getString("SOND_ID","0");

        int i = 0;
        String new_s = "";
        while(!s.equals("")){
            if(i != position){
                new_s += s.substring(0,s.indexOf(".")) + ".";
            }
            i++;
            s = s.substring(s.indexOf(".")+ 1 , s.length());
        }

        editor.putString("SOND_ID",new_s);
        editor.commit();
    }

    public void new_song()
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME1, 0);
        SharedPreferences.Editor editor = settings.edit();
        String s = settings.getString("SOND_ID","0");
        // Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();

        if(s.equals("0")) {
            String ss = Integer.toString(song) + ".";
            editor.putString("SOND_ID", ss);

            editor.commit();
            String ks = settings.getString("SOND_ID","0");
            //  Toast.makeText(getBaseContext(),ks,Toast.LENGTH_SHORT).show();
        }
        else if (!kind){
            editor.putString("SOND_ID",s + Integer.toString(song) + ".");
            editor.commit();
        }
        else{
            int i = 0;
            String new_s = "";
            while(!s.equals("")){
                if(i != position){
                    new_s += s.substring(0,s.indexOf(".")) + ".";
                }
                else{
                    new_s += Integer.toString(song) + ".";
                    //spinner.setSelection(song);
                }
                i++;
                s = s.substring(s.indexOf(".")+ 1 , s.length());
            }

            editor.putString("SOND_ID",new_s);
            editor.commit();
        }
    }


    public void get_Repeat()
    {
        try {
            InputStream inputStream = getBaseContext().openFileInput("Repeat_IDs.txt");
            int _i = 0;
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    if(_i == position){
                        if(Integer.parseInt(receiveString.substring(0,receiveString.indexOf(".")))== 0){
                            repeat_off.setChecked(true);
                        }
                        else{
                            repeat_on.setChecked(true);
                        }
                    }
                    _i++;
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    public String read_repeats()
    {
        String ret = "";
        try {
            InputStream inputStream = getBaseContext().openFileInput("Repeat_IDs.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
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


    public int getID()
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME1, 0);
        int ID = settings.getInt("IDS", -1);
        return ID;
    }

    public int get_del_ID()
    {
        int po = 0;
        String res = "";
        try {
            InputStream inputStream = getBaseContext().openFileInput("IDs.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    if(po == position){
                        res = receiveString;
                    }
                    po++;
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                res = res.substring(0,res.length() - 1);
                //     Toast.makeText(getBaseContext(),res,Toast.LENGTH_SHORT).show();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return Integer.parseInt(res);
    }

    public void putInt(int x){
        x = x + 1;
        Log.e("Value in putint",Integer.toString(x));
        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME1, 0);
        SharedPreferences.Editor editor = settings1.edit();
        editor.putInt("IDS", x);
        editor.commit();
    }

    public void Write_to_file(String dat){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("IDs.txt", Context.MODE_APPEND));
            outputStreamWriter.write(dat + ".\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void delete_ID(int pos)
    {
        String k = read_ID_From_File();
        Log.e("Reading id file",k);
        int po = 0;
        try {
            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(getBaseContext().openFileOutput("IDs.txt", Context.MODE_PRIVATE));
            outputStreamWriter1.close();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("IDs.txt", Context.MODE_APPEND));
            while(!k.equals("")){
                String k_sub = k.substring(0,k.indexOf("."));
                Log.e("K_sub",k_sub);
                if(po != pos){
                    outputStreamWriter.write(k_sub + ".\n");
                }
                po++;
                k = k.substring(k.indexOf(".") + 1, k.length());
            }
            outputStreamWriter.close();
            String k1 = read_ID_From_File();
            Log.e("Reading after","ok" + k1);
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void delete_repeat(int pos)
    {
        String k = read_repeats();
        Log.e("Reading id file",k);
        int po = 0;
        try {
            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(getBaseContext().openFileOutput("Repeat_IDs.txt", Context.MODE_PRIVATE));
            outputStreamWriter1.close();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("Repeat_IDs.txt", Context.MODE_APPEND));
            while(!k.equals("")){
                String k_sub = k.substring(0,k.indexOf("."));
                if(po != pos){
                    outputStreamWriter.write(k_sub + ".\n");
                }
                po++;
                k = k.substring(k.indexOf(".") + 1, k.length());
            }
            outputStreamWriter.close();
            String k1 = read_ID_From_File();
            Log.e("Reading after","ok" + k1);
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public String read_ID_From_File()
    {
        String ret = "";
        try {
            InputStream inputStream = getBaseContext().openFileInput("IDs.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent01 = new Intent(Set_Alarms.this, MainActivity.class);
            startActivity(intent01);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick2()
    {
        if(!kind) {
            Intent i1 = new Intent(Set_Alarms.this, MainActivity.class);
            startActivity(i1);
            finish();
        }
        else{
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.HOUR_OF_DAY, hour);
            calendar.set(calendar.MINUTE, minute);
            intent.putExtra("KIND","Stop");
            id = (int) System.currentTimeMillis();
            pendingIntent = PendingIntent.getBroadcast(Set_Alarms.this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            sendBroadcast(intent);
            Intent i1 = new Intent(Set_Alarms.this, MainActivity.class);
            startActivity(i1);
            finish();
        }
    }

    public void del()
    {
        String res = readFromFile(getBaseContext());
        int ii = 0;

        // Toast.makeText(getBaseContext(),res.substring(0,res.indexOf(".") + 1),Toast.LENGTH_SHORT).show();

        try {
            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(getBaseContext().openFileOutput("Names.txt", Context.MODE_PRIVATE));
            outputStreamWriter1.close();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getBaseContext().openFileOutput("Names.txt", Context.MODE_APPEND));
            while(!res.equals("")) {
                String res1 = res.substring(0,res.indexOf(".") + 1);
                if(ii != position)
                    outputStreamWriter.write(res1 + "\n");
                res = res.substring(res.indexOf(".") + 1, res.length());
                ii++;
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        String str = readFromFile1(getBaseContext());

        int iii = 0;

        try {
            OutputStreamWriter outputStreamWriter01 = new OutputStreamWriter(getBaseContext().openFileOutput("Type.txt", Context.MODE_PRIVATE));
            outputStreamWriter01.close();
            OutputStreamWriter outputStreamWriter11 = new OutputStreamWriter(getBaseContext().openFileOutput("Type.txt", Context.MODE_APPEND));
            while(!str.equals("")) {
                int x = Integer.parseInt(str.substring(0,str.indexOf(".")));
                if(x < position){
                    outputStreamWriter11.write(str.substring(0,str.indexOf(".")) + ".\n");
                }
                else if(x > position){
                    int k = Integer.parseInt(str.substring(0,str.indexOf(".")));
                    k--;
                    outputStreamWriter11.write(Integer.toString(k) + ".\n");
                }
                iii++;
                str = str.substring(str.indexOf(".") + 1,str.length());
            }
            outputStreamWriter11.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        Intent i1 = new Intent(Set_Alarms.this, MainActivity.class);
        startActivity(i1);
        finish();
    }

    private void writeToFile(String hour,String minute, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Names.txt", Context.MODE_APPEND));
            outputStreamWriter.write(hour + ":" + minute + ".\n");//pm am
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

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

    private void saveFileInPublicDirectory(String fileName){
        Bitmap myImage = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode);
        String MEDIA_MOUNTED = "mounted";
        String diskState = Environment.getExternalStorageState();
        if(diskState.equals(MEDIA_MOUNTED)){
            File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File filePicture = new File(pictureFolder, fileName);
            FileOutputStream out = null;
            try{
                out = new FileOutputStream(filePicture);
                myImage.compress(Bitmap.CompressFormat.JPEG, 80, out);
                out.close();
                Uri selectedUri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "*/*");
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(Set_Alarms.this).
                        setSmallIcon(R.drawable.qrcode).
                        setContentTitle("qrcode.jpeg Downloaded").
                        setVibrate(new long[]{0,300,300}).
                        setContentText("qrcode.jpeg is in Download folder").
                        setLights(Color.WHITE,1000,5000).
                        setAutoCancel(true).
                        setPriority(NotificationCompat.PRIORITY_HIGH).
                        addAction(R.drawable.iconalarm, "qrcode.jpeg", pIntent).
                        setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);
                notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0,notification.build());
                Toast.makeText(this, "qrcode.jpeg downloaded", Toast.LENGTH_LONG).show();
            }catch(IOException e)
            {
                Toast.makeText(this, "Problem saving to the public file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "The external disk is not mounted", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Log", "#3 INFO: received a response permission was granted");
                    Toast.makeText(this, "Please print the QR code You will need it to turn off the alarm", Toast.LENGTH_LONG).show();

                } else {

                    Log.d("Log", "#4 INFO: received a response permission - denied");
                    Toast.makeText(this, "Please print the QR code You will need it to turn off the alarm", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(

                            Set_Alarms.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_STORAGE);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
