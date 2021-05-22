package com.gastos.gastosprovider;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;


public class PhoneNumberActivity extends AppCompatActivity  implements View.OnClickListener{

   // private EditText phoneEdt;
    //private ImageView getOtpBtn;
   // private FirebaseAuth mAuth;
    private ImageView otp_button;
    private EditText phone_num;
    private EditText edt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        otp_button=findViewById(R.id.idBtnGetOTP);
        phone_num=findViewById(R.id.idEdtMobile);

        edt = phone_num;
        phone_num.setShowSoftInputOnFocus(false);

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
        ((EditText)findViewById(R.id.idEdtMobile)).addTextChangedListener(new TextWatcher() {

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

        findViewById(R.id.t9_key_0).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_1).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_2).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_3).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_4).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_5).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_6).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_7).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_8).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_9).setOnClickListener(PhoneNumberActivity.this);
        findViewById(R.id.t9_key_backspace).setOnClickListener(PhoneNumberActivity.this);


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