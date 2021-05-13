package com.gastos.gastosprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SetNewPin_Activity extends AppCompatActivity {

    private String epin1,epin2;
    private EditText pin1,pin2;
    private ImageView btnDone;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_pin_);

        pin1 = findViewById(R.id.newpin1);
        pin2 = findViewById(R.id.newpin2);
        btnDone = findViewById(R.id.goo);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epin1 = pin1.getText().toString() ;
                epin2 = pin2.getText().toString() ;
                if(epin1.length() == 4  ) {

                    if(epin1==epin2)
                    {
                        setPin(epin1);
                        Toast.makeText(SetNewPin_Activity.this, "set pin done=" + epin1, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SetNewPin_Activity.this, HomeActivity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(SetNewPin_Activity.this, "Both pin should be same" , Toast.LENGTH_SHORT).show();
                    }

                }
              else{
                    Toast.makeText(SetNewPin_Activity.this, "PIN Should Be Of 4 Digits", Toast.LENGTH_SHORT).show();
                }

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