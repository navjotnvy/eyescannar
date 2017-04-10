package com.navjot.faceproject.alarmforhealth;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Disclaimer extends AppCompatActivity {

    Button exit,back;
    RelativeLayout rel;
    TextView txtvw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);

        rel = (RelativeLayout) findViewById(R.id.activity_disclaimer);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.7),(int)(height*0.7));

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

        txtvw = (TextView) findViewById(R.id.textView14);
/*
        exit = (Button)findViewById(R.id.button6);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                //Toast.makeText(Disclaimer.this, "App Exit", Toast.LENGTH_SHORT).show();
            }
        });

        back = (Button)findViewById(R.id.button7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Disclaimer.this,MainActivity.class);
                startActivity(intent);
            }
        });
*/

    }
}
