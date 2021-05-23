package com.gastos.gastosprovider.Setting.PaymentInformation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gastos.gastosprovider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QRCodeRVAdapter extends RecyclerView.Adapter<QRCodeRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QRCodeRVModal> qrCodeRVModalArrayList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference ref;



    public QRCodeRVAdapter(Context context, ArrayList<QRCodeRVModal> qrCodeRVModalArrayList ) {
        this.context = context;
        this.qrCodeRVModalArrayList = qrCodeRVModalArrayList;
    }

    @NonNull
    @Override
    public QRCodeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_code_rv_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRCodeRVAdapter.ViewHolder holder, int position) {
        QRCodeRVModal modal = qrCodeRVModalArrayList.get(position);
        holder.upiName.setText(modal.getUpiName());
        holder.upiId.setText(modal.getUpiId());
        if(position == 0) {
                strokeOfPrimary(holder.itemCardView);
                addToFirebase();
        }

    }

    @Override
    public int getItemCount() {
        return qrCodeRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView upiId;
        private TextView upiName;
        private ImageButton deleteUpiId;
        private MaterialCardView itemCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            upiId = itemView.findViewById(R.id.txtUpiId);
            upiName = itemView.findViewById(R.id.txtUpiName);
            deleteUpiId = itemView.findViewById(R.id.deleteUpi);
            itemCardView = itemView.findViewById(R.id.itemCardView);

            deleteUpiId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   builder.setCancelable(false);
                    builder.setTitle("Delete UPI ID Permanently!");
                    builder.setMessage("Are You Sure To delete this Upi Id Permanently?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        dialog.cancel();
                        deleteFromFirebase(getAdapterPosition());
                        qrCodeRVModalArrayList.remove(getAdapterPosition());
                        notifyDataSetChanged();

                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    builder.show();

                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    QRCodeRVModal modal = qrCodeRVModalArrayList.get(0);
                    qrCodeRVModalArrayList.set( 0, qrCodeRVModalArrayList.get(getAdapterPosition()));
                    qrCodeRVModalArrayList.set(getAdapterPosition() , modal);

                    notifyDataSetChanged();

                    addToFirebase();


                    return true;
                }

            });


        }


        }

    private void strokeOfPrimary( MaterialCardView cardView)
    {
        cardView.setStrokeColor(Color.parseColor("#17e84b"));
        cardView.setStrokeWidth(10);
    }

    private void addToFirebase()
    {
        Map<String, QRCodeRVModal> user = new HashMap<>();
        user.put("primaryUPI",new QRCodeRVModal(qrCodeRVModalArrayList.get(0).getUpiName() , qrCodeRVModalArrayList.get(0).getUpiId()));
        String userId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Payment_Information").child("primaryUPI");
        ref.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Primary UPI changes To = " +qrCodeRVModalArrayList.get(0).getUpiId(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail to Change UPI...", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Primary UPI changes To = " + qrCodeRVModalArrayList.get(0).getUpiId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFromFirebase(int pos )
    {
        String userId = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Payment_Information");

        ref.child("All Upi").child(qrCodeRVModalArrayList.get(pos).getUpiId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "UPI Deleted " , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed To Delete UPI " , Toast.LENGTH_SHORT).show();
            }
        });

        if(pos == 0){
            ref.child("primaryUPI").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Primary UPI Delete  " , Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed To Delete Primary UPI ", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
