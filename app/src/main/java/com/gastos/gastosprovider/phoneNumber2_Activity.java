package com.gastos.gastosprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gastos.gastosprovider.Setting.AccountInformation.AccountData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class phoneNumber2_Activity extends AppCompatActivity implements View.OnClickListener{

//    private EditText phoneEdt;
//    private ImageView getOtpBtn;
    private FirebaseAuth mAuth;
    private  FirebaseDatabase database;
    private ImageView otp_button;
    private EditText phone_num;
    private EditText edt ;
    private  String oldnum="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number2_);


        otp_button=findViewById(R.id.idBtnGetOTP2);
        phone_num=findViewById(R.id.idEdtMobile2);

        edt = phone_num;
        phone_num.setShowSoftInputOnFocus(false);
        mAuth = FirebaseAuth.getInstance();
        database=  FirebaseDatabase.getInstance();

        getNumberFromFirebase();
        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=phone_num.getText().toString().trim();
                if(!num.isEmpty()) {
                    if (num.length() == 10) {
                        if(num.equals(oldnum)){
                            Intent intent = new Intent(phoneNumber2_Activity.this, VerifyOTPActivity.class);
                            intent.putExtra("phone_number", num);
                            intent.putExtra("via", 2);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(phoneNumber2_Activity.this, "This App is Registered With this "+oldnum+"  Phone Number", Toast.LENGTH_SHORT).show();
                        }




                    } else {
                        Toast.makeText(phoneNumber2_Activity.this, "Please enter correct phone number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(phoneNumber2_Activity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ((EditText)findViewById(R.id.idEdtMobile2)).addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String searchString = s.toString();
                int textLength = searchString.length();
                phone_num.setSelection(textLength);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.t9_key_0).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_1).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_2).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_3).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_4).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_5).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_6).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_7).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_8).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_9).setOnClickListener(phoneNumber2_Activity.this);
        findViewById(R.id.t9_key_backspace).setOnClickListener(phoneNumber2_Activity.this);


    }

    private void getNumberFromFirebase() {

        DatabaseReference ref = database.getReference("Merchant_data/"+mAuth.getUid()).child("Account_Information");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {

                AccountData info = dataSnapshot.getValue(AccountData.class);
                oldnum=info.getPhoneNumber();
            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {
                Toast.makeText(phoneNumber2_Activity.this, "Internet Connection Error...", Toast.LENGTH_SHORT).show();
            }
        });

    }


@Override
public void onClick(View view) {
    switch (view.getId())
    {
        case R.id.t9_key_0:
            edt.setText(edt.getText()+"0");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_1:
            edt.setText(edt.getText()+"1");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_2:
            edt.setText(edt.getText()+"2");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_3:
            edt.setText(edt.getText()+"3");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_4:
            edt.setText(edt.getText()+"4");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_5:
            edt.setText(edt.getText()+"5");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_6:
            edt.setText(edt.getText()+"6");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_7:
            edt.setText(edt.getText()+"7");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_8:
            edt.setText(edt.getText()+"8");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_9:
            edt.setText(edt.getText()+"9");
            edt.requestFocus(1);
            break;
        case R.id.t9_key_backspace:
            if(edt.getText().toString().length()!=0)
            {

                edt.setText(edt.getText().toString().substring(0,edt.getText().toString().length()-1));
            }
            edt.requestFocus(-1);
            break;
    }
}



}