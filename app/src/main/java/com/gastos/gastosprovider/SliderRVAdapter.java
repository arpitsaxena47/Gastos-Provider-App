package com.gastos.gastosprovider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderRVAdapter extends RecyclerView.Adapter<SliderRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> imageArrayList;

    public SliderRVAdapter(Context context, ArrayList<String> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
    }

    @NonNull
    @Override
    public SliderRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderRVAdapter.ViewHolder holder, int position) {
        Picasso.get().load(imageArrayList.get(position)).into(holder.backIV);
    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private ImageView backIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            backIV = itemView.findViewById(R.id.idIVImage);
        }
    }
}
