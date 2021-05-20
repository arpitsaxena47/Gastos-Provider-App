package com.gastos.gastosprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Setpin_Activity extends AppCompatActivity {

    private String epin1,epin2;
    private EditText pin1,pin2;
    private ImageView btnDone;
    private SharedPreferences sharedPreferences;
    private TextView resetPin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpin_);

        pin1 = findViewById(R.id.pin1);
        pin2 = findViewById(R.id.pin2);
        btnDone = findViewById(R.id.setpinOk);
       resetPin=findViewById(R.id.resetPin);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epin1 = pin1.getText().toString().trim() ;
                epin2 = pin2.getText().toString().trim() ;
                if(epin1.length() == 4 && epin1.equals(epin2)==true ) {

                    setPin(epin1);
                    Toast.makeText(Setpin_Activity.this, "set pin done=" + epin1, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Setpin_Activity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(epin1.length() != 4){
                    Toast.makeText(Setpin_Activity.this, "PIN Should Be Of 4 Digits", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Setpin_Activity.this, "Both pin should be same" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin1.setText("");
                pin2.setText("");
            }
        });
    }

    private void setPin(String pin)
    {
        sharedPreferences =  getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myPin", pin);
        editor.apply();
    }

}