package com.gastos.gastosprovider.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.gastos.gastosprovider.R;
import com.squareup.picasso.Picasso;

public class ShopPicAdapter extends FirebaseRecyclerAdapter<ShopPic, ShopPicAdapter.ViewHolder> {


    public ShopPicAdapter(@NonNull FirebaseRecyclerOptions<ShopPic> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopPicAdapter.ViewHolder holder, int position, @NonNull ShopPic model) {

        Picasso.get().load(model.getOtherPic()).into(holder.otherpic);
    }

    @NonNull
    @Override
    public ShopPicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.other_pic, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

       private ImageView otherpic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            otherpic = itemView.findViewById(R.id.other);
        }
    }
}
