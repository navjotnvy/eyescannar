package com.navjot.faceproject.alarmforhealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class settings extends AppCompatActivity {
    Button exit;
    Button back, calc, theme,disclaim,qrc;
    private int themeid;
    RelativeLayout rel;
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*1),(int)(height*1));

        rel = (RelativeLayout) findViewById(R.id.activity_settings);

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

        disclaim = (Button)findViewById(R.id.button19);
        disclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this,Disclaimer.class);
                startActivity(intent);
            }
        });


        calc = (Button)findViewById(R.id.button18);
        Intent inte = getIntent();
        String Bmi = inte.getStringExtra("bmi");
       try {
           Double Bmidou = Double.parseDouble(Bmi);

           Log.d("BMIDOU",Bmidou+"");
            if (Bmidou==0) {
                Log.d("if BMIDOU",""+Bmidou);
            } else {
                Log.d("else BMIDOU",""+Bmidou);
                if (Bmidou <= 18.5) {
                    calc.setText("Under Weight, BMI: " + String.format( "%.2f", Bmidou ));
                } else if (Bmidou > 18.5 && Bmidou <= 24.9) {
                    calc.setText("Normal Weight, BMI: " + String.format( "%.2f", Bmidou ));
                } else if (Bmidou > 25 && Bmidou <= 29.9) {
                    calc.setText("OverWeight, BMI: " + String.format( "%.2f", Bmidou ));
                } else if (Bmidou >= 30) {
                    calc.setText("Obesity, BMI: " + String.format( "%.2f", Bmidou ));
                }else{
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }catch(NullPointerException ex){
            Log.d("Exception","ex"+ex);
        }

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this,Calbmi.class);
                startActivity(intent);
            }
        });

        exit = (Button)findViewById(R.id.button6);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                Toast.makeText(settings.this, "App Exit", Toast.LENGTH_SHORT).show();
            }
        });

        back = (Button)findViewById(R.id.button7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(settings.this,MainActivity.class);
                //startActivity(intent);
                a=1;
                finish();
            }
        });

        theme = (Button)findViewById(R.id.button17);
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, Themechange.class);
                startActivity(intent);
            }
        });

        qrc = (Button)findViewById(R.id.button20);
        qrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this,qrcodemorning.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(a==1){
            finish();
        }
        if(a==2){
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        a=2;
        onPause();
    }
}
