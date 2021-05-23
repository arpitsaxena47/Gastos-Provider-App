package com.gastos.gastosprovider;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity implements View.OnClickListener{

  /*  private EditText otpEdt1, otpEdt2, otpEdt3, otpEdt4, otpEdt5, otpEdt6;
    private ImageView verifyOtpBtn;
    String phoneNumber;
    private TextView resendOTPTV,pho_number;
    private String verificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken resendOTPtoken;
    String r;

   */
  private DatabaseReference ref;
    TextView phone_num;
    EditText inputotp1, inputotp2, inputotp3, inputotp4, inputotp5,inputotp6;
    public String phonenumber_value;
    private FirebaseAuth mAuth;
     ImageView btnVerify;
    private TextView resendOTPTV;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendOTPtoken;
    private int r;
    private EditText edt ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        //getting phone number
        phone_num=findViewById(R.id.pho_number);
        phonenumber_value = getIntent().getStringExtra("phone_number");
        phone_num.setText(phonenumber_value);
        mAuth = FirebaseAuth.getInstance();
        phonenumber_value="+91"+phonenumber_value;

        btnVerify = findViewById(R.id.go);
        //getting OTP
        inputotp1=findViewById(R.id.inputcode1);
        inputotp2=findViewById(R.id.inputcode2);
        inputotp3=findViewById(R.id.inputcode3);
        inputotp4=findViewById(R.id.inputcode4);
        inputotp5=findViewById(R.id.inputcode5);
        inputotp6=findViewById(R.id.inputcode6);
        resendOTPTV=findViewById(R.id.idTVResendOTP);


        edt = inputotp1;

        inputotp1.setShowSoftInputOnFocus(false);
        inputotp2.setShowSoftInputOnFocus(false);
        inputotp3.setShowSoftInputOnFocus(false);
        inputotp4.setShowSoftInputOnFocus(false);
        inputotp5.setShowSoftInputOnFocus(false);
        inputotp6.setShowSoftInputOnFocus(false);

        r=getIntent().getIntExtra("via",-1);
        setupOTPINPUTS();
        sendVerificationCode(phonenumber_value);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = inputotp1.getText().toString() + inputotp2.getText().toString() + inputotp3.getText().toString() + inputotp4.getText().toString() + inputotp5.getText().toString() + inputotp6.getText().toString();

                if(otp .length() == 6)
                    verifyCode(otp);
                else
                    Toast.makeText(VerifyOTPActivity.this, "Enter Correct OTP", Toast.LENGTH_SHORT).show();
            }
        });
        resendOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyOTPActivity.this, "Resending  OTP...", Toast.LENGTH_SHORT).show();
                resendVerificationCode(phonenumber_value, resendOTPtoken);
            }
        });


//        inputotp2.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    inputotp1.requestFocus();
//                    inputotp1.setText("");
//
//                }
//                return false;
//            }
//        });
//
//        inputotp3.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    inputotp2.requestFocus();
//                    inputotp2.setText("");
//                }
//                return false;
//            }
//        });
//        inputotp4.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    inputotp3.requestFocus();
//                    inputotp3.setText("");
//
//                }
//                return false;
//            }
//        });
//        inputotp5.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    inputotp4.requestFocus();
//                    inputotp4.setText("");
//
//                }
//                return false;
//            }
//        });
//        inputotp6.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//                if(keyCode == KeyEvent.KEYCODE_DEL&&event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
//                    //this is for backspace
//                    if(inputotp6.getText().toString().equals(""))
//                    {
//                        inputotp5.requestFocus();
//                        inputotp5.setText("");
//                    }
//                    else{
//                        inputotp6.setText("");
//                    }
//
//
//                }
//                return false;
//            }
//        });
       /* otpEdt1 = findViewById(R.id.inputcode1);
        otpEdt2 = findViewById(R.id.inputcode2);
        otpEdt3 = findViewById(R.id.inputcode3);
        otpEdt4 = findViewById(R.id.inputcode4);
        otpEdt5 = findViewById(R.id.inputcode5);
        otpEdt6 = findViewById(R.id.inputcode6);
        pho_number=findViewById(R.id.pho_number);
        resendOTPTV = findViewById(R.id.idTVResendOTP);
        verifyOtpBtn = findViewById(R.id.go);
        mAuth = FirebaseAuth.getInstance();
        phoneNumber = getIntent().getStringExtra("phone");
         r=getIntent().getStringExtra("via");
         pho_number.setText(phoneNumber.substring(3));
        setupOTPINPUTS();
        sendVerificationCode(phoneNumber);
        verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (TextUtils.isEmpty(otpEdt.getText().toString())) {
//                    Toast.makeText(VerifyOTPActivity.this, "Please enter your OTP", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                String otp = otpEdt1.getText().toString() + otpEdt2.getText().toString() + otpEdt3.getText().toString() + otpEdt4.getText().toString() + otpEdt5.getText().toString() + otpEdt6.getText().toString();

                verifyCode(otp);
            }
        });

        resendOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phoneNumber, resendOTPtoken);
            }
        });

        */
        findViewById(R.id.t9_key_0).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_1).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_2).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_3).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_4).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_5).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_6).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_7).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_8).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_9).setOnClickListener(VerifyOTPActivity.this);
        findViewById(R.id.t9_key_backspace).setOnClickListener(VerifyOTPActivity.this);

    }

    private void setupOTPINPUTS()
    {
        inputotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    inputotp2.requestFocus();
                    edt = inputotp2;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    inputotp3.requestFocus();
                    edt = inputotp3;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    inputotp4.requestFocus();
                    edt = inputotp4;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    inputotp5.requestFocus();
                    edt = inputotp5;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputotp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    inputotp6.requestFocus();
                    edt = inputotp6;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputotp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    String searchString = s.toString();
                    int textLength = searchString.length();
                    inputotp6.setSelection(textLength);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            String userId = mAuth.getCurrentUser().getUid();
//                            ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("MobilePin");
//
//                            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DataSnapshot> task2) {
//                                    if (!task2.isSuccessful()) {
////                                    Log.e("firebase", "Error getting data", task.getException());
////                                    Toast.makeText(context, "Some Error ..." , Toast.LENGTH_SHORT).show();
////                                        Toast.makeText(VerifyOTPActivity.this, "Pin has been not stored..", Toast.LENGTH_SHORT).show();
////                                        Intent i = new Intent(VerifyOTPActivity.this, Setpin_Activity.class);
////                                        startActivity(i);
////                                        finish();
//                                    }
//                                    else
//                                        {
//                                            Intent i = new Intent(VerifyOTPActivity.this, Setpin_Activity.class);
//                                            startActivity(i);
//                                            finish();
//
////                                        if(task2.getResult().getValue() != null){
////                                            Toast.makeText(VerifyOTPActivity.this, "Pin has been stored already..", Toast.LENGTH_SHORT).show();
////                                            Intent i = new Intent(VerifyOTPActivity.this, Enterpin_Activity.class);
////                                            startActivity(i);
////                                            finish();
////                                        }
////                                        else{
////                                            Toast.makeText(VerifyOTPActivity.this, "Pin has been not stored..", Toast.LENGTH_SHORT).show();
////                                            Intent i = new Intent(VerifyOTPActivity.this, Setpin_Activity.class);
////                                            startActivity(i);
////                                            finish();
////                                        }
//
//                                    }
//
//                                }
//                            });

                             if(r==2)
                            {
                                Intent i = new Intent(VerifyOTPActivity.this, SetNewPin_Activity.class);
                                startActivity(i);
                                finish();
                                return;
                            }
                           else if(r==1)
                            {
                                Intent i = new Intent(VerifyOTPActivity.this, Setpin_Activity.class);
                                startActivity(i);
                                finish();
                                return;
                            }

                             else {
                                 FirebaseUser user = mAuth.getCurrentUser();
                                 if(user!=null){
                                     Intent i = new Intent(VerifyOTPActivity.this, Enterpin_Activity.class);
                                     startActivity(i);
                                     finish();
                                 }
                                 else
                                 {
                                     Intent i = new Intent(VerifyOTPActivity.this, Setpin_Activity.class);
                                     startActivity(i);
                                     finish();
                                 }
                             }


                            //Toast.makeText(VerifyOTPActivity.this, "User verified..", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerifyOTPActivity.this, "Fail to verify the user..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                VerifyOTPActivity.this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendOTPtoken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                //otpEdt.setVisibility(View.INVISIBLE);
                inputotp1.setText(code.substring(0, 1));
                inputotp2.setText(code.substring(1, 2));
                inputotp3.setText(code.substring(2, 3));
                inputotp4.setText(code.substring(3, 4));
                inputotp5.setText(code.substring(4, 5));
                inputotp6.setText(code.substring(5, 6));
//                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("TAG", "ERROR IS " + e.getMessage());
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

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
                if(edt == inputotp2)
                {
                    inputotp1.requestFocus();
                    edt = inputotp1;
                    edt.setText("");
                }
                else if(edt == inputotp3)
                {
                    inputotp2.requestFocus();
                    edt = inputotp2;
                    edt.setText("");
                }
                else if(edt == inputotp4)
                {
                    inputotp3.requestFocus();
                    edt = inputotp3;
                    edt.setText("");
                }
                else if(edt == inputotp5)
                {
                    inputotp4.requestFocus();
                    edt = inputotp4;
                    edt.setText("");
                }
                else if(edt == inputotp6)
                {
                    if(edt.getText().toString().equals(""))
                    {
                        inputotp5.requestFocus();
                        edt = inputotp5;
                        edt.setText("");
                    }
                    else{
                        edt.setText("");
                    }

                }

                break;
        }
    }

  /*  private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallBack,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(r=="false")
                            {
                                Intent i = new Intent(VerifyOTPActivity.this, Setpin_Activity.class);
                                startActivity(i);
                            }
                            else
                            {
                                Intent i = new Intent(VerifyOTPActivity.this, SetNewPin_Activity.class);
                                startActivity(i);
                            }

                            //Toast.makeText(VerifyOTPActivity.this, "User verified..", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VerifyOTPActivity.this, "Fail to verify the user..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                VerifyOTPActivity.this,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendOTPtoken = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                //otpEdt.setVisibility(View.INVISIBLE);
                otpEdt1.setText(code.substring(0, 1));
                otpEdt2.setText(code.substring(1, 2));
                otpEdt3.setText(code.substring(2, 3));
                otpEdt4.setText(code.substring(3, 4));
                otpEdt5.setText(code.substring(4, 5));
                otpEdt6.setText(code.substring(5, 6));
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("TAG", "ERROR IS " + e.getMessage());
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void setupOTPINPUTS()
    {
        otpEdt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    otpEdt2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    otpEdt3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpEdt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    otpEdt4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpEdt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    otpEdt5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpEdt5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty())
                {
                    otpEdt6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

   */
}