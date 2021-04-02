package com.gastos.gastosprovider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText userNameEdt, dobEdt, phnoEdt, emailEdt;
    private Button addDataBtn;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView profileIV;
    private final int PICK_IMAGE_REQUEST = 22;
    private Button uploadBtn;
    private Uri filePath;
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
        uploadBtn = view.findViewById(R.id.idBtnUpload);
        userNameEdt = view.findViewById(R.id.idEdtUserName);
        dobEdt = view.findViewById(R.id.idEdtDate);
        phnoEdt = view.findViewById(R.id.idEdtPhone);
        emailEdt = view.findViewById(R.id.idEdtEmail);
        addDataBtn = view.findViewById(R.id.idBtnAddData);
        profileIV = view.findViewById(R.id.idIVProfile);
        getData();
        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "Clicked button ", Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(userNameEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter User Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(dobEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter Date of birth", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(phnoEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(emailEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter your Email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadImage(userNameEdt.getText().toString(), dobEdt.getText().toString(), phnoEdt.getText().toString(), emailEdt.getText().toString());

            }
        });
        return view;
    }

    private void addDataToFirebase(String userName, String dob, String phone, String email, String imgUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("userName", userName);
        user.put("dob", dob);
        user.put("phone", phone);
        user.put("email", email);
        user.put("profileUrl", imgUrl);

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


    private void uploadImage(String userName, String dob, String phone, String email) {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String imgID = UUID.randomUUID().toString();
            // Defining the child of storageReference
            StorageReference ref = storageRef.child("images/" + imgID);
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.e("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                                            addDataToFirebase(userName, dob, phone, email, uri.toString());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("TAG", "DATA S " + e.getMessage());
                                        }
                                    });
                                    progressDialog.dismiss();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(context,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
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
                    userNameEdt.setText(value.getData().get("userName").toString());
                    dobEdt.setText(value.getData().get("dob").toString());
                    phnoEdt.setText(value.getData().get("phone").toString());
                    emailEdt.setText(value.getData().get("email").toString());
                    Picasso.get().load(value.getData().get("profileUrl").toString()).placeholder(R.drawable.ic_launcher_background).into(profileIV);
                }
            }
        });

    }

    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                context.getContentResolver(),
                                filePath);
                profileIV.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}