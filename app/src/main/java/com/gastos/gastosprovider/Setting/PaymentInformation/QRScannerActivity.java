package com.gastos.gastosprovider.Setting.PaymentInformation;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gastos.gastosprovider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission_group.CAMERA;

public class QRScannerActivity extends AppCompatActivity {
    private ScannerLiveView camera;
    private TextView qrCodeTV;
    private ImageView qrCodeIV;
    private EditText upiEdt;
    private Button submitQRBtn;
    FirebaseStorage storage;
    StorageReference storageRef;
    String scannedQRCOde;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mauth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);
//        qrCodeIV = findViewById(R.id.idIVQrCode);
//        camera = findViewById(R.id.camview);
//        upiEdt = findViewById(R.id.idEdtUPI);
//        submitQRBtn = findViewById(R.id.idBtnAddPayment);
//        mauth = FirebaseAuth.getInstance();
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
////        userID = mauth.getCurrentUser().getUid();
//
//        qrCodeTV = findViewById(R.id.idTVQrCode);
//        if (checkPermission()) {
//            // if permission is already granted display a toast message
//            // Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
//        } else {
//            requestPermission();
//        }
//        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
//            @Override
//            public void onScannerStarted(ScannerLiveView scanner) {
//                // method is called when scanner is started
//                //   Toast.makeText(QRScannerActivity.this, "Scanner Started", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onScannerStopped(ScannerLiveView scanner) {
//                // method is called when scanner is stoped.
//                // Toast.makeText(QRScannerActivity.this, "Scanner Stopped", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onScannerError(Throwable err) {
//                // method is called when scanner gives some error.
//                //   Toast.makeText(QRScannerActivity.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCodeScanned(String data) {
//                // method is called when camera scans the
//                // qr code and the data from qr code is
//                // stored in data in string format.
//                setQRCode(data);
//            }
//        });
//
//        submitQRBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String upiId = upiEdt.getText().toString();
//                if (TextUtils.isEmpty(upiId)) {
//                    Toast.makeText(QRScannerActivity.this, "Please enter URP id..", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    uploadQRCodeToFirebase(upiId);
//                }
//            }
//        });
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
//        ActivityCompat.requestPermissions(this, new String[]{CAMERA, VIBRATE}, PERMISSION_REQUEST_CODE);
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
//        int vibrate_permission = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
//        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        // this method is called when user
//        // allows the permission to use camera.
//        if (grantResults.length > 0) {
//            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//            if (cameraaccepted && vibrateaccepted) {
//                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Permission Denined \n You cannot use app without providing permssion", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
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
    }
}