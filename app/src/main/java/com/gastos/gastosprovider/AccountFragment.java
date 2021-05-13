package com.gastos.gastosprovider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private ImageButton editOwnerName , editPhoneNum , editEmail;
    private ImageView saveAccountInfoButton;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageRef;

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
                if(!editable.toString().equals(getOwnerNameEdt()))
                {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                }
                else
                if(phoneNumEdt.getText().toString().equals(getPhoneNumEdt()) && emailEdt.getText().toString().equals(getEmailEdt()))
                {
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
                if(!editable.toString().equals(getPhoneNumEdt()))
                {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                }
                else
                if(ownerNameEdt.getText().toString().equals(getPhoneNumEdt()) && emailEdt.getText().toString().equals(getEmailEdt()))
                {
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
                if(!editable.toString().equals(getEmailEdt()))
                {
                    saveAccountInfoButton.setVisibility(View.VISIBLE);
                }
                else
                if(phoneNumEdt.getText().toString().equals(getPhoneNumEdt()) && ownerNameEdt.getText().toString().equals(getEmailEdt()))
                {
                    saveAccountInfoButton.setVisibility(View.GONE);
                }
            }
        });


        saveAccountInfoButton.setActivated(false);
        saveAccountInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToFirebase(ownerNameEdt.getText().toString() , phoneNumEdt.getText().toString() , emailEdt.getText().toString());
            }
        });

        editOwnerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ownerNameEdt.callOnClick();

            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdt.callOnClick();

            }
        });
        editPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumEdt.callOnClick();

            }
        });


        return view;
    }

    private void addDataToFirebase(String userName, String phone, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("userName", userName);
        user.put("phone", phone);
        user.put("email", email);

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("Users").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Data updated successfully..", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Fail to Add Data.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public String getOwnerNameEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] ownerName = {""};
        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    ownerName[0] = "";
                }
                if (value != null && value.exists()) {
                    ownerName[0] =  value.getData().get("ownerName").toString();


                }
            }
        });
        return ownerName[0];
    }

    public String getPhoneNumEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] phoneNum = {""};
        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    phoneNum[0] = "";
                }
                if (value != null && value.exists()) {
                    phoneNum[0] =  value.getData().get("ownerName").toString();


                }
            }
        });
        return phoneNum[0];
    }
    public String getEmailEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] email = {""};
        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    email[0] = "";
                }
                if (value != null && value.exists()) {
                    email[0] =  value.getData().get("ownerName").toString();


                }
            }
        });
        return email[0];
    }

    private void getData() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    return;
                }
                if (value != null && value.exists()) {
                    ownerNameEdt.setText(value.getData().get("ownerName").toString());
                    phoneNumEdt.setText(value.getData().get("phone").toString());
                    emailEdt.setText(value.getData().get("email").toString());

                }
            }
        });

    }

//    private void uploadImage(String userName, String dob, String phone, String email) {
//        if (filePath != null) {
//            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(context);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//            String imgID = UUID.randomUUID().toString();
//            // Defining the child of storageReference
//            StorageReference ref = storageRef.child("images/" + imgID);
//            // adding listeners on upload
//            // or failure of image
//            ref.putFile(filePath)
//                    .addOnSuccessListener(
//                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(
//                                        UploadTask.TaskSnapshot taskSnapshot) {
//
//                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            Log.e("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
//                                            addDataToFirebase(userName, dob, phone, email, uri.toString());
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.e("TAG", "DATA S " + e.getMessage());
//                                        }
//                                    });
//                                    progressDialog.dismiss();
//                                }
//                            })
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                            // Error, Image not uploaded
//                            progressDialog.dismiss();
//                            Toast
//                                    .makeText(context,
//                                            "Failed " + e.getMessage(),
//                                            Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    })
//                    .addOnProgressListener(
//                            new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                                // Progress Listener for loading
//                                // percentage on the dialog box
//                                @Override
//                                public void onProgress(
//                                        UploadTask.TaskSnapshot taskSnapshot) {
//                                    double progress
//                                            = (100.0
//                                            * taskSnapshot.getBytesTransferred()
//                                            / taskSnapshot.getTotalByteCount());
//                                    progressDialog.setMessage(
//                                            "Uploaded "
//                                                    + (int) progress + "%");
//                                }
//                            });
//        }
//    }



//    private void selectImage() {
//        // Defining Implicit Intent to mobile gallery
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(
//                Intent.createChooser(
//                        intent,
//                        "Select Image from here..."),
//                PICK_IMAGE_REQUEST);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null) {
//
//            // Get the Uri of data
//            filePath = data.getData();
//            try {
//
//                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore
//                        .Images
//                        .Media
//                        .getBitmap(
//                                context.getContentResolver(),
//                                filePath);
//                profileIV.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                // Log the exception
//                e.printStackTrace();
//            }
//        }
//    }
}