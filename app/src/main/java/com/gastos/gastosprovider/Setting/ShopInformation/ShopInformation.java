package com.gastos.gastosprovider.Setting.ShopInformation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gastos.gastosprovider.HomeActivity;
import com.gastos.gastosprovider.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ShopInformation extends AppCompatActivity {
    private static final String TAG = "Location of shop";
     private double gotlocationlatitude=0;
    private double gotlocationlongitude=0;
    private EditText shopNameEdt, shopAddressEdt;
    private TextView txtCoverPhoto , txtOther1 , txtOther2 , txtOther3 ;
    private ImageView backShopInfo , saveShopInfoButton , other1, other2 , other3;
    private ImageView shopIV ;
    private Spinner categoryDropDown , locationDropDown;
    private Button btnAddPinLocation;
    private Context context;
    private FirebaseAuth mAuth;
    DatabaseReference ref;
    private final LatLng mDefaultLocation = new LatLng(30.7691099, 76.5758537);
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient   mFusedLocationProviderClient ;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private String shopPicUrl = "" , other1Url = "",other2Url = "" , other3Url = "" , location =  "" , category = "";
    private String prevShopName = "" , prevShopAddress = "" , prevLocation = "" , prevCategory = "" ,
            prevShopUrl = "" , prevOther1 = "" , prevOther2 = "" , prevOther3 = "" ;

    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();


    public static final int REQUEST_IMAGE = 100;


   private String selectedImage = "";

//   private int flag1 = 0 , flag2 =0;
    public ShopInformation() {
        // Required empty public constructor

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_shop_information);
        context = ShopInformation.this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mAuth = FirebaseAuth.getInstance();
        shopNameEdt = findViewById(R.id.idEdtShopName);
        shopAddressEdt = findViewById(R.id.idEdtShopAddress);

        backShopInfo = findViewById(R.id.shop_info_back);

        txtCoverPhoto = findViewById(R.id.txtCoverPhoto);
        txtOther1 = findViewById(R.id.txtOther1);
        txtOther2 = findViewById(R.id.txtOther2);
        txtOther3 = findViewById(R.id.txtOther3);


        other1 = findViewById(R.id.other1);
        other2 = findViewById(R.id.other2);
        other3 = findViewById(R.id.other3);

//        editShopName = findViewById(R.id.editShopName);
//        editShopAddress = findViewById(R.id.editShopAddress);

        categoryDropDown = findViewById(R.id.dropDownCategory);
        locationDropDown = findViewById(R.id.dropdownCity);

        btnAddPinLocation = findViewById(R.id.btnShopLocation);

        saveShopInfoButton = findViewById(R.id.saveShopInfoChanges);

        shopIV = findViewById(R.id.idIVShop);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        loadProfileDefault();
//        saveShopInfoButton.setVisibility(View.GONE);
        ImagePickerActivity.clearCache(context);

//        editShopName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(flag1 == 0 && flag2 == 0){
//                    flag1 = 1;
//                    shopNameEdt.setFocusable(true);
//                    shopNameEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
//                    shopNameEdt.setClickable(true); // user navigates with wheel and selects widget
//
//                    editShopName.setBackgroundColor(Color.GREEN);
//                }
//                else{
//                    flag1 = 0;
//                    shopNameEdt.setFocusable(false);
//                    shopNameEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
//                    shopNameEdt.setClickable(false); // user navigates with wheel and selects widget
//
//                    editShopName.setBackgroundColor(Color.TRANSPARENT);
//                }
//
//            }
//        });

//        editShopAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(flag1 == 0 && flag2 == 0){
//                    flag2 = 1;
//                    shopAddressEdt.setFocusable(true);
//                    shopAddressEdt.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
//                    shopAddressEdt.setClickable(true); // user navigates with wheel and selects widget
//
//                    editShopAddress.setBackgroundColor(Color.GREEN);
//                }
//                else{
//                    flag2 = 0;
//                    shopAddressEdt.setFocusable(false);
//                    shopAddressEdt.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
//                    shopAddressEdt.setClickable(false); // user navigates with wheel and selects widget
//
//                    editShopAddress.setBackgroundColor(Color.TRANSPARENT);
//                }
//            }
//        });

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

        backShopInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveShopInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopNameEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter shop name..", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (shopAddressEdt.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter shop address..", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(category.isEmpty() )
                {
                    Toast.makeText(context, "Please Set Shop Category....", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(location.isEmpty() )
                {
                    Toast.makeText(context, "Please Set Shop Location....", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(shopPicUrl.isEmpty()){
                    Toast.makeText(context, "Please Set Shop Profile Picture....", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (gotlocationlongitude==0|| gotlocationlatitude==0){
                    Toast.makeText(context, "Add Location Pin....", Toast.LENGTH_SHORT).show();
                    return;
                }


                addDataToFirebase(shopNameEdt.getText().toString(), shopAddressEdt.getText().toString(), shopPicUrl , other1Url ,
                        other2Url , other3Url , location , category,gotlocationlatitude,gotlocationlongitude );
                saveShopInfoButton.setVisibility(View.GONE);
//                Fragment fragment = new SettingsFragment();
//                getFragmentManager().beginTransaction().replace(R.id.idFLContainer,fragment).commit();
                Intent homeIntent = new Intent(ShopInformation.this , HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });

        //For adding location in backend
        btnAddPinLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(shopAddressEdt.getText().toString().isEmpty()){
//                    Toast.makeText(context, "Please enter shop address first...", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                   Geolocation geolocation=new Geolocation();
//                    geolocation.getAddress(shopAddressEdt.getText().toString(),
//                            getApplicationContext(), new GeocoderHandler());
//
//                }

                // Prompt the user for permission.
               getLocationPermission();



                // Get the current location of the device and set the position of the map.
                getDeviceLocation();

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
                if(!editable.toString().equals(prevShopName))
                {
                    saveShopInfoButton.setVisibility(View.VISIBLE);
                }
                else
                if( shopAddressEdt.getText().toString().equals(prevShopAddress) && shopPicUrl.equals(prevShopUrl) &&
                        other1Url.equals(prevOther1)   &&  other2Url.equals(prevOther2)
                        &&  other3Url.equals(prevOther3) && location.equals(prevLocation) && category.equals(prevCategory))
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
                if(!editable.toString().equals(prevShopAddress))
                {
                    saveShopInfoButton.setVisibility(View.VISIBLE);

                }
                else
                if( shopNameEdt.getText().toString().equals(prevShopName)  &&  shopPicUrl.equals(prevShopUrl) &&
                        other1Url.equals(prevOther1)   && other2Url.equals(prevOther2)
                        && other3Url.equals(prevOther3) && location.equals(prevLocation) && category.equals(prevCategory))
                {
                    saveShopInfoButton.setVisibility(View.GONE);
                }
            }
        });

        fillLocations(locations);
        fillCategories(categories);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(ShopInformation.this, android.R.layout.simple_spinner_dropdown_item, categories){
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryDropDown.setAdapter(categoryAdapter);
        categoryDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if( parent.getItemAtPosition(position).toString().equals(prevCategory) ||
                        parent.getItemAtPosition(position).toString().equals(categories.get(categories.size()-1) ))
                {
                    saveShopInfoButton.setVisibility(View.GONE);
                    category = prevCategory;
                }
                else {
                    saveShopInfoButton.setVisibility(View.VISIBLE);
                    category = parent.getItemAtPosition(position).toString();
                    categoryDropDown.setSelection(position);
                }

//                 txtCategory.setVisibility(View.GONE);
//                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,  Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(ShopInformation.this, android.R.layout.simple_spinner_dropdown_item, locations){
            @Override
            public int getCount() {
                return super.getCount() -1;
            }
        };
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationDropDown.setAdapter(locationAdapter);
        locationDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                location = parent.getItemAtPosition(position).toString();
                if( parent.getItemAtPosition(position).toString().equals(prevLocation) ||
                        parent.getItemAtPosition(position).toString().equals(locations.get(locations.size()-1)))
                {
                    saveShopInfoButton.setVisibility(View.GONE);
                    location = prevLocation;
                }
                else {

                    saveShopInfoButton.setVisibility(View.VISIBLE);
                    location = parent.getItemAtPosition(position).toString();
                    locationDropDown.setSelection(position);
                }


//                txtLocation.setVisibility(View.GONE);
//                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,  Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

    }



//    public String getShopNameEdt() {
//        String userId = mAuth.getCurrentUser().getUid();
//        final String[] shopName = {""};
//        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("ShopName");
//        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
////                   Log.e("firebase", "Error getting data", task.getException());
//                    shopName[0]  = null;
//                }
//                else {
//                    shopName[0] = task.getResult().getValue().toString();
////                   Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });
//        return shopName[0];
//    }
//
//    public String getShopAddressEdt() {
//        String userId = mAuth.getCurrentUser().getUid();
//        final String[] shopAddress = {""};
//        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("ShopAddress");
//        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
////                   Log.e("firebase", "Error getting data", task.getException());
//                    shopAddress[0]  = null;
//                }
//                else {
//                    shopAddress[0] = task.getResult().getValue().toString();
////                   Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });
//        return shopAddress[0];
//    }

    private void loadProfile(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        shopPicUrl = url;
        Picasso.get().load(url).into(shopIV);
        shopIV.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        txtCoverPhoto.setVisibility(View.GONE);
    }

    private void loadOther1(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        other1Url = url;
        Picasso.get().load(url).into(other1);
        other1.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        txtOther1.setVisibility(View.GONE);
    }

    private void loadOther2(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        other2Url = url;
        Picasso.get().load(url).into(other2);
        other2.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        txtOther2.setVisibility(View.GONE);
    }

    private void loadOther3(String url ) {
        Log.d("TAG", "Image cache path: " + url);

        other3Url = url;
        Picasso.get().load(url).into(other3);
        other3.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        txtOther3.setVisibility(View.GONE);
    }


    private void loadProfileDefault() {
        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Fetching Data!! Please Wait...");
        progressDialog.show();

        String userId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Shop_Information");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
//                   Log.e("firebase", "Error getting data", task.getException());
                    locationDropDown.setSelection(locations.size()-1);
                    categoryDropDown.setSelection(categories.size()-1);
                    Toast.makeText(context, "Check Your Internet Connection..." , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
                else {
                    if(task.getResult().getValue() != null) {

//                        AccountData accountData = task.getResult(AccountData.class);

                        prevShopName = task.getResult().child("ShopName").getValue() != null?task.getResult().child("ShopName").getValue() + "" : "";
                        prevShopAddress = task.getResult().child("ShopAddress").getValue() !=null?task.getResult().child("ShopAddress").getValue() + "" : "";

//                        if(task.getResult().child("ShopName").getValue() != null)
                            shopNameEdt.setText(prevShopName);

//                        if(!task.getResult().child("ShopAddress").getValue().toString().trim().isEmpty())
                            shopAddressEdt.setText(prevShopAddress);
                        if(task.getResult().child("ShopPic").getValue() != null)
                        {
                            Picasso.get().load(task.getResult().child("ShopPic").getValue()+"")
                                    .into(shopIV);
                            shopPicUrl = task.getResult().child("ShopPic").getValue()+"";
                            prevShopUrl = shopPicUrl;
                            txtCoverPhoto.setVisibility(View.GONE);
                        }

                        if(task.getResult().child("OtherImages").child("Other1").getValue() != null){

                            Picasso.get().load(task.getResult().child("OtherImages").child("Other1").getValue()+"")
                                    .into(other1);
                            other1Url = task.getResult().child("OtherImages").child("Other1").getValue()+"";
                            prevOther1 = other1Url;
                            txtOther1.setVisibility(View.GONE);
                        }

                        if(task.getResult().child("OtherImages").child("Other2").getValue()!= null){

                            Picasso.get().load(task.getResult().child("OtherImages").child("Other2").getValue()+"")
                                    .into(other2);
                            other2Url = task.getResult().child("OtherImages").child("Other2").getValue()+"";
                            prevOther2 = other2Url;
                            txtOther2.setVisibility(View.GONE);
                        }

                        if(task.getResult().child("OtherImages").child("Other3").getValue() != null){
                            Picasso.get().load(task.getResult().child("OtherImages").child("Other3").getValue()+"")
                                    .into(other3);
                            other3Url = task.getResult().child("OtherImages").child("Other3").getValue()+"";
                            prevOther3 = other3Url;
                            txtOther3.setVisibility(View.GONE);
                        }

                        if(task.getResult().child("Category").getValue() != null)
                        {
                            categoryDropDown.setSelection(categories.indexOf(task.getResult().child("Category").getValue()+""));
                            prevCategory = task.getResult().child("Category").getValue()+"";
                            category = prevCategory;

                        }
                        else{
                            categoryDropDown.setSelection(categories.size()-1);
                        }

                        if(task.getResult().child("Location").getValue() != null)
                        {
                            locationDropDown.setSelection(locations.indexOf(task.getResult().child("Location").getValue()+""));
                            prevLocation = task.getResult().child("Location").getValue()+"";
                            location = prevLocation;

                        }
                        else{
                            locationDropDown.setSelection(locations.size()-1);
                        }

                        if(task.getResult().child("ShopAddressLatitude").getValue() != null &&task.getResult().child("ShopAddressLogitude").getValue() != null  )
                        {

                           gotlocationlatitude = Double.parseDouble(task.getResult().child("ShopAddressLatitude").getValue() != null?task.getResult().child("ShopAddressLatitude").getValue()+"" : "");
                            gotlocationlongitude= Double.parseDouble(task.getResult().child("ShopAddressLogitude").getValue() != null?task.getResult().child("ShopAddressLogitude").getValue()+"" : "");
                        }

                        progressDialog.dismiss();
                    }
                    else {
                        locationDropDown.setSelection(locations.size()-1);
                        categoryDropDown.setSelection(categories.size()-1);
                        Toast.makeText(context, "No Data Found..." , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        });


    }

    void onProfileImageClick() {
        Dexter.withActivity(ShopInformation.this)
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                    // loading profile image from local cache
                    if (selectedImage == "shopIV")
                        loadProfile(uri.toString());
                    else if (selectedImage == "other1")
                        loadOther1(uri.toString());
                    else if (selectedImage == "other2")
                        loadOther2(uri.toString());
                    else if (selectedImage == "other3")
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


    private void addDataToFirebase(String shopName, String shopAddress, String shopPic , String other1 , String other2 ,
                                   String other3,  String location, String category,double locationLatitude,double locationLogitude) {

        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(true);

        String l1=Double.toString(locationLatitude);
        String l2=Double.toString(locationLogitude);
        Map<String, Object> user = new HashMap<>();
        user.put("ShopName",shopName);
        user.put("ShopAddress", shopAddress);
        user.put("ShopPic" , shopPic);
        user.put("Location" , location);
        user.put("Category" , category);
        user.put("ShopAddressLatitude" , l1);
        user.put("ShopAddressLogitude" , l2);


        String userId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Shop_Information");

        ref.setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                            Log.i("jfbvkj", "onComplete: ");
                        if(task.isSuccessful()){
//                            Toast.makeText(context, "Data updated successfully..", Toast.LENGTH_SHORT).show();
//                            progressDialog.dismiss();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                            Log.i("jfbvkj", "onFailure: "+e.toString());
//                        Toast.makeText(context, "Fail to Add Data.", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                    Log.i("jfbvkj", "onSuccess: ");
//                Toast.makeText(context, "Data updated successfully..", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }
        });


        Map<String, Object> other = new HashMap<>();

        if(!other1.equals(""))
        other.put("Other1", other1);
        if(!other2.equals(""))
        other.put("Other2", other2);
        if(!other3.equals(""))
        other.put("Other3", other3);

        ref.child("OtherImages").setValue(other).addOnCompleteListener(new OnCompleteListener<Void>() {
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

//        private void uploadImage(String shopName, String shopAddress, String shopPic , String other1 , String other2 ,
//                                 String other3,  String location, String category) {
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
//                                            addDataToFirebase(shopName,  shopAddress,  shopPic , other1 ,  other2 ,  other3 , location , category);
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

    private ArrayList<String> fillLocations(ArrayList<String> arr)
    {
        arr.add("Alwar");
        arr.add("Bhiwadi");
        arr.add("Chandigarh");
        arr.add("Delhi");
        arr.add("Ludhiana");
        arr.add("Mumbai");
        arr.add("Patna");
        arr.add("Kolkata");
        arr.add("Select Location");

        return arr;

    }

    private ArrayList<String> fillCategories(ArrayList<String> arr)
    {
        arr.add("Food");
        arr.add("Salon");
        arr.add("Pub");
        arr.add("General Store");
        arr.add("Club");
        arr.add("Lounges");
        arr.add("Boutique");
        arr.add("Grocery");
        arr.add("Medical");
        arr.add("Select Category");


        return arr;

    }

//    private class GeocoderHandler extends Handler {
//        @Override
//        public void handleMessage(Message message) {
//
//            switch (message.what) {
//                case 1:
//                    Bundle bundle = message.getData();
//                    gotlocationlatitude = bundle.getString("lati");
//                    gotlocationlongitude = bundle.getString("logi");
//                    break;
//
//                default:
//                    gotlocationlatitude = null;
//                    gotlocationlongitude = null;
//
//            }
//           // Log.e(TAG, gotlocationlatitude);
//          //  Log.e(TAG, gotlocationlongitude);
//           // Toast.makeText(context, "Latitude=" +gotlocationlatitude+"  " + "Longitude="+gotlocationlongitude, Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "Location has been added successfully", Toast.LENGTH_SHORT).show();
//        }
//    }
//

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    mLocationPermissionGranted = true;
                    getDeviceLocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                              //  Log.d(TAG, "Lati"+mLastKnownLocation.getLatitude() + "Logi"+ mLastKnownLocation.getLongitude());
                                gotlocationlongitude=mLastKnownLocation.getLongitude();
                                gotlocationlatitude=mLastKnownLocation.getLatitude();

                                if(gotlocationlongitude!=0  && gotlocationlatitude!=0)
                                    Toast.makeText(context, "Location coordinates added successfully", Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(context, "Lati="+gotlocationlatitude +"Logi="+gotlocationlongitude, Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context, "Location Can't be fetched", Toast.LENGTH_SHORT).show();
//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                        new LatLng(mLastKnownLocation.getLatitude(),
//                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//
//
//
//                                MarkerOptions markerOptions = new MarkerOptions();
//
//                                // Setting the position for the marker
//                                LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
//
//                                markerOptions.position(latLng);
//
//                                // Setting the title for the marker.
//                                // This will be displayed on taping the marker
//                                markerOptions.title("Your Location");
//                                Toast.makeText(MapsActivity.this,latLng.latitude+"  "+latLng.longitude,Toast.LENGTH_SHORT).show();
//                                // Clears the previously touched position
//                                mMap.clear();
//
//                                // Animating to the touched position
//                                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                                // Placing a marker on the touched position
//                                mMap.addMarker(markerOptions);
                            }
                        } else {
                           // Log.d(TAG, "Current location is null. Using defaults.");
                           // Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            Toast.makeText(context, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    }





