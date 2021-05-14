package com.gastos.gastosprovider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRVAdapter extends RecyclerView.Adapter<UserRVAdapter.ViewHolder> {
     ArrayList<UserModal> userModalArrayList;
     Context context;

    public UserRVAdapter(ArrayList<UserModal> userModalArrayList, Context context) {
        this.userModalArrayList = userModalArrayList;
        this.context = context;
    }

//    public void filter(ArrayList<UserModal> filteredList) {
//        userModalArrayList = filteredList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public UserRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_rv_item, parent, false);
        return new UserRVAdapter.ViewHolder(view);
        //return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRVAdapter.ViewHolder holder, int position) {
//        UserModal modal = userModalArrayList.get(position);
//        Picasso.get().load(modal.getImgUrl()).into(holder.userCIV);
//        holder.userNameTV.setText(modal.getUserName());
//        holder.amountTV.setText(modal.getUserAmount());
//        holder.dateTV.setText(modal.getDate());

        UserModal userModal=userModalArrayList.get(position);

        holder.userName.setText(userModal.getUserName());
        holder.userAmount.setText(userModal.getUserAmount());
        holder. date.setText(userModal.getDate());
        Picasso.get().load(userModal.getImgUrl()).into(holder.imgUrl);
        Picasso.get().load(userModal.getPayMode()).into(holder.payMode);
    }

    @Override
    public int getItemCount() {
        return userModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView  imgUrl ,  payMode;
        private TextView userName , userAmount,  date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUrl = itemView.findViewById(R.id.idCIVImage);
            userName = itemView.findViewById(R.id.idTVUserName);
            userAmount = itemView.findViewById(R.id.idTVAmount);
            date = itemView.findViewById(R.id.idTVDate);
            payMode = itemView.findViewById(R.id.paymentTv);
        }
    }
}
