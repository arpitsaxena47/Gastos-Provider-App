package com.gastos.gastosprovider.Setting.PaymentInformation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gastos.gastosprovider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentInformation extends AppCompatActivity {

    private CardView addQRCodeCV;
    ImageView savePaymentInfo , btnBackPayment;
    private Context context;
    private RecyclerView qrCodeRV;
    private QRCodeRVAdapter qrCodeRVAdapter;
    private ArrayList<QRCodeRVModal> qrCodeRVModalArrayList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference ref;

    public PaymentInformation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment_information);
        addQRCodeCV = findViewById(R.id.idCVAddCode);
        qrCodeRV = findViewById(R.id.idRVQrCodes);

        btnBackPayment = findViewById(R.id.payment_info_back);
        savePaymentInfo = findViewById(R.id.savePaymentChanges);

        context = PaymentInformation.this;
//        activity = getActivity();
//        userID = mAuth.getCurrentUser().getUid();
        qrCodeRVModalArrayList = new ArrayList<>();

        getDataQRCodeDataFromFirebase();

        qrCodeRVAdapter = new QRCodeRVAdapter(context, qrCodeRVModalArrayList);
        qrCodeRV.setLayoutManager(new LinearLayoutManager(context));
        qrCodeRV.setAdapter(qrCodeRVAdapter);

        addQRCodeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, QRScannerActivity.class);
                startActivityForResult(i, 0000);
            }
        });

        btnBackPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        savePaymentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToFirebase(qrCodeRVModalArrayList);
                savePaymentInfo.setVisibility(View.GONE);
            }
        });

        savePaymentInfo.setVisibility(View.GONE);
//        getDataQRCodeDataFromFirebase();

    }

    private void getDataQRCodeDataFromFirebase() {

        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Fetching Data!! Please Wait...");
        progressDialog.show();

        String userId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Payment_Information");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
//                   Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(context, "Some Error Occurred...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                } else {
                    if (task.getResult().getValue() != null) {

//                        AccountData accountData = task.getResult(AccountData.class);
//                        long size = task.getResult().getChildrenCount();
                        for (DataSnapshot dataSnapshot:task.getResult().getChildren()) {

                            qrCodeRVModalArrayList.add(new QRCodeRVModal(dataSnapshot.child("upiId").child("upiName").getValue() + "" ,
                                    dataSnapshot.child("upiId").child("upiId").getValue()+""));
                        }

                    }

                }
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0000) {
            if (resultCode == Activity.RESULT_OK) {
                String upiId = data.getStringExtra("UPI");

                if(upiId.contains("@ok"))
                {
                    qrCodeRVModalArrayList.add(new QRCodeRVModal("Google Pay UPI" , upiId));
                }
                else if(upiId.contains("@paytm"))
                {
                    qrCodeRVModalArrayList.add(new QRCodeRVModal("PayTm UPI" , upiId));
                }
                else if(upiId.contains("@upi"))
                {
                    qrCodeRVModalArrayList.add(new QRCodeRVModal("BHIM UPI" , upiId));
                }
                else
                {
                    qrCodeRVModalArrayList.add(new QRCodeRVModal("Phone Pay UPI" , upiId));
                }
//                for(String upi: qrCodeRVModalArrayList.)
                qrCodeRVAdapter.notifyDataSetChanged();
                savePaymentInfo.setVisibility(View.VISIBLE);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    private void addDataToFirebase(ArrayList<QRCodeRVModal> arrayList)
    {

        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(true);

        Map<String, QRpair> user = new HashMap<>();

        for(int i =0; i<arrayList.size();i++) {

            user.put(arrayList.get(i).getUpiName() , new QRpair(arrayList.get(i).getUpiId() , arrayList.get(i).getUpiName()));
        }
            String userId = mAuth.getCurrentUser().getUid();
            ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Payment_Information");

            ref.setValue(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public static class QRpair{
        public String upiId;
        public String upiName;
        public QRpair() {
        }

        public String getUpiId() {
            return upiId;
        }

        public void setUpiId(String upiId) {
            this.upiId = upiId;
        }

        public String getUpiName() {
            return upiName;
        }

        public void setUpiName(String upiName) {
            this.upiName = upiName;
        }

        public QRpair(String upiId, String upiName) {
            this.upiId = upiId;
            this.upiName = upiName;
        }
    }
}