package com.navjot.faceproject.alarmforhealth;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Adapter  extends ArrayAdapter<String> {
    private Activity context;
    public static final String PREFS_NAME = "MyPreferenceFile";
    private int id;
    int p, song;
    AlarmManager alarmManager;
    boolean repeat;
    public static final String PREFS_NAME1 = "MyPreferenceFile1";

    public Adapter(Activity context, int resource, List<String> objects)  {
        super(context, resource, objects);
        this.context = context;
        id = resource;
    }

    ArrayList<Integer> switches = new ArrayList();
    ArrayList<Integer> list = new ArrayList();

    int pos = 0;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainviewholder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(id, parent,false);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.aSwitch = (Switch) convertView.findViewById(R.id.switchbtn);

            pos = position;
            viewHolder.aSwitch.setTag(position);
            viewHolder.aSwitch.setOnCheckedChangeListener(null);

            String ret = readFromFile(getContext());
            int i = 0;
            while(!ret.equals("")){
                if(ret.substring(0,ret.indexOf(".")).equals(Integer.toString(position))){
                    i = 1;
                    break;
                }
                ret = ret.substring(ret.indexOf(".") + 1,ret.length());
            }

            if(i == 1){
                viewHolder.aSwitch.setChecked(true);
            }
            else{
                viewHolder.aSwitch.setChecked(false);
            }

            viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()  {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        list.add(position);
                        notifyDataSetChanged();
                        writeToFile(Integer.toString(position),getContext());
                        String time = getItem(position);
                        pos = position;
                        get_Repeat();

                        int hour = Integer.parseInt(time.substring(0,time.indexOf(":")));
                        int minute = Integer.parseInt(time.substring(time.indexOf(":") + 1,time.length() - 1));
                        // Toast.makeText(getContext(),Integer.toString(hour) + Integer.toString(minute),Toast.LENGTH_SHORT).show();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(calendar.HOUR_OF_DAY, hour);
                        calendar.set(calendar.MINUTE, minute);
                        int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int MINUTE = Calendar.getInstance().get(Calendar.MINUTE);

                        Log.e("THE WORD: ", Integer.toString(hour) +" " + Integer.toString(minute) + " " + Integer.toString(HOUR) + " " +Integer.toString(MINUTE));
                        if(hour < HOUR){
                            calendar.add(Calendar.DAY_OF_MONTH,1);
                        }
                        else if(HOUR == hour && minute < MINUTE){

                            calendar.add(Calendar.DAY_OF_MONTH,1);
                        }
                        final Intent intent = new Intent(context, Alarms.class);
                        intent.putExtra("KIND", "Start");

                        id = get_ID(position);
                        Log.e("ID after restart:", Integer.toString(id));
                        p = position;
                        songs();
                        intent.putExtra("SONG",song);
                        PendingIntent pendingIntent;
                        pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                        //  alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        if(repeat) {
                            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }

                        else if(!repeat) {

                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        }

                        //Toast.makeText(getContext(),"Alarm on!",Toast.LENGTH_SHORT).show();
                        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("KIND", 1);
                        //editor.putString("email", email.getText().toString());
                        editor.commit();

                    }
                    else{
                        //Toast.makeText(getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
                        String time = getItem(position);
                        int hour = Integer.parseInt(time.substring(0,time.indexOf(":")));
                        int minute = Integer.parseInt(time.substring(time.indexOf(":") + 1,time.length() - 1));
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(calendar.HOUR_OF_DAY, hour);
                        calendar.set(calendar.MINUTE, minute);
                        Intent intent = new Intent(context, Alarms.class);
                        intent.putExtra("KIND","Stop");
                        id = get_ID(position);
                        Log.e("ID after off:", Integer.toString(id));
                        PendingIntent pendingIntent;
                        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(context,id,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        context.sendBroadcast(intent);
                        //Toast.makeText(getContext(),"Alarm off!",Toast.LENGTH_SHORT).show();
                        String re = readFromFile(getContext());
                        try{
                            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(context.openFileOutput("Type.txt", Context.MODE_PRIVATE));
                            outputStreamWriter1.close();
                        }
                        catch (IOException e){
                            Log.e("Cannot read from file: ","Type.txt");
                        }
                        while(!re.equals("")) {
                            if (!re.substring(0, re.indexOf(".")).equals(Integer.toString(position))) {
                                writeToFile2(re.substring(0, re.indexOf(".")),getContext());
                            }
                            re = re.substring(re.indexOf(".") + 1 , re.length());
                        }
                    }
                }
            });
            viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String current = getItem(position);
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("ADD",false);
                    intent.putExtra("POSITION",position);
                    //   intent.putExtra("DEL_NUMBER",cur);
                    String hour = current.substring(0,current.indexOf(":"));
                    String minute = current.substring(current.indexOf(":") + 1,current.length());
                    intent.putExtra("HOUR", hour);
                    intent.putExtra("MINUTE", minute);
                    intent.putExtra("FUN", true);
                    //Toast.makeText(getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
                    context.startActivity(intent);
                    context.finish();
                }
            });




            convertView.setTag(viewHolder);

            mainviewholder = (ViewHolder)convertView.getTag();
            mainviewholder.textView.setText(getItem(position));
        }

        else{
            mainviewholder = (ViewHolder)convertView.getTag();
            mainviewholder.textView.setText(getItem(position));
        }





        return convertView;
    }

    public void songs()
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME1, 0);
        String s = settings.getString("SOND_ID","0");
        // Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();

        int i = 0;
        String ss = "";
        while(!s.equals("")){
            ss = s.substring(0,s.indexOf("."));
            if(i == p){
                song = Integer.parseInt(ss);
            }
            i++;
            s = s.substring(s.indexOf(".") + 1, s.length());
        }
    }



    public void get_Repeat()
    {
        try {
            InputStream inputStream = getContext().openFileInput("Repeat_IDs.txt");
            int _i = 0;
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    if(_i == pos){
                        if(Integer.parseInt(receiveString.substring(0,receiveString.indexOf(".")))== 0){
                            repeat = false;
                        }
                        else{
                            repeat = true;
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


    public int get_ID(int pos)
    {
        int p = 0;
        String ID = "";
        try {
            InputStream inputStream = context.openFileInput("IDs.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    if(p == pos){
                        ID = receiveString;
                    }
                    p++;
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                ID = ID.substring(0,ID.length() - 1);

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return Integer.parseInt(ID);
    }


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Type.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data+".\n");
            //outputStreamWriter.write("");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void writeToFile2(String data,Context context) {
        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Type.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data+".\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

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


    public class ViewHolder {
        TextView textView;
        Switch aSwitch;

    }
}