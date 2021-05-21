package com.gastos.gastosprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.numpad.NumPad;
import com.google.firebase.auth.FirebaseAuth;

public class phoneNumber2_Activity extends AppCompatActivity {

//    private EditText phoneEdt;
//    private ImageView getOtpBtn;
//    private FirebaseAuth mAuth;

    private ImageView otp_button;
    private EditText phone_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number2_);


        otp_button=findViewById(R.id.idBtnGetOTP2);
        phone_num=findViewById(R.id.idEdtMobile2);

        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=phone_num.getText().toString().trim();
                if(!num.isEmpty()) {
                    if (num.length() == 10) {
                        Intent intent = new Intent(phoneNumber2_Activity.this, VerifyOTPActivity.class);
                        intent.putExtra("phone_number", num);
                        intent.putExtra("via", 2);
                        startActivity(intent);
                        finish();


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

//        phoneEdt = findViewById(R.id.idEdtMobile2);
//        mAuth = FirebaseAuth.getInstance();
//        getOtpBtn = findViewById(R.id.idBtnGetOTP2);
//        String phoneNumber = phoneEdt.getText().toString();
//        getOtpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int l = phoneEdt.getText().length();
//                if (TextUtils.isEmpty(phoneEdt.getText().toString()) && l<10) {
//                    Toast.makeText(phoneNumber2_Activity.this, "Please enter a valid phone number..", Toast.LENGTH_SHORT).show();
//                    return;
//                }else if(phoneEdt.getText().toString().length()!=10){
//                    Toast.makeText(phoneNumber2_Activity.this, "Please enter a valid 10 digit phone number..", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                sendOTP(phoneEdt.getText().toString());
//            }
//        });
    }

//    private void sendOTP(String phoneNumber) {
//        String phone = "+" + "91" + phoneNumber;
//        Toast.makeText(this, "OTP has been sent to your number..", Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(phoneNumber2_Activity.this, VerifyOTPActivity.class);
//        i.putExtra("phone", phone);
//        i.putExtra("via", "true");
//        startActivity(i);
//
//    }
}