package com.gastos.gastosprovider.Card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gastos.gastosprovider.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends FirebaseRecyclerAdapter<Post, PostAdapter.PastViewHolder> {


    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, int i, @NonNull Post post) {


        holder.userName.setText(post.getUserName());
        holder.userAmount.setText(post.getUserAmount());
        holder. date.setText(post.getDate());
        Picasso.get().load(post.getImgUrl()).into(holder.imgUrl);
        Picasso.get().load(post.getPayMode()).into(holder.payMode);
    }

    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_rv_item, parent, false);
        return new PastViewHolder(view);
    }

    class PastViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imgUrl ,  payMode;
        private TextView userName , userAmount,  date;

        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUrl = itemView.findViewById(R.id.idCIVImage);
            userName = itemView.findViewById(R.id.idTVUserName);
            userAmount = itemView.findViewById(R.id.idTVAmount);
            date = itemView.findViewById(R.id.idTVDate);
            payMode = itemView.findViewById(R.id.paymentTv);
        }
    }
}

