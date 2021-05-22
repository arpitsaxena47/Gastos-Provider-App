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
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(QRScannerActivity.this, new String[]{Manifest.permission.CAMERA}
                    , RequestCameraPermissionId);
            return;
        }

        try {
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
        if (isPermissionGranted)
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
//                        Toast.makeText(QRScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        String input = result.getText();
//                        Pattern pattern = Pattern.compile("[a-zA-z0-9]+@[a-zA-z0-9]+");
//                        Pattern pattern = Pattern.compile("[a-zA-z0-9]");

                        if (input.length() > 10 && input.indexOf("?pa=")+4 < input.indexOf("&"))
                            input = input.substring(input.indexOf("?pa=") + 4, input.indexOf("&"));
                        if (input.matches("[a-zA-Z0-9\\.\\-]{2,256}\\@[a-zA-Z][a-zA-Z]{2,64}")) {
                            Intent paymentInfoIntent = new Intent();
                            paymentInfoIntent.putExtra("UPI", input);
                            setResult(Activity.RESULT_OK, paymentInfoIntent);
                            finish();
                        } else {
                            Toast.makeText(QRScannerActivity.this, "Invalid UPI !! " + input, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

}