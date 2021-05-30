package com.gastos.gastosprovider.Home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.gastos.gastosprovider.R;
import com.gastos.gastosprovider.Setting.AccountInformation.AccountData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;


public class HomeFragment extends Fragment {
    private RecyclerView recycler;
   // private ShopPicAdapter adap;
    private  FirebaseAuth auth1 ;
    private  FirebaseDatabase database;
    private Context context;
    private String Latitude;
    private  String Longitude;
    String prevShopAddress;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();
        auth1 = FirebaseAuth.getInstance();
        database=  FirebaseDatabase.getInstance();

        // For OwnerName

        DatabaseReference ref = database.getReference("Merchant_data/"+auth1.getUid()).child("Account_Information");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {

                AccountData info = dataSnapshot.getValue(AccountData.class);

                ((TextView)view.findViewById(R.id.payment_ben_value)).setText(info != null ? info.getOwnerName() : "Xxxx");
                //((TextView)view.findViewById(R.id.payment_ben_value)).setText(info.getOwnerName());


            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        });


       //For Shop information
        String userId = auth1.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Shop_Information");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                   Log.e("firebase", "Error getting data", task.getException());
                   // Toast.makeText(context, "Some Error ..." , Toast.LENGTH_SHORT).show();

                }
                else {
                    if(task.getResult().getValue() != null) {

//                        AccountData accountData = task.getResult(AccountData.class);
                        TextView shopName=view.findViewById(R.id.shop_name);
                        TextView shopAddress=view.findViewById(R.id.Address);
                        TextView shopCategory=view.findViewById(R.id.category);
                       String prevShopName = task.getResult().child("ShopName").getValue() != null?task.getResult().child("ShopName").getValue() + "":"";
                         prevShopAddress = task.getResult().child("ShopAddress").getValue() !=null?task.getResult().child("ShopAddress").getValue() + "": "";
                        String prevShopCategory = task.getResult().child("Category").getValue() != null?task.getResult().child("Category").getValue() + "":"";
                         Latitude = task.getResult().child("ShopAddressLatitude").getValue() != null?task.getResult().child("ShopAddressLatitude").getValue() + "":"";
                        Longitude = task.getResult().child("ShopAddressLogitude").getValue() != null?task.getResult().child("ShopAddressLogitude").getValue() + "":"";
//                        if(task.getResult().child("ShopName").getValue() != null)
                        shopName.setText(prevShopName);

//                        if(!task.getResult().child("ShopAddress").getValue().toString().trim().isEmpty())
                        shopAddress.setText(prevShopAddress);
                        shopCategory.setText(prevShopCategory);


                        ImageView shopIVV=view.findViewById(R.id.rectangle_1);
                        if(task.getResult().child("ShopPic").getValue() != null)
                        {
                            Picasso.get().load(task.getResult().child("ShopPic").getValue()+"")
                                    .into(shopIVV);
                            //shopPicUrl = task.getResult().child("ShopPic").getValue()+"";
                            //txtCoverPhoto.setVisibility(View.GONE);
                        }
                        ImageView otherpic1=view.findViewById(R.id.otherpic1);
                        if(task.getResult().child("OtherImages").child("Other1").getValue() != null){

                            Picasso.get().load(task.getResult().child("OtherImages").child("Other1").getValue()+"")
                                    .into(otherpic1);
                           // other1Url = task.getResult().child("OtherImages").child("Other1").getValue()+"";
                           // txtOther1.setVisibility(View.GONE);
                        }
                        ImageView otherpic2=view.findViewById(R.id.otherpic2);
                        if(task.getResult().child("OtherImages").child("Other2").getValue()!= null){

                            Picasso.get().load(task.getResult().child("OtherImages").child("Other2").getValue()+"")
                                    .into(otherpic2);
                            //other2Url = task.getResult().child("OtherImages").child("Other2").getValue()+"";
                          //  txtOther2.setVisibility(View.GONE);
                        }
                        ImageView otherpic3=view.findViewById(R.id.otherpic3);
                        if(task.getResult().child("OtherImages").child("Other3").getValue() != null){
                            Picasso.get().load(task.getResult().child("OtherImages").child("Other3").getValue()+"")
                                    .into(otherpic3);
                          //  other3Url = task.getResult().child("OtherImages").child("Other3").getValue()+"";
                            //txtOther3.setVisibility(View.GONE);
                        }





                    }
                    else {
                      //  Toast.makeText(context, "No Data Found..." , Toast.LENGTH_SHORT).show();
                       // progressDialog.dismiss();
                    }
                }

            }
        });


        ImageView map=view.findViewById(R.id.btnNearMe);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(prevShopAddress));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

                String geoUri = "http://maps.google.com/maps?q=loc:" + Latitude + "," + Longitude ;
                Log.e("TAG", "onClick: "+geoUri );
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });

        return view;
    }

}