package com.gastos.gastosprovider.Setting.PaymentInformation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.gastos.gastosprovider.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRScannerActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private boolean isPermissionGranted = false;
    private final int RequestCameraPermissionId = 50;
    private String str , meruid;
    private int a = 0;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    String upiRegex = "^[\\w\\.\\-_]{3,}@[a-zA-Z]{3,}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(QRScannerActivity.this , new String[]{Manifest.permission.CAMERA}
                    , RequestCameraPermissionId);
            return;
        }

        try
        {
            isPermissionGranted = true;
            startScanner();

        } catch (Exception e) {
            e.printStackTrace();
        }




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionId:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        isPermissionGranted = true;
                        startScanner();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPermissionGranted)
            startScanner();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        super.onPause();
    }

    public void startScanner() {
        mCodeScanner.startPreview();
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);// or CAMERA_FRONT or specific camera id
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS); // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE); // or CONTINUOUS
        mCodeScanner.setScanMode(ScanMode.SINGLE); // or CONTINUOUS or PREVIEW
        mCodeScanner.setAutoFocusEnabled(true); // Whether to enable auto focus or not
        mCodeScanner.setFlashEnabled(false);// Whether to enable flash or not
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QRScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        String input = result.getText();
//                        Pattern pattern = Pattern.compile("[a-zA-z0-9]+@[a-zA-z0-9]+");
//                        Pattern pattern = Pattern.compile("[a-zA-z0-9]");

//                        if(input.matches("[a-zA-Z0-9]+@+ [a-zA-Z0-9]"))
                        {
                            Intent paymentInfoIntent = new Intent(QRScannerActivity.this , PaymentInformation.class);
                            paymentInfoIntent.putExtra("UPI",input);
                            setResult(Activity.RESULT_OK,paymentInfoIntent);
                            finish();
                        }
//                        else
//                        {
//                            Toast.makeText(QRScannerActivity.this, "Invalid UPI !! ", Toast.LENGTH_SHORT).show();
//                        }

                    }
                });
            }
        });
    }




