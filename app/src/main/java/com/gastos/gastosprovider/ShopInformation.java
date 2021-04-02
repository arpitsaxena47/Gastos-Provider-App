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

public class ShopInformation extends Fragment {

    private EditText shopNameEdt, shopAddressEdt, shopMapEdt, shopCityEdt, shopCategoryEdt;
    private Button updateShopBtn;
    private Context context;
    private FirebaseAuth mAuth;
    private ImageView shopIV;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final int PICK_IMAGE_REQUEST = 22;

    public ShopInformation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_information, container, false);
        context = container.getContext();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        shopNameEdt = view.findViewById(R.id.idEdtShopName);
        shopAddressEdt = view.findViewById(R.id.idEdtShopAddress);
        shopMapEdt = view.findViewById(R.id.idEdtPin);
        shopCityEdt = view.findViewById(R.id.idEdtShopCity);
        shopCategoryEdt = view.findViewById(R.id.idEdtShopCategory);
        updateShopBtn = view.findViewById(R.id.idBtnShopData);
        shopIV = view.findViewById(R.id.idIVShop);
        getShopData();
        shopIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        updateShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(shopNameEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter shop name..", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(shopAddressEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter shop address..", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(shopMapEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter shop map..", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(shopCityEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter shop city..", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(shopCategoryEdt.getText().toString())) {
                    Toast.makeText(context, "Please enter shop category..", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadImage(shopNameEdt.getText().toString(), shopAddressEdt.getText().toString(), shopMapEdt.getText().toString(), shopCityEdt.getText().toString(), shopCategoryEdt.getText().toString());
            }
        });

        return view;
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


    private void uploadImage(String shopName, String shopAddress, String shopMap, String shopCity, String shopCategory) {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String imgID = UUID.randomUUID().toString();
            // Defining the child of storageReference
            StorageReference ref = storageRef.child("shops/" + imgID);
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
                                            Log.e("TAG", "URI " + uri);
                                            Log.e("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                                            addShopDatatoFirebase(shopName,shopAddress,shopMap,shopCity,shopCategory,uri.toString());
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


    private void addShopDatatoFirebase(String shopName, String shopAddress, String shopMap, String shopCity, String shopCategory,String imgUrl) {
        Map<String, Object> shopInfo = new HashMap<>();
        shopInfo.put("shopName", shopName);
        shopInfo.put("shopAddress", shopAddress);
        shopInfo.put("shopMap", shopMap);
        shopInfo.put("shopCity", shopCity);
        shopInfo.put("shopCategory", shopCategory);
        shopInfo.put("shopImg", imgUrl);
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("Shops").document(userId).set(shopInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void getShopData() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Shops").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    return;
                }
                if (value != null && value.exists()) {
                    shopNameEdt.setText(value.getData().get("shopName").toString());
                    shopAddressEdt.setText(value.getData().get("shopAddress").toString());
                    shopMapEdt.setText(value.getData().get("shopMap").toString());
                    shopCityEdt.setText(value.getData().get("shopCity").toString());
                    shopCategoryEdt.setText(value.getData().get("shopCategory").toString());
                    Picasso.get().load(value.getData().get("shopImg").toString()).into(shopIV);
                }
            }
        });
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
                shopIV.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}