package com.navjot.faceproject.alarmforhealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;


public class Profile extends AppCompatActivity {
    DBAdapter myDb;
    DBAdapter1 myDb1;
    Button exit;
    Button back,walk;
    Button pushup;
    Double bmii;
    int id;
    ImageView imgview;
    long wakeup;
    TextView timetowakeup, bmi, quote, namee;
    String fname, lname, weight, height1, walkprofile, pushupsprofile, imgimg;
    private int themeid;
    RelativeLayout rel;
    int a=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*1),(int)(height*1));
        readFromFile(getBaseContext());
        //Toast.makeText(this, readFromFile(getBaseContext())+"Read file", Toast.LENGTH_SHORT).show();
        Log.d(readFromFile(getBaseContext()),"read file");
        String readfiltimewakeup = (readFromFile(getBaseContext()));
        timetowakeup = (TextView)findViewById(R.id.textView4);
        bmi = (TextView)findViewById(R.id.textView3);
        pushup = (Button)findViewById(R.id.button10);
       // imgbtn = (ImageButton) findViewById(R.id.imageButton2);
        walk = (Button)findViewById(R.id.button9);
        namee = (TextView) findViewById(R.id.textView5);
        imgview = (ImageView) findViewById(R.id.imageView4);
        myDb1 = new DBAdapter1(this);
        myDb1.open1();
        Cursor cursor1 = myDb1.getAllRows1();
        //Toast.makeText(this, cursor1.getCount()+"cursor count", Toast.LENGTH_SHORT).show();
        if(cursor1.getCount() == 0){
            //hello
        }else {
            id = cursor1.getInt(DBAdapter1.COL_ROWID);
            imgimg = cursor1.getString(DBAdapter1.COL_IMAGE);
            //byte[] arrimage = cursor1.getBlob(1);
            //imgview.setImageBitmap(getImage(arrimage));
            if(imgimg.equals("Male"))
            {
                imgview.setBackgroundResource(R.drawable.male);
            }
            else if(imgimg.equals("Female"))
            {
                imgview.setBackgroundResource(R.drawable.female);
            }
            else
            {

            }
        }
        readFromFilewakeup(getBaseContext());
        quote = (TextView)findViewById(R.id.textView16);

        rel = (RelativeLayout) findViewById(R.id.activity_profile);

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

        myDb1.close1();
        DecimalFormat df = new DecimalFormat("###.##");

        exit = (Button)findViewById(R.id.button16);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                //Toast.makeText(Profile.this, "App Exit", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
            }
        });

        back = (Button)findViewById(R.id.button15);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Profile.this,MainActivity.class);
                //startActivity(intent);
                a=1;
                finish();
            }
        });



        openDB();
        Cursor cursor = myDb.getAllRows();
        if(cursor.getCount() == 0){
            //hello
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



        pushup = (Button)findViewById(R.id.button10);
        pushup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,Pushups.class);
                startActivity(intent);
                a=1;
                onPause();
            }
        });

        try {
            Intent pushupintent = getIntent();
            pushupintent.getStringExtra("pushup");
            pushup.setText("Total Pushups "+pushupsprofile);
        }catch(NullPointerException ex){
            Log.d("Push up null pointer",""+ex);
        }

        if(pushupsprofile==null){
            pushup.setText("CLICK AND PUSHUP");
        }
        else if(pushupsprofile.equals("")){
            pushup.setText("CLICK AND PUSHUP");
        }
        else{
            pushup.setText("Total Pushups "+pushupsprofile);
        }
        if(walkprofile==null){
            walk.setText("Click and Walk");
        }
        else if(walkprofile.equals("0.0")){
            walk.setText("Click and Walk");
        }
        else{
            try {
                String walkk = walkprofile.substring(0, 6);
                walk.setText("Last Walk " + walkk + " KM");
            }
            catch(IndexOutOfBoundsException ex){
                ex.printStackTrace();
                String walkk = walkprofile.substring(0, walkprofile.length());
                walk.setText("Last Walk " + walkk + " KM");
            }
            //Toast.makeText(this, walkprofile+" walk profile", Toast.LENGTH_SHORT).show();
        }


        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this,MapsActivity.class));
                a=1;
                onPause();
            }
        });

        if(fname==null&&lname==null&&wakeup==0&&weight==null&&height1==null){

        }
        else if(fname.equals("")&&lname.equals("")&&wakeup==0&&weight.equals("")&&height1.equals("")){

        }
        else {
            double pounddou = (Double.parseDouble(weight));
            double inchdou = (Double.parseDouble(height1));
            try {
                Log.d("Double " + pounddou, " " + inchdou);
                bmii = (pounddou / (inchdou * inchdou)) * 703;
            } catch (NumberFormatException ex) {
                Log.d("Numberformatexception", "ex" + ex);
            }
            namee.setText(fname + " " + lname);
            bmi.setText("Body Mass Index : " +""+df.format(bmii));
        }
        try {
            Double waketime = Double.parseDouble(readfiltimewakeup);
            Double mindouble = waketime / (1000 * 60);
            timetowakeup.setText("Time took to wakeup : " + df.format(mindouble) + "min");
        }
        catch(RuntimeException ex){

            timetowakeup.setText("Time took to wakeup : ");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(a==1){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("config.txt");
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

    private String readFromFilewakeup(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("config.txt");
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
}
