package com.gastos.gastosprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Setpin_Activity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mauth;
    private String epin1,epin2;
    private EditText pin1,pin2;
    private ImageView btnDone;
    private SharedPreferences sharedPreferences;
    private TextView resetPin;
    private DatabaseReference ref;
    private EditText edt1,edt2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpin_);

        pin1 = findViewById(R.id.pin1);
        pin2 = findViewById(R.id.pin2);
        btnDone = findViewById(R.id.setpinOk);
       resetPin=findViewById(R.id.resetPin);
        mauth = FirebaseAuth.getInstance();

        edt1 = pin1;
        edt2 = pin2;
        pin1.setShowSoftInputOnFocus(false);
        pin2.setShowSoftInputOnFocus(false);

        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
         String PhoneNumber=pref.getString("phoneNum","");

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epin1 = pin1.getText().toString().trim() ;
                epin2 = pin2.getText().toString().trim() ;
                if(epin1.length() == 4 && epin1.equals(epin2)==true ) {

                    setPin(epin1);
                    Toast.makeText(Setpin_Activity.this, "set pin done=" + epin1, Toast.LENGTH_SHORT).show();
                    //Pin save in firebase.
                    Map<String, Object> user = new HashMap<>();
                    user.put("Pin",epin1);
                    user.put("Phone_Number",PhoneNumber);
                    String userId = mauth.getCurrentUser().getUid();
                    ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Details");

                    ref.setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });



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

        ((EditText)findViewById(R.id.pin1)).addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = s.toString();
                int textLength = searchString.length();
                pin1.setSelection(textLength);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        ((EditText)findViewById(R.id.pin2)).addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = s.toString();
                int textLength = searchString.length();
                pin2.setSelection(textLength);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.t9_key_0).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_1).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_2).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_3).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_4).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_5).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_6).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_7).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_8).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_9).setOnClickListener(Setpin_Activity.this);
        findViewById(R.id.t9_key_backspace).setOnClickListener(Setpin_Activity.this);

    }

    private void setPin(String pin)
    {
        sharedPreferences =  getSharedPreferences("preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myPin", pin);
        editor.apply();
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.t9_key_0:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "0");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "0");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_1:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "1");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "1");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_2:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "2");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "2");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_3:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "3");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "3");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_4:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "4");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "4");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_5:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "5");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "5");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_6:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "6");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "6");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_7:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "7");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "7");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_8:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "8");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "8");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_9:
                if(edt1.hasFocus()) {
                    edt1.setText(edt1.getText() + "9");
                    edt1.requestFocus(1);
                }
                if (edt2.hasFocus()) {
                    edt2.setText(edt2.getText() + "9");
                    edt2.requestFocus(1);
                }
                break;
            case R.id.t9_key_backspace:
                if (edt1.hasFocus()) {
                    if (edt1.getText().toString().length() != 0) {

                        edt1.setText(edt1.getText().toString().substring(0, edt1.getText().toString().length() - 1));
                    }
                    edt1.requestFocus(-1);
                }

                if (edt2.hasFocus()) {
                    if (edt2.getText().toString().length() != 0) {

                        edt2.setText(edt2.getText().toString().substring(0, edt2.getText().toString().length() - 1));
                    }
                    edt2.requestFocus(-1);
                }
                break;
        }
    }

}