package com.gastos.gastosprovider.Setting.PaymentInformation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gastos.gastosprovider.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class PaymentInformation extends Fragment {

    private CardView addQRCodeCV;
    private Context context;
    private Activity activity;
    private RecyclerView qrCodeRV;
    private QRCodeRVAdapter qrCodeRVAdapter;
    private ArrayList<QRCodeRVModal> qrCodeRVModalArrayList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userID;

    public PaymentInformation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_payment_information, container, false);
        addQRCodeCV = view.findViewById(R.id.idCVAddCode);
        qrCodeRV = view.findViewById(R.id.idRVQrCodes);
        context = container.getContext();
        activity = getActivity();
        userID = mAuth.getCurrentUser().getUid();
        qrCodeRVModalArrayList = new ArrayList<>();
        qrCodeRVAdapter = new QRCodeRVAdapter(context,qrCodeRVModalArrayList);
        qrCodeRV.setLayoutManager(new LinearLayoutManager(context));
        qrCodeRV.setAdapter(qrCodeRVAdapter);

        addQRCodeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, QRScannerActivity.class);
                startActivity(i);
            }
        });
    getDataQRCodeDataFromFirebase();
    return view;
    }

    private void getDataQRCodeDataFromFirebase() {
        CollectionReference dbQrCodes = db.collection("QRCodes");
        dbQrCodes.document(userID).collection("QRCODES").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    QRCodeRVModal c = d.toObject(QRCodeRVModal.class);
                    qrCodeRVModalArrayList.add(c);
                }
                qrCodeRVAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                Log.e("TAG","SCANNED CODE IS "+intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}