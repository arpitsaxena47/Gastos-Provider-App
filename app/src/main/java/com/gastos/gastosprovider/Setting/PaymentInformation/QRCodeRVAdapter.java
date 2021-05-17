package com.gastos.gastosprovider.Setting.PaymentInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gastos.gastosprovider.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QRCodeRVAdapter extends RecyclerView.Adapter<QRCodeRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<QRCodeRVModal> qrCodeRVModalArrayList;

    public QRCodeRVAdapter(Context context, ArrayList<QRCodeRVModal> qrCodeRVModalArrayList) {
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
        holder.upiIdTV.setText(modal.getUpiId());
        Picasso.get().load(modal.getQrCodeImg()).into(holder.qrCodeIV);
    }

    @Override
    public int getItemCount() {
        return qrCodeRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView upiIdTV;
        private ImageView qrCodeIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            upiIdTV = itemView.findViewById(R.id.idTVUpiId);
            qrCodeIV = itemView.findViewById(R.id.idIVQrCode);

        }
    }
}
