package com.gastos.gastosprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Enterpin_Activity extends AppCompatActivity {


    private String originalPin;
    ImageView btnOk;
    EditText dig1,dig2,dig3,dig4;
    private TextView forgotPin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterpin_);

        dig1 = findViewById(R.id.digit1);
        dig2 = findViewById(R.id.digit2);
        dig3 = findViewById(R.id.digit3);
        dig4 = findViewById(R.id.digit4);
        forgotPin=findViewById(R.id.forgotPin);

        btnOk = findViewById(R.id.go);

        setpinEditText();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredPin = dig1.getText().toString() + dig2.getText().toString() + dig3.getText().toString() +
                        dig4.getText().toString() ;
                if(enteredPin.length() == 4)
                {
                    if(enteredPin.equals(getOriginalPin()) )
                    {
                        //Intent
                        Intent intent = new Intent(Enterpin_Activity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Enterpin_Activity.this, "Entered Pin is Incorrect " , Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Enterpin_Activity.this, "Pin Must be of 4 digit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Enterpin_Activity.this,phoneNumber2_Activity.class);
                startActivity(intent);
            }
        });

        dig2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    dig1.requestFocus();
                    dig1.setText("");

                }
                return false;
            }
        });

        dig3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    dig2.requestFocus();
                    dig2.setText("");
                }
                return false;
            }
        });
        dig4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if(dig4.getText().toString().equals(""))
                    {
                        dig3.requestFocus();
                        dig3.setText("");
                    }
                    else{
                        dig4.setText("");
                    }


                }
                return false;
            }
        });
    }

    private String getOriginalPin()
    {
        SharedPreferences sharedPref =  getSharedPreferences("preference", Context.MODE_PRIVATE);
        originalPin = sharedPref.getString("myPin","");
        return originalPin;
    }

    private void setpinEditText()
    {
        dig1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    dig2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dig2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                {
                    dig3.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dig3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    dig4.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

}