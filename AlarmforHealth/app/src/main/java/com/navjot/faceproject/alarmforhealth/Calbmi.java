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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Calbmi extends AppCompatActivity {

    EditText pound,inch;
    Button cal;
    String poundstr, inchstr;
    double a;
    Double pounddou,inchdou;
    LinearLayout rel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calbmi);
        pound= (EditText) findViewById(R.id.editText);
        inch = (EditText) findViewById(R.id.editText2);
        Log.d(""+poundstr,""+inchstr);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*0.7),(int)(height*0.7));

        rel = (LinearLayout)findViewById(R.id.activity_calbmi);

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

        cal = (Button)findViewById(R.id.button8);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poundstr = pound.getText().toString();
                inchstr = inch.getText().toString();

                if((pound.getText().toString().equals(""))||(inch.getText().toString().equals(""))){
                    Toast.makeText(Calbmi.this, "Please Fill the required fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    pounddou = (Double.parseDouble(poundstr));
                    inchdou = (Double.parseDouble(inchstr));
                    try {
                        Log.d("Double "+pounddou," "+inchdou);
                        a = (pounddou / (inchdou * inchdou)) * 703;
                        Intent intent = new Intent(Calbmi.this,settings.class);
                        intent.putExtra("bmi",""+a);
                        startActivity(intent);
                    }
                    catch(NumberFormatException ex){
                        Log.d("Exception Log"+poundstr,""+inchstr);
                        Log.d("Numberformatexception","ex"+ex);
                    }
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}
