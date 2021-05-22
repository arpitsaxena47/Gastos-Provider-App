package com.gastos.gastosprovider.Setting.AccountInformation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gastos.gastosprovider.HomeActivity;
import com.gastos.gastosprovider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AccountInformation extends AppCompatActivity {

    private EditText ownerNameEdt, phoneNumEdt, emailEdt;
//    private ImageButton editOwnerName, editPhoneNum, editEmail;
    private ImageView saveAccountInfoButton , btnAccountBack;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    String emailPattern = "[a-zA-Z0-9._-]+ @ + [a-z]+.+[a-z]+";
    private DatabaseReference ref;

    private String prevOwnerName  = "", prevPhoneNum = "" , prevEmail = "";
//    private int flag1 = 0 , flag2 = 0 ,  flag3 = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_account);
        context = AccountInformation.this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        saveAccountInfoButton = findViewById(R.id.saveAccountChanges);
        btnAccountBack =findViewById(R.id.account_info_back);

        ownerNameEdt = findViewById(R.id.idEdtOwnerName);
//        ownerNameEdt.setFocusable(false);

        phoneNumEdt = findViewById(R.id.idEdtPhoneNum);
//        phoneNumEdt.setClickable(false);

        emailEdt = findViewById(R.id.idEdtEmailAddress);
//        emailEdt.setClickable(false);

//        editOwnerName = findViewById(R.id.editOwnerName);
//        editPhoneNum = findViewById(R.id.editPhoneNum);
//        editEmail = findViewById(R.id.editEmailAddress);

        getData();

        ownerNameEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(!charSequence.toString().trim().equals(getOwnerNameEdt().trim()))
//                {
//                    saveAccountInfoButton.setVisibility(View.VISIBLE);
//                }
//                else if (phoneNumEdt.getText().toString().trim().equals(getPhoneNumEdt()) && emailEdt.getText().toString().trim().equals(getEmailEdt())) {
//                    saveAccountInfoButton.setVisibility(View.GONE);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(prevOwnerName)) {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                } else if (phoneNumEdt.getText().toString().equals(prevPhoneNum) && emailEdt.getText().toString().equals(prevEmail)) {
                    saveAccountInfoButton.setVisibility(View.GONE);
                }
            }
        });

        phoneNumEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (!charSequence.toString().trim().equals(getPhoneNumEdt().trim()) && charSequence.toString().length() == 10) {
//                        saveAccountInfoButton.setVisibility(View.VISIBLE);
//                    } else if (ownerNameEdt.getText().toString().trim().equals(getPhoneNumEdt().trim()) &&
//                            emailEdt.getText().toString().trim().equals(getEmailEdt().trim())) {
//                        saveAccountInfoButton.setVisibility(View.GONE);
//                    }
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(prevPhoneNum) && editable.toString().length() == 10) {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                } else if (ownerNameEdt.getText().toString().equals(prevOwnerName) && emailEdt.getText().toString().equals(prevEmail)) {
                    saveAccountInfoButton.setVisibility(View.GONE);
                }
            }
        });

        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!charSequence.toString().trim().equals(getEmailEdt().trim())) {
//                    saveAccountInfoButton.setVisibility(View.VISIBLE);
//                } else if (phoneNumEdt.getText().toString().trim().equals(getPhoneNumEdt().trim()) && ownerNameEdt.getText().toString().trim().equals(getEmailEdt().trim())) {
//                    saveAccountInfoButton.setVisibility(View.GONE);
//                }
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(prevEmail)) {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                } else if (phoneNumEdt.getText().toString().equals(prevPhoneNum) && ownerNameEdt.getText().toString().equals(prevOwnerName)) {
                    saveAccountInfoButton.setVisibility(View.GONE);
                }
            }
        });

        saveAccountInfoButton.setVisibility(View.GONE);

//        saveAccountInfoButton.setVisibility(View.GONE);
        saveAccountInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ownerNameEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter owner name..", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phoneNumEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter phone num..", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phoneNumEdt.getText().toString().length() != 10) {
                    Toast.makeText(context, "Phone number should be  10 Digits long", Toast.LENGTH_SHORT).show();
                    return;
                } else if (emailEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter Email ID....", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isEmailValid(emailEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter correct Email ID....", Toast.LENGTH_SHORT).show();
                    return;
                }
                addDataToFirebase(ownerNameEdt.getText().toString(), phoneNumEdt.getText().toString(), emailEdt.getText().toString().toLowerCase());
//                save(ownerNameEdt.getText().toString(), phoneNumEdt.getText().toString(), emailEdt.getText().toString());
                saveAccountInfoButton.setVisibility(View.GONE);

//                Fragment fragment = new SettingsFragment();
//                getFragmentManager().beginTransaction().replace(R.id.idFLContainer, fragment).commit();


                Intent homeIntent = new Intent(AccountInformation.this , HomeActivity.class);
                startActivity(homeIntent);
                finish();

//                Fragment fragment = new SettingsFragment();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.idFLContainer, fragment);
//                transaction.commit();
            }
        });

//        editOwnerName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(flag1 == 0 && flag2 == 0 && flag3 == 0){
//                    flag1 = 1;
//                    ownerNameEdt.setFocusable(true);
//                    ownerNameEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
//                    ownerNameEdt.setClickable(true); // user navigates with wheel and selects widget
//                    editOwnerName.setBackgroundColor(Color.GREEN);
//                }
//                else{
//                    flag1 = 0;
//                    ownerNameEdt.setFocusable(false);
//                    ownerNameEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
//                    ownerNameEdt.setClickable(false); // user navigates with wheel and selects widget
//                    editOwnerName.setBackgroundColor(Color.TRANSPARENT);
//                }
//            }
//        });
//        editEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(flag1 == 0 && flag2 == 0 && flag3 == 0){
//                    flag2 = 1;
//                    emailEdt.setFocusable(true);
//                    emailEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
//                    emailEdt.setClickable(true); // user navigates with wheel and selects widget
//                    editEmail.setBackgroundColor(Color.GREEN);
//                }
//                else{
//                    flag2 = 0;
//                    emailEdt.setFocusable(false);
//                    emailEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
//                    emailEdt.setClickable(false); // user navigates with wheel and selects widget
//                    editEmail.setBackgroundColor(Color.TRANSPARENT);
//                }            }
//        });
//        editPhoneNum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(flag1 == 0 && flag2 == 0 && flag3 == 0){
//                    flag3 = 1;
//                    phoneNumEdt.setFocusable(true);
//                    phoneNumEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
//                    phoneNumEdt.setClickable(true); // user navigates with wheel and selects widget
//                    editPhoneNum.setBackgroundColor(Color.GREEN);
//                }
//                else{
//                    flag3 = 0;
//                    phoneNumEdt.setFocusable(false);
//                    phoneNumEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
//                    phoneNumEdt.setClickable(false); // user navigates with wheel and selects widget
//                    editPhoneNum.setBackgroundColor(Color.TRANSPARENT);
//                }
//
//            }
//        });

        btnAccountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }



    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

        private void addDataToFirebase(String userName, String phone, String email) {

            ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
//            progressDialog.setProgress(100);
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                }}, 1000);

            Map<String, Object> user = new HashMap<>();
        user.put("OwnerName", userName);
        user.put("PhoneNumber", phone);
        user.put("EmailAddress", email);

            String userId = mAuth.getCurrentUser().getUid();
             ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Account_Information");

                    ref.setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            Log.i("jfbvkj", "onComplete: ");
                            if(task.isSuccessful()){
                                Toast.makeText(context, "Data updated successfully..", Toast.LENGTH_SHORT).show();
                                if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Log.i("jfbvkj", "onFailure: "+e.toString());
                            Toast.makeText(context, "Fail to Add Data.", Toast.LENGTH_SHORT).show();
                            if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                    Log.i("jfbvkj", "onSuccess: ");
                    Toast.makeText(context, "Data updated successfully..", Toast.LENGTH_SHORT).show();
                    if(progressDialog.isShowing())
                    progressDialog.dismiss();
                }
            });
        }


//    public String getOwnerNameEdt() {
//
//
//        String userId = mAuth.getCurrentUser().getUid();
//        final String[] ownerName = {""};
//
//
//        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("OwnerName");
//        ref.ad
//       ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//           @Override
//           public void onComplete(@NonNull Task<DataSnapshot> task) {
//               if (!task.isSuccessful()) {
////                   Log.e("firebase", "Error getting data", task.getException());
//
//               }
//               else {
//                   if(task.getResult().getValue()!= null)
//                   ownerName[0] = task.getResult().getValue() + "";
////                   Log.d("firebase", String.valueOf(task.getResult().getValue()));
//               }
//           }
//       });
//        return ownerName[0];
//    }

//    public String getPhoneNumEdt() {
//        String userId = mAuth.getCurrentUser().getUid();
//        final String[] phoneNum = {""};
//        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("PhoneNumber");
//        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
////                   Log.e("firebase", "Error getting data", task.getException());
//
//                }
//                else {
//                    if(task.getResult().getValue() != null)
//                    phoneNum[0] = task.getResult().getValue() + "";
////                   Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });
//        return phoneNum[0];
//    }

//    public String getEmailEdt() {
//        String userId = mAuth.getCurrentUser().getUid();
//        final String[] email = {""};
//        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("EmailAddress");
//        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
////                   Log.e("firebase", "Error getting data", task.getException());
//
//                }
//                else {
//                    if(task.getResult().getValue() != null)
//                    email[0] = task.getResult().getValue() + "";
////                   Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });
//        return email[0];
//    }

    private void getData() {

        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Fetching Data!! Please Wait...");
        progressDialog.show();

        String userId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Account_Information");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
//                   Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(context, "Check Your Internet Connection]=..." , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();



                }
                else {
                    if(task.getResult().getValue() != null) {



                        prevOwnerName = task.getResult().child("OwnerName").getValue() + "";
                        prevPhoneNum = task.getResult().child("PhoneNumber").getValue() + "";
                        prevEmail = task.getResult().child("EmailAddress").getValue() + "";

                        ownerNameEdt.setText(prevOwnerName);
                        phoneNumEdt.setText(prevPhoneNum);

                        emailEdt.setText(prevEmail);
//                        ownerNameEdt.setText(task.getResult().child("OwnerName").getValue() + "");
//                        phoneNumEdt.setText(task.getResult().child("PhoneNumber").getValue() + "");
////                  Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                        emailEdt.setText(task.getResult().child("EmailAddress").getValue() + "");

                        progressDialog.dismiss();
                    }
                    else {
                        Toast.makeText(context, "No Data Found..." , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if(prevPhoneNum.equals("")){

                            SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                            phoneNumEdt.setText(pref.getString("phoneNum",""));
                        }
                    }
                }

            }
        });


    }

}