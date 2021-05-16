package com.gastos.gastosprovider;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gastos.gastosprovider.Card.Post;
import com.gastos.gastosprovider.Setting.SettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private EditText ownerNameEdt, phoneNumEdt, emailEdt;
    private ImageButton editOwnerName, editPhoneNum, editEmail;
    private ImageView saveAccountInfoButton;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageRef;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Post_test Post;
    DatabaseReference ref;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        context = container.getContext();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        saveAccountInfoButton = view.findViewById(R.id.saveAccountChanges);
        ownerNameEdt = view.findViewById(R.id.idEdtOwnerName);
        phoneNumEdt = view.findViewById(R.id.idEdtPhoneNum);
        emailEdt = view.findViewById(R.id.idEdtEmailAddress);

        editOwnerName = view.findViewById(R.id.editOwnerName);
        editPhoneNum = view.findViewById(R.id.editPhoneNum);
        editEmail = view.findViewById(R.id.editEmailAddress);

        getData();

        ownerNameEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(getOwnerNameEdt())) {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                } else if (phoneNumEdt.getText().toString().equals(getPhoneNumEdt()) && emailEdt.getText().toString().equals(getEmailEdt())) {
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

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(getPhoneNumEdt()) && editable.toString().length() == 10) {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                } else if (ownerNameEdt.getText().toString().equals(getPhoneNumEdt()) && emailEdt.getText().toString().equals(getEmailEdt())) {
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

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(getEmailEdt())) {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                } else if (phoneNumEdt.getText().toString().equals(getPhoneNumEdt()) && ownerNameEdt.getText().toString().equals(getEmailEdt())) {
                    saveAccountInfoButton.setVisibility(View.GONE);
                }
            }
        });


        saveAccountInfoButton.setVisibility(View.GONE);
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
                } else if (emailEdt.getText().toString().matches(emailPattern)) {
                    Toast.makeText(context, "Please enter correct Email ID....", Toast.LENGTH_SHORT).show();
                    return;
                }
//                addDataToFirebase(ownerNameEdt.getText().toString(), phoneNumEdt.getText().toString(), emailEdt.getText().toString());
                save(ownerNameEdt.getText().toString(), phoneNumEdt.getText().toString(), emailEdt.getText().toString());
                saveAccountInfoButton.setVisibility(View.GONE);

                Fragment fragment = new SettingsFragment();
                getFragmentManager().beginTransaction().replace(R.id.idFLContainer, fragment).commit();
            }
        });

        editOwnerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ownerNameEdt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ownerNameEdt.setOnClickListener(this);
                    }
                });

            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdt.setOnClickListener(this);

            }
        });
        editPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumEdt.setOnClickListener(this);

            }
        });


        return view;
    }

//    private void addDataToFirebase(String userName, String phone, String email) {
//
//        ProgressDialog progressDialog
//                = new ProgressDialog(context);
//        progressDialog.setTitle("Uploading...");
//        progressDialog.show();
//
//
//        Map<String, Object> user = new HashMap<>();
//        user.put("OwnerName", userName);
//        user.put("PhoneNumber", phone);
//        user.put("EmailAddress", email);
//
//        String userId = mAuth.getCurrentUser().getUid();
//        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data");
//        ref.child("Merchant_data").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(context, "Data updated successfully..", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                } else {
//                    progressDialog.dismiss();
//                    Toast.makeText(context, "Fail to Add Data.", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//}
        private void save(String userName, String phone, String email) {
            Map<String, Object> user = new HashMap<>();
        user.put("OwnerName", userName);
        user.put("PhoneNumber", phone);
        user.put("EmailAddress", email);

            String userId = mAuth.getCurrentUser().getUid();
             ref = FirebaseDatabase.getInstance().getReference().child("Merchant_Data/" + mAuth.getUid());

                    ref.setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("jfbvkj", "onComplete: ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("jfbvkj", "onFailure: "+e.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("jfbvkj", "onSuccess: ");
                }
            });
        }


    public String getOwnerNameEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] ownerName = {""};
        DocumentReference documentReference = db.collection("Merchant_data").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    ownerName[0] = "";
                }
                if (value != null && value.exists()) {
                    ownerName[0] = value.getData().get("OwnerName").toString();


                }
            }
        });
        return ownerName[0];
    }

    public String getPhoneNumEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] phoneNum = {""};
        DocumentReference documentReference = db.collection("Merchant_data").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    phoneNum[0] = "";
                }
                if (value != null && value.exists()) {
                    phoneNum[0] = value.getData().get("PhoneNumber").toString();


                }
            }
        });
        return phoneNum[0];
    }

    public String getEmailEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] email = {""};
        DocumentReference documentReference = db.collection("Merchant_data").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    email[0] = "";
                }
                if (value != null && value.exists()) {
                    email[0] = value.getData().get("EmailAddress").toString();


                }
            }
        });
        return email[0];
    }

    private void getData() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Merchant_data").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    return;
                } else if (value != null && value.exists()) {
                    ownerNameEdt.setText(value.getData().get("OwnerName").toString());
                    phoneNumEdt.setText(value.getData().get("PhoneNumber").toString());
                    emailEdt.setText(value.getData().get("EmailAddress").toString());

                }
            }
        });

    }

}