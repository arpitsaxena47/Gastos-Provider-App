package com.gastos.gastosprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Enterpin_Activity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth Auth;
    private String originalPin;
    ImageView btnOk;
    EditText dig1,dig2,dig3,dig4;
    private TextView forgotPin;
    private DatabaseReference ref;
    int a;
    EditText edt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterpin_);



        dig1 = findViewById(R.id.digit1);
        dig2 = findViewById(R.id.digit2);
        dig3 = findViewById(R.id.digit3);
        dig4 = findViewById(R.id.digit4);
        forgotPin=findViewById(R.id.forgotPin);

        edt = dig1;

        dig1.setShowSoftInputOnFocus(false);
        dig2.setShowSoftInputOnFocus(false);
        dig3.setShowSoftInputOnFocus(false);
        dig4.setShowSoftInputOnFocus(false);

        btnOk = findViewById(R.id.go);
        Auth = FirebaseAuth.getInstance();
        setpinEditText();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredPin = dig1.getText().toString() + dig2.getText().toString() + dig3.getText().toString() +
                        dig4.getText().toString() ;
                if(enteredPin.length() == 4)
                {
                    String userId = Auth.getCurrentUser().getUid();
                    ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("MobilePin");

                    ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task2) {
                            if (!task2.isSuccessful()) {

                            }
                            else {
                                if(task2.getResult().getValue() != null) {

                                    String prevPin = task2.getResult().child("Pin").getValue() != null?task2.getResult().child("Pin").getValue() + "":"";

                                    if(enteredPin.equals(prevPin)){
                                        Intent intent = new Intent(Enterpin_Activity.this,HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(Enterpin_Activity.this, "Entered Pin is Incorrect " , Toast.LENGTH_SHORT).show();
                                    }


                                }
                                else {
                                    // Toast.makeText(context, "No Data Found..." , Toast.LENGTH_SHORT).show();
                                    // progressDialog.dismiss();
                                }
                            }

                        }
                    });
                    if(enteredPin.equals(getOriginalPin()) )
                    {
                        //Intent
                        Intent intent = new Intent(Enterpin_Activity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
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

        findViewById(R.id.t9_key_0).setOnClickListener(this);
        findViewById(R.id.t9_key_1).setOnClickListener(this);
        findViewById(R.id.t9_key_2).setOnClickListener(this);
        findViewById(R.id.t9_key_3).setOnClickListener(this);
        findViewById(R.id.t9_key_4).setOnClickListener(this);
        findViewById(R.id.t9_key_5).setOnClickListener(this);
        findViewById(R.id.t9_key_6).setOnClickListener(this);
        findViewById(R.id.t9_key_7).setOnClickListener(this);
        findViewById(R.id.t9_key_8).setOnClickListener(this);
        findViewById(R.id.t9_key_9).setOnClickListener(this);
        findViewById(R.id.t9_key_backspace).setOnClickListener(this);

//        dig2.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    dig1.requestFocus();
//                    edt  = dig1;
//                    dig1.setText("");
//
//                }
//                return false;
//            }
//        });
//
//        dig3.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    dig2.requestFocus();
//                    edt = dig2;
//                    dig2.setText("");
//                }
//                return false;
//            }
//        });
//        dig4.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    if(dig4.getText().toString().equals(""))
//                    {
//                        dig3.requestFocus();
//                        edt = dig3;
//                        dig3.setText("");
//                    }
//                    else{
//                        dig4.setText("");
//                    }
//
//
//                }
//                return false;
//            }
//        });

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
                    edt = dig2;
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
                    edt = dig3;
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
                    edt = dig4;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.t9_key_0:
                edt.setText("0");
                break;
            case R.id.t9_key_1:
                edt.setText("1");
                break;
            case R.id.t9_key_2:
                edt.setText("2");
                break;
            case R.id.t9_key_3:
                edt.setText("3");
                break;
            case R.id.t9_key_4:
                edt.setText("4");
                break;
            case R.id.t9_key_5:
                edt.setText("5");
                break;
            case R.id.t9_key_6:
                edt.setText("6");
                break;
            case R.id.t9_key_7:
                edt.setText("7");
                break;
            case R.id.t9_key_8:
                edt.setText("8");
                break;
            case R.id.t9_key_9:
                edt.setText("9");
                break;
            case R.id.t9_key_backspace:
                if(edt == dig2)
                {
                    dig1.requestFocus();
                    edt = dig1;
                    edt.setText("");
                }
                else if(edt == dig3)
                {
                    dig2.requestFocus();
                    edt = dig2;
                    edt.setText("");
                }
                else if(edt == dig4)
                {
                    if(edt.getText().toString().equals(""))
                    {
                        dig3.requestFocus();
                        edt = dig3;
                        edt.setText("");
                    }
                    else{
                        edt.setText("");
                    }

                }

                break;
        }
    }
}