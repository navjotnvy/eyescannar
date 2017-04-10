package com.navjot.faceproject.alarmforhealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CreateProfile extends AppCompatActivity {

    long wakey;
    EditText fname,lname,weight,height;
   // ImageButton imgbtn;
    DBAdapter myDb;
    DBAdapter1 myDb1;
    Button save, back;
    Spinner picspinner;
    String spinnerresult;
    //private int themeid;
    LinearLayout rel;
    int a=0;
  //  Drawable d,e;
   // Bitmap bitmap;
   // Uri Selected_Image_Uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        fname = (EditText)findViewById(R.id.editText3);
        lname = (EditText)findViewById(R.id.editText6);
        weight = (EditText)findViewById(R.id.editText4);
        height = (EditText)findViewById(R.id.editText5);
        String wakeuptime = readFromFile(getBaseContext());
        back = (Button)findViewById(R.id.button7);
        picspinner = (Spinner) findViewById(R.id.spinner2);
/*
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice(Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID))  // My Galaxy Nexus test phone
                .build();
        mAdView.loadAd(request);
*/
        if(wakeuptime.equals(""))
        {

        }else {
            wakey = Long.parseLong(wakeuptime);
        }
        save = (Button) findViewById(R.id.button4);
        //if in future you thing to use image button then this is the code
       /* imgbtn = (ImageButton) findViewById(R.id.imageButton2);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 256);
                intent.putExtra("outputY", 256);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 1);
            }
        });*/
        rel = (LinearLayout) findViewById(R.id.activity_create_profile);

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Drawable a = ResourcesCompat.getDrawable(getResources(), R.drawable.logo1, null);
                Drawable drawable = this.getResources().getDrawable(R.drawable.logo1);
                Toast.makeText(CreateProfile.this, a+ " Hey " +d, Toast.LENGTH_SHORT).show();*/
                myDb1 = new DBAdapter1(CreateProfile.this);
                myDb1.open1();
                Cursor cursor1 = myDb1.getAllRows1();
                spinnerresult = picspinner.getSelectedItem().toString();
                //
                // Toast.makeText(CreateProfile.this, "Spiner result "+fname.getText(), Toast.LENGTH_SHORT).show();

                if(spinnerresult.equals("Choose an Avatar"))
                {
                    Toast.makeText(CreateProfile.this, "Please Select an avatar", Toast.LENGTH_SHORT).show();
                }
                else if((fname.getText().toString()).equals(""))
                {
                    Toast.makeText(CreateProfile.this, "First name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if((lname.getText().toString()).equals(""))
                {
                    Toast.makeText(CreateProfile.this, "Last name field is empty", Toast.LENGTH_SHORT).show();
                }
                else if(((weight.getText().toString()).equals(""))||((weight.getText().toString()).equals(".")))
                {
                    Toast.makeText(CreateProfile.this, "Enter correct Weight", Toast.LENGTH_SHORT).show();
                }
                else if(((height.getText().toString()).equals(""))||((height.getText().toString()).equals(".")))
                {
                    Toast.makeText(CreateProfile.this, "Enter correct Height", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(spinnerresult.equals("Male"))
                    {
                        if(cursor1.getCount() == 0) {
                            myDb1.insertRow1(spinnerresult);
                        }
                        else{
                            long id = cursor1.getLong(DBAdapter1.COL_ROWID);
                            myDb1.updateRow1(id,spinnerresult);
                        }
                    }
                    else if(spinnerresult.equals("Female"))
                    {
                        if(cursor1.getCount() == 0) {
                            myDb1.insertRow1(spinnerresult);
                        }
                        else{
                            long id = cursor1.getLong(DBAdapter1.COL_ROWID);
                            myDb1.updateRow1(id,spinnerresult);
                        }
                    }

                    onClick_AddRecord(view);
                    a=1;
                    Intent intent = new Intent(CreateProfile.this,Profile.class);
                    startActivity(intent);
                }
            /*    Log.d("BITMAP ",bitmap+"");
//                getBytes(bitmap);
                Cursor cursor1 = myDb1.getAllRows1();
                if(cursor1.getCount() == 0) {
                    myDb1.insertRow1(getBytes(bitmap));
                }
                else{
                    long id = cursor1.getLong(DBAdapter1.COL_ROWID);
                    myDb1.updateRow1(id,getBytes(bitmap));
                }
                getImage(getBytes(bitmap));
                Log.d("getImage",""+getImage(getBytes(bitmap)));*/


                myDb1.close1();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent backintent = new Intent(CreateProfile.this, MainActivity.class);
                a=1;
                //startActivity(backintent);
                finish();
            }
        });

        openDB();
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            closeDB();
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

    public void onClick_AddRecord(View v) {
        Cursor cursor = myDb.getAllRows();
        if(cursor.getCount() == 0){
            long newId = myDb.insertRow(fname.getText().toString(), lname.getText().toString(), wakey, weight.getText().toString(), height.getText().toString(),"","");
            cursor = myDb.getRow(newId);
        }
        else
        {
            long id = cursor.getLong(DBAdapter.COL_ROWID);
            myDb.updateRow(id,fname.getText().toString(), lname.getText().toString(), wakey, weight.getText().toString(), height.getText().toString(),"","");
        }
        // Query for the record we just added to confirm.
        // Use the ID:
        //Toast.makeText(CreateProfile.this, "onClick method ran", Toast.LENGTH_SHORT).show();
        //String message = "";
        cursor.close();
        onClick_DisplayRecords(v);
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    //for future purpose to get image and set image and keep it in database
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

  /*  public void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode, resCode, data);

       // d = imgbtn.getBackground();

//        bitmap = ((BitmapDrawable)d).getBitmap();

        Log.d("Hello log","");
        if((resCode==RESULT_OK))
        {
            Log.d("resCode","Hi");
            if(reqCode==1) {
                getBytes(bitmap);
                getImage(getBytes(bitmap));
                Log.d("Get Image",getImage(getBytes(bitmap))+"");
                imgbtn.setImageBitmap(getImage(getBytes(bitmap)));
                //imgbtn.setImageURI();
                //Selected_Image_Uri = data.getData();
               // imgbtn.setImageURI(Selected_Image_Uri);
            }
        }
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Selected_Image_Uri);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
      /*super.onActivityResult(reqCode, resCode, data);
        try {
            // When an Image is picked
            if (reqCode == 1 && resCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                /*ImageView imgView = (ImageView) findViewById(R.id.imgView);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                imgbtn.setImageURI(data.getData());

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }*/



    @Override
    protected void onPause() {
        super.onPause();
        if(a==1){
            finish();
        }
    }

    public void onClick_DisplayRecords(View v) {
        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);
    }

    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor
        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String fname = cursor.getString(DBAdapter.COL_FNAME);
                String lname = cursor.getString(DBAdapter.COL_LNAME);
                int wakeup = cursor.getInt(DBAdapter.COL_WAKEUP);
                String weight = cursor.getString(DBAdapter.COL_WEIGHT);
                String height = cursor.getString(DBAdapter.COL_HEIGHT);
                String walkss = cursor.getString(DBAdapter.COL_WALK);
                String pushupss = cursor.getString(DBAdapter.COL_PUSHUPS);

                // Append data to the message:
                message += "id=" + id
                        +", fname=" + fname
                        +", lname=" + lname
                        +", #=" + wakeup
                        +", weight=" + weight
                        +", height" + height
                        +", walk" + walkss
                        +", pushups" + pushupss
                        +"\n";
                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
                //Toast.makeText(this, message+"", Toast.LENGTH_SHORT).show();
            } while(cursor.moveToNext());
        }
        // Close the cursor to avoid a resource leak.
        cursor.close();
        // [TO_DO_B7]
        // Update the list view
        // [TO_DO_B8]
        // Display a Toast message
    }
}