//    private ScannerLiveView camera;
//    private TextView qrCodeTV;
//    private ImageView qrCodeIV;
//    private EditText upiEdt;
//    private Button submitQRBtn;
//    FirebaseStorage storage;
//    StorageReference storageRef;
//    String scannedQRCOde;
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    FirebaseAuth mauth;
//    String userID;
//    private boolean isPermissionGranted = false;
//    private final int RequestCameraPermissionId = 50;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_q_r_scanner);
//        qrCodeIV = findViewById(R.id.idIVQrCode);
//        camera = findViewById(R.id.camview);
//        upiEdt = findViewById(R.id.idEdtUPI);
//        submitQRBtn = findViewById(R.id.idBtnAddPayment);
//        mauth = FirebaseAuth.getInstance();
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
////        userID = mauth.getCurrentUser().getUid();
//
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(QRScannerActivity.this , new String[]{Manifest.permission.CAMERA}
//                    , RequestCameraPermissionId);
//            return;
//        }
//
//        try
//        {
//            isPermissionGranted = true;
//            camera.startScanner();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
////        qrCodeTV = findViewById(R.id.idTVQrCode);
////        if (checkPermission()) {
////            // if permission is already granted display a toast message
////            // Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
////        } else {
////            requestPermission();
////        }
//            camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
//                @Override
//                public void onScannerStarted(ScannerLiveView scanner) {
//                    // method is called when scanner is started
//                    //   Toast.makeText(QRScannerActivity.this, "Scanner Started", Toast.LENGTH_SHORT).show();
//                    camera.startScanner();
//                }
//
//                @Override
//                public void onScannerStopped(ScannerLiveView scanner) {
//                    // method is called when scanner is stoped.
//                    // Toast.makeText(QRScannerActivity.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onScannerError(Throwable err) {
//                    // method is called when scanner gives some error.
//                    //   Toast.makeText(QRScannerActivity.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCodeScanned(String data) {
//                    // method is called when camera scans the
//                    // qr code and the data from qr code is
//                    // stored in data in string format.
//                    setQRCode(data);
//                }
//            });
//
//
//            submitQRBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String upiId = upiEdt.getText().toString();
//                    if (TextUtils.isEmpty(upiId)) {
//                        Toast.makeText(QRScannerActivity.this, "Please enter URP id..", Toast.LENGTH_SHORT).show();
//                        return;
//                    } else {
////                        uploadQRCodeToFirebase(upiId);
//                    }
//                }
//            });
//
//
//    }
//
//    private void uploadQRCodeToFirebase(String upiId) {
//// Code for showing progressDialog while uploading
//        ProgressDialog progressDialog
//                = new ProgressDialog(this);
//        progressDialog.setTitle("Uploading...");
//        progressDialog.show();
//        String imgID = UUID.randomUUID().toString();
//        // Defining the child of storageReference
//        StorageReference ref = storageRef.child("qrCodes/" + imgID);
//
//        qrCodeIV.setDrawingCacheEnabled(true);
//        qrCodeIV.buildDrawingCache();
//        Bitmap bitmap = qrCodeIV.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Log.e("TAG", "URI " + uri);
//                        Log.e("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
//                        addDataToFirebase(scannedQRCOde, upiId, uri.toString());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("TAG", "DATA S " + e.getMessage());
//                    }
//                });
//            progressDialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
//                Toast
//                        .makeText(QRScannerActivity.this,
//                                "Failed " + e.getMessage(),
//                                Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }).addOnProgressListener(
//                new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                    // Progress Listener for loading
//                    // percentage on the dialog box
//                    @Override
//                    public void onProgress(
//                            UploadTask.TaskSnapshot taskSnapshot) {
//                        double progress
//                                = (100.0
//                                * taskSnapshot.getBytesTransferred()
//                                / taskSnapshot.getTotalByteCount());
//                        progressDialog.setMessage(
//                                "Uploaded "
//                                        + (int) progress + "%");
//                    }
//                });
//
//    }
//
//    private void addDataToFirebase(String scannedQRCode, String upiId, String qrCodeUrl) {
//        Map<String, Object> user = new HashMap<>();
//        user.put("scannedCode", scannedQRCode);
//        user.put("upiId", upiId);
//        user.put("qrCodeImg", qrCodeUrl);
//
//        CollectionReference dbQrCodes = db.collection("QRCodes");
//        dbQrCodes.document(userID).collection("QRCODES").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(QRScannerActivity.this, "QR Code Updated..", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(QRScannerActivity.this, "Fail to update QR Code..", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void requestPermission() {
//        // this method is to request
//        // the runtime permission.
//        int PERMISSION_REQUEST_CODE = 200;
//        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        ZXDecoder decoder = new ZXDecoder();
//        // 0.5 is the area where we have
//        // to place red marker for scanning.
//        decoder.setScanAreaPercent(0.8);
//        // below method will set secoder to camera.
//        camera.setDecoder(decoder);
//        if(isPermissionGranted)
//        camera.startScanner();
//    }
//
//    @Override
//    protected void onPause() {
//        // on app pause the
//        // camera will stop scanning.
//        camera.stopScanner();
//        super.onPause();
//    }
//
//    private boolean checkPermission() {
//        // here we are checking two permission that is vibrate
//        // and camera which is granted by user and not.
//        // if permission is granted then we are returning
//        // true otherwise false.
//        int camera_permission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
////        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
//        return camera_permission == PackageManager.PERMISSION_GRANTED ;
////                && vibrate_permission == PackageManager.PERMISSION_GRANTED;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case RequestCameraPermissionId:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//                        return;
//                    }
//                    try {
//                        isPermissionGranted = true;
//                        camera.startScanner();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                break;
//        }
//    }
//
//
//
//
//    private void setQRCode(String code) {
//        Log.e("TAG", "CODE IS " + code);
//        // Whatever you need to encode in the QR code
//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE, 200, 200);
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//            qrCodeTV.setText(code);
//            scannedQRCOde = code;
//            qrCodeIV.setVisibility(View.VISIBLE);
//            camera.setVisibility(View.GONE);
//            qrCodeTV.setVisibility(View.GONE);
//            qrCodeIV.setImageBitmap(bitmap);
//            upiEdt.setVisibility(View.VISIBLE);
//            submitQRBtn.setVisibility(View.VISIBLE);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }
}