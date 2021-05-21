package com.gastos.gastosprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.numpad.NumPad;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {

   // private EditText phoneEdt;
    //private ImageView getOtpBtn;
   // private FirebaseAuth mAuth;
    private ImageView otp_button;
    private EditText phone_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        otp_button=findViewById(R.id.idBtnGetOTP);
        phone_num=findViewById(R.id.idEdtMobile);


        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=phone_num.getText().toString().trim();
                if(!num.isEmpty()) {
                    if (num.length() == 10) {
                        Intent intent = new Intent(PhoneNumberActivity.this, VerifyOTPActivity.class);
                        intent.putExtra("phone_number", num);
                        intent.putExtra("via", 1);

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("phoneNum", num);
                        editor.apply();


                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(PhoneNumberActivity.this, "Please enter correct phone number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(PhoneNumberActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

      /*  phoneEdt = findViewById(R.id.idEdtMobile);
        mAuth = FirebaseAuth.getInstance();
        getOtpBtn = findViewById(R.id.idBtnGetOTP);
        String phoneNumber = phoneEdt.getText().toString();
        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int l = phoneEdt.getText().length();
                if (TextUtils.isEmpty(phoneEdt.getText().toString()) && l<10) {
                    Toast.makeText(PhoneNumberActivity.this, "Please enter a valid phone number..", Toast.LENGTH_SHORT).show();
                    return;
                }else if(phoneEdt.getText().toString().length()!=10){
                    Toast.makeText(PhoneNumberActivity.this, "Please enter a valid 10 digit phone number..", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendOTP(phoneEdt.getText().toString().trim());
            }
        });

       */
    }

  /*  private void sendOTP(String phoneNumber) {
        String phone = "+" + "91" + phoneNumber;
        Toast.makeText(this, "OTP has been sent to your number..", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(PhoneNumberActivity.this, VerifyOTPActivity.class);
        i.putExtra("phone", phone);
        i.putExtra("via", "false");
        startActivity(i);

    }

   */
}