package com.navjot.faceproject.alarmforhealth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

public class Themechange extends AppCompatActivity {
    RelativeLayout backg,leaf1,desert1,abs1,yello,bud,rel;
    SharedPreferences backpref;
    SharedPreferences.Editor editor;
    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themechange);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.7),(int)(height*0.7));

        rel = (RelativeLayout) findViewById(R.id.activity_themechange);
        backg = (RelativeLayout)findViewById(R.id.relativeLayoutbg);

        leaf1 = (RelativeLayout)findViewById(R.id.relativeLayoutleaf);
        desert1 = (RelativeLayout)findViewById(R.id.relativeLayoutdesert);

        abs1 = (RelativeLayout)findViewById(R.id.relativeLayoutabst);
        yello = (RelativeLayout)findViewById(R.id.relativeLayoutyel);
        bud = (RelativeLayout)findViewById(R.id.relativeLayoutbudah);


        backpref = getSharedPreferences("prefback", Context.MODE_PRIVATE);
        editor = backpref.edit();

        backg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel.setBackgroundResource(R.drawable.deadleaf);

                editor.putString("themm","deadLeaf");
                editor.apply();
                Intent intent = new Intent(Themechange.this, settings.class);
                startActivity(intent);
                a=1;
            }
        });

        leaf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel.setBackgroundResource(R.drawable.antique);
                editor.putString("themm","antique");
                editor.apply();
                Intent intent = new Intent(Themechange.this, settings.class);
                startActivity(intent);
                a=1;
            }
        });

        desert1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel.setBackgroundResource(R.drawable.backgroundyelwrite);
                editor.putString("themm","backgroundyelwrite");
                editor.apply();
                Intent intent = new Intent(Themechange.this, settings.class);
                startActivity(intent);
                a=1;
            }
        });

        abs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel.setBackgroundResource(R.drawable.abstract1);
                editor.putString("themm","abstract1");
                editor.apply();
                Intent intent = new Intent(Themechange.this, settings.class);
                startActivity(intent);
                a=1;
            }
        });

        yello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel.setBackgroundResource(R.drawable.yellowpainting);
                editor.putString("themm","yellowpainting");
                editor.apply();
                Intent intent = new Intent(Themechange.this, settings.class);
                startActivity(intent);
                a=1;
            }
        });

        bud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel.setBackgroundResource(R.drawable.backgroundrose);
                editor.putString("themm","backgroundrose");
                editor.apply();
                Intent intent = new Intent(Themechange.this, settings.class);
                startActivity(intent);
                a=1;
            }
        });


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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(a==1){
            finish();
        }
    }
}
