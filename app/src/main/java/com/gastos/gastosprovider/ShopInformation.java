package com.gastos.gastosprovider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.gastos.gastosprovider.Setting.SettingsFragment;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ShopInformation extends Fragment {

    private EditText shopNameEdt, shopAddressEdt;
    private ImageView backShopInfo , saveShopInfoButton , other1, other2 , other3;
    private ImageView shopIV , editShopName , editShopAddress;
    private Spinner categoryDropDown , cityDropDown;
    private Button btnAddPinLocation;
    private Context context;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String shopPicUrl = null , other1Url = null,other2Url = null , other3Url = null;

    public static final int REQUEST_IMAGE = 100;


   private String selectedImage = "";

   private Uri filePath ;
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

        backShopInfo = view.findViewById(R.id.shop_info_back);

        other1 = view.findViewById(R.id.other1);
        other2 = view.findViewById(R.id.other2);
        other3 = view.findViewById(R.id.other3);

        editShopName = view.findViewById(R.id.editShopName);
        editShopAddress = view.findViewById(R.id.editShopAddress);

        categoryDropDown = view.findViewById(R.id.dropDownCategory);
        cityDropDown = view.findViewById(R.id.dropdownCity);

        btnAddPinLocation = view.findViewById(R.id.btnShopLocation);

        saveShopInfoButton = view.findViewById(R.id.saveShopInfoChanges);
        saveShopInfoButton.setVisibility(View.GONE);

        shopIV = view.findViewById(R.id.idIVShop);
        loadProfileDefault();
        ImagePickerActivity.clearCache(context);

        editShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopNameEdt.setOnClickListener(this);
            }
        });

        editShopAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopAddressEdt.setOnClickListener(this);
            }
        });
        shopIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = "shopIV";
                onProfileImageClick();
            }
        });

        other1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage = "other1";
                onProfileImageClick();
            }
        });

        other2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage = "other2";
                onProfileImageClick();
            }
        });

        other3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage = "other3";
                onProfileImageClick();
            }
        });

        saveShopInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopNameEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter shop name..", Toast.LENGTH_SHORT).show();
                    return;
                }  if (shopAddressEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter shop address..", Toast.LENGTH_SHORT).show();
                    return;
                }
                 if(shopPicUrl.isEmpty()){
                    Toast.makeText(context, "Please Set Shop Profile Picture....", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveShopInfoButton.setVisibility(View.GONE);
                uploadImage(shopNameEdt.getText().toString(), shopAddressEdt.getText().toString(), shopPicUrl , other1Url , other2Url , other3Url);

                Fragment fragment = new SettingsFragment();
                getFragmentManager().beginTransaction().replace(R.id.idFLContainer,fragment).commit();
            }
        });

        shopNameEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(getShopNameEdt()))
                {
                    saveShopInfoButton.setVisibility(View.VISIBLE);
                }
                else
                if( shopAddressEdt.getText().toString().equals(getShopAddressEdt()) && shopPicUrl == null &&
                        other1Url == null && other2Url == null && other3Url == null)
                {
                    saveShopInfoButton.setVisibility(View.GONE);
                }
            }
        });

        shopAddressEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals(getShopAddressEdt()))
                {
                    saveShopInfoButton.setVisibility(View.VISIBLE);
                }
                else
                if( shopNameEdt.getText().toString().equals(getShopNameEdt()) && shopPicUrl == null &&
                        other1Url == null && other2Url == null && other3Url == null)
                {
                    saveShopInfoButton.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    public String getShopNameEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] shopName = {""};
        DocumentReference documentReference = db.collection("Merchant_data").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    shopName[0] = "";
                }
                else
                if (value != null && value.exists()) {
                    shopName[0] =  value.getData().get("ShopName").toString();


                }
            }
        });
        return shopName[0];
    }

    public String getShopAddressEdt() {
        String userId = mAuth.getCurrentUser().getUid();
        final String[] shopAddress = {""};
        DocumentReference documentReference = db.collection("Merchant_data").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    shopAddress[0] = "";
                }
                else
                if (value != null && value.exists()) {
                    shopAddress[0] =  value.getData().get("ShopAddress").toString();


                }
            }
        });
        return shopAddress[0];
    }

    private void loadProfile(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        shopPicUrl = url;
        Picasso.get().load(url).into(shopIV);
        shopIV.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
    }

    private void loadOther1(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        other1Url = url;
        Picasso.get().load(url).into(other1);
        other1.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
    }

    private void loadOther2(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        other2Url = url;
        Picasso.get().load(url).into(other2);
        other2.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
    }

    private void loadOther3(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        other3Url = url;
        Picasso.get().load(url).into(other3);
        other3.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
    }


    private void loadProfileDefault() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Merchant_data").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "NO DATA FOUND");
                    return;
                }
                if (value != null && value.exists()) {
                    if(!value.getData().get("ShopName").toString().isEmpty())
                    shopNameEdt.setText(value.getData().get("ShopName").toString());

                    if(!value.getData().get("ShopAddress").toString().isEmpty())
                    shopAddressEdt.setText(value.getData().get("ShopAddress").toString());
                    if(!value.getData().get("ShopPic").toString().isEmpty())
                    Picasso.get().load(value.getData().get("ShopPic").toString())
                            .into(shopIV);
                    if(!value.getData().get("Other1").toString().isEmpty())
                    Picasso.get().load(value.getData().get("Other1").toString())
                            .into(shopIV);
                    if(!value.getData().get("Other2").toString().isEmpty())
                    Picasso.get().load(value.getData().get("Other2").toString())
                            .into(shopIV);
                    if(!value.getData().get("Other3").toString().isEmpty())
                    Picasso.get().load(value.getData().get("Other3").toString())
                            .into(shopIV);


                }
            }
        });
    }

    void onProfileImageClick() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(context, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                    // loading profile image from local cache
                    if(selectedImage == "shopIV")
                        loadProfile(uri.toString());
                    else
                    if(selectedImage == "other1")
                        loadOther1(uri.toString());
                    else
                    if(selectedImage == "other2")
                        loadOther2(uri.toString());
                    else
                    if(selectedImage == "other3")
                        loadOther3(uri.toString());

                    saveShopInfoButton.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void addDataToFirebase(String shopName, String shopAddress, String shopPic , String other1 , String other2 , String other3) {

        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(true);

        Map<String, Object> user = new HashMap<>();
        user.put("ShopName",shopName);
        user.put("ShopAddress", shopAddress);
        user.put("ShopPic" , shopPic);
//        if(!other1.equals(""))
            user.put("Other1", other1);
//        if(!other2.equals(""))
            user.put("Other2", other2);
//        if(!other3.equals(""))
            user.put("Other3", other3);

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("Merchant_data").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Data updated successfully..", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else {
                    Toast.makeText(context, "Fail to Add Data.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

        private void uploadImage(String shopName, String shopAddress, String shopPic , String other1 , String other2 , String other3) {
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
                                            addDataToFirebase(shopName,  shopAddress,  shopPic , other1 ,  other2 ,  other3);
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
}




