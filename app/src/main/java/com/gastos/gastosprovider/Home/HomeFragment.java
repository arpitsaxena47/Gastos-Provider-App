package com.gastos.gastosprovider.Home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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


public class HomeFragment extends Fragment {
    private RecyclerView recycler;
   // private ShopPicAdapter adap;
    private  FirebaseAuth auth1;
    private  FirebaseDatabase database;
    private Context context;
//    private RecyclerView imageRV;
//    private ArrayList<String> imageURls;
//    private RecyclerView usersRV;
//  //  private ArrayList<UserModal> userModalArrayList;
//    private EditText searchEdt;
 //   private UserRVAdapter userRVAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
      /*  imageRV = view.findViewById(R.id.idRVImages);
        searchEdt = view.findViewById(R.id.idEdtSearch);
        usersRV = view.findViewById(R.id.idRvUsers);
        userModalArrayList = new ArrayList<>();
        imageURls = new ArrayList<>();
        imageURls.add("https://images.unsplash.com/photo-1612831457091-ffd909920941?ixid=MXwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwxfHx8ZW58MHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        imageURls.add("https://images.unsplash.com/photo-1593642634315-48f5414c3ad9?ixid=MXwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHw2fHx8ZW58MHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        imageURls.add("https://images.unsplash.com/photo-1611095560192-ccc932f617e1?ixid=MXwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwxNnx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");

        userModalArrayList.add(new UserModal("Santosh Sehgal", "200", "25 Dec 2020", "https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Asjok Singh", "400", "25 Dec 2020", "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Preeti Sehgal", "500", "25 Dec 2020", "https://images.pexels.com/photos/2625122/pexels-photo-2625122.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Surya Kumar", "900", "25 Dec 2020", "https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Santosh Koli", "1000", "25 Dec 2020", "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));

        userRVAdapter = new UserRVAdapter(userModalArrayList, container.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        usersRV.setLayoutManager(manager);
        usersRV.setAdapter(userRVAdapter);


        SliderRVAdapter adapter = new SliderRVAdapter(container.getContext(), imageURls);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        imageRV.setLayoutManager(linearLayoutManager);
        imageRV.setAdapter(adapter);

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });

       */
        //For other Images
        auth1 = FirebaseAuth.getInstance();
        database=  FirebaseDatabase.getInstance();
//        recycler = view.findViewById(R.id.local_r2);
//        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        FirebaseRecyclerOptions<ShopPic> options =
//                new FirebaseRecyclerOptions.Builder<ShopPic>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference("Merchant_data/"+auth1.getUid()).child("Shop_Information").child("OtherImages"), ShopPic.class)
//                        .build();
//        //Log.i("jfbvkj", "FirebaseDatabase.getInstance().getReference().child(\"OtherPictures\"), ShopPic.class ");
//        adap = new ShopPicAdapter(options);
//        recycler.setAdapter(adap);
//        recycler.setHasFixedSize(true);
        //end of code

        //For othershopimages
//        database=  FirebaseDatabase.getInstance();
//        DatabaseReference ref3 = database.getReference("Merchant_data/"+auth1.getUid()).child("Shop_Information").child("OtherImages");
//        ref3.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
//                // shopOwnerDetails info = dataSnapshot.getValue(shopOwnerDetails.class);
//               // ShopDetails info = dataSnapshot.getValue(ShopDetails.class);
//                //((TextView)view.findViewById(R.id.shop_name)).setText(info != null ? info.getShopName() : "ShopName");
//                // String link = dataSnapshot.getValue(String.class);
//                // loading that data into rImage
////                ImageView OtherImage1= view.findViewById(R.id.otherpic1);
////                String url1 = dataSnapshot.child("Merchant_Data/"+auth1.getUid()).child("Shop_Information").child("OtherImages").getValue(String.class);
////                Picasso.get().load(url1).into(OtherImage1);
////
////                ImageView OtherImage2= view.findViewById(R.id.otherpic2);
////                String url2 = dataSnapshot.child("Merchant_Data/"+auth1.getUid()).child("Shop_Information").child("OtherImages").getValue(String.class);
////                Picasso.get().load(url2).into(OtherImage2);
////
////                ImageView OtherImage3= view.findViewById(R.id.otherpic3);
////                String url3 = dataSnapshot.child("Merchant_Data/"+auth1.getUid()).child("Shop_Information").child("OtherImages").getValue(String.class);
////                Picasso.get().load(url3).into(OtherImage3);
//                // variable which is ImageView
//                // Picasso.get().load(info.getProfileImage()).into(ProfileImage);
//                // ProfileImage = (String) info.getProfileImage();
//                // Picasso..load(url).into(ProfileImage);
//                //((TextView)view.findViewById(R.id.payment_ben_value)).setText(info != null ? info.getOwnerName() : "OwnerName");
//              //  ((TextView)view.findViewById(R.id.Address)).setText(info != null ? info.getShopAddress() : "ShopAddress");
//                //  ((TextView)view.findViewById(R.id.cafe)).setText(info != null ? info.getShopCategory() : "ShopCategory");
//
//
//            }
//
//            @Override
//            public void onCancelled ( @NonNull DatabaseError databaseError ) {
//
//            }
//        });
//

        // For OwnerName

        DatabaseReference ref = database.getReference("Merchant_data/"+auth1.getUid()).child("Account_Information");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
               // shopOwnerDetails info = dataSnapshot.getValue(shopOwnerDetails.class);
                AccountData info = dataSnapshot.getValue(AccountData.class);
             //  ((TextView)view.findViewById(R.id.shop_name)).setText(info != null ? info.getShopName() : "ShopName");
             //  ImageView ProfileImage= view.findViewById(R.id.rectangle_1);
             // String link = dataSnapshot.getValue(String.class);
               // String url = dataSnapshot.child("Merchant_Data/"+auth.getUid()).getValue(String.class);
                // loading that data into rImage
              //  Picasso.get().load(url).into(ProfileImage);
                // variable which is ImageView
               // Picasso.get().load(info.getProfileImage()).into(ProfileImage);
               // ProfileImage = (String) info.getProfileImage();
              // Picasso..load(url).into(ProfileImage);
                ((TextView)view.findViewById(R.id.payment_ben_value)).setText(info != null ? info.getOwnerName() : "OwnerName");
               // ((TextView)view.findViewById(R.id.Address)).setText(info != null ? info.getShopAddress() : "ShopAddress");
              //  ((TextView)view.findViewById(R.id.cafe)).setText(info != null ? info.getShopCategory() : "ShopCategory");


            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        });

         // For Shop Details
      /*  DatabaseReference ref2 = database.getReference("Merchant_data/"+auth1.getUid()).child("Shop_Information");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
               // shopOwnerDetails info = dataSnapshot.getValue(shopOwnerDetails.class);
                ShopDetails info = dataSnapshot.getValue(ShopDetails.class);
                  ((TextView)view.findViewById(R.id.shop_name)).setText(info != null ? info.getShopName() : "ShopName");
                ((TextView)view.findViewById(R.id.Address)).setText(info != null ? info.getShopAddress() : "ShopAddress");


                if(info!=null)
                {
                    ImageView ProfileImage= view.findViewById(R.id.rectangle_1);
                    Picasso.get().load(info.getShopPic()).into(ProfileImage);

                }
                if(info!=null)
                {
                    ImageView otherpic1= view.findViewById(R.id.otherpic1);
                    Picasso.get().load(info.getOtherPic1()).into(otherpic1);


                }
                if(info!=null)
                {
                    ImageView otherpic2= view.findViewById(R.id.otherpic2);
                    Picasso.get().load(info.getOtherPic2()).into(otherpic2);

                }

                if(info!=null)
                {
                    ImageView otherpic3= view.findViewById(R.id.otherpic3);
                    Picasso.get().load(info.getOtherPic3()).into(otherpic3);

                }
                // String link = dataSnapshot.getValue(String.class);
                 //String url = dataSnapshot.child("Merchant_data/"+auth1.getUid()).child("Shop_Information").getValue(String.class);
                // loading that data into rImage
                 // Picasso.get().load(url).into(ProfileImage);
                // variable which is ImageView

                // ProfileImage = (String) info.getProfileImage();
                // Picasso..load(url).into(ProfileImage);
                //((TextView)view.findViewById(R.id.payment_ben_value)).setText(info != null ? info.getOwnerName() : "OwnerName");

                //  ((TextView)view.findViewById(R.id.cafe)).setText(info != null ? info.getShopCategory() : "ShopCategory");


            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        });

       */

        String userId = auth1.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Merchant_data/" + userId).child("Shop_Information");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
//                   Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(context, "Some Error ..." , Toast.LENGTH_SHORT).show();

                }
                else {
                    if(task.getResult().getValue() != null) {

//                        AccountData accountData = task.getResult(AccountData.class);
                        TextView shopName=view.findViewById(R.id.shop_name);
                        TextView shopAddress=view.findViewById(R.id.Address);
                        TextView shopCategory=view.findViewById(R.id.category);
                       String prevShopName = task.getResult().child("ShopName").getValue() != null?task.getResult().child("ShopName").getValue() + "":"";
                        String prevShopAddress = task.getResult().child("ShopAddress").getValue() !=null?task.getResult().child("ShopAddress").getValue() + "": "";
                        String prevShopCategory = task.getResult().child("Category").getValue() != null?task.getResult().child("Category").getValue() + "":"";
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
                        Toast.makeText(context, "No Data Found..." , Toast.LENGTH_SHORT).show();
                       // progressDialog.dismiss();
                    }
                }

            }
        });


        ImageView map=view.findViewById(R.id.btnNearMe);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        adap.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adap.stopListening();
 //   }
   /* private void filterData(String query) {
    ArrayList<UserModal> filteredList = new ArrayList<>();
    for(UserModal modal : userModalArrayList){
        if(modal.getUserName().toLowerCase().contains(query.toLowerCase())){
            filteredList.add(modal);
        }
    }
    if(filteredList.isEmpty()){
        Toast.makeText(getContext(), "No data found..", Toast.LENGTH_SHORT).show();
    }else{
        userRVAdapter.filter(filteredList);
    }
    }

    */
}