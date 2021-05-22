package com.gastos.gastosprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SetNewPin_Activity extends AppCompatActivity {
    private FirebaseAuth mauth;
    private String epin1,epin2;
    private EditText pin1,pin2;
    private ImageView btnDone;
    private SharedPreferences sharedPreferences;
    private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_pin_);

        pin1 = findViewById(R.id.newpin1);
        pin2 = findViewById(R.id.newpin2);
        btnDone = findViewById(R.id.goo);
        mauth = FirebaseAuth.getInstance();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epin1 = pin1.getText().toString().trim() ;
                epin2 = pin2.getText().toString().trim() ;
                if(epin1.length() == 4 && epin1.equals(epin2)==true ) {

                    setPin(epin1);
                    Toast.makeText(SetNewPin_Activity.this, "set pin done=" + epin1, Toast.LENGTH_SHORT).show();
                    //Pin save in firebase.
                    Map<String, Object> user = new HashMap<>();
                    user.put("Pin",epin1);
                    String userId = mauth.getCurrentUser().getUid();
                    ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("MobilePin");

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



                    Intent i = new Intent(SetNewPin_Activity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(epin1.length() != 4){
                    Toast.makeText(SetNewPin_Activity.this, "PIN Should Be Of 4 Digits", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SetNewPin_Activity.this, "Both pin should be same" , Toast.LENGTH_SHORT).show();
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