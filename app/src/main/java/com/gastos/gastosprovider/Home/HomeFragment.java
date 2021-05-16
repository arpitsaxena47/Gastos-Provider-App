package com.gastos.gastosprovider.Home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gastos.gastosprovider.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {
    private RecyclerView recycler;
    private ShopPicAdapter adap;
    private  FirebaseAuth auth;
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
        //New code

        auth = FirebaseAuth.getInstance();

        recycler = view.findViewById(R.id.local_r2);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        FirebaseRecyclerOptions<ShopPic> options =
                new FirebaseRecyclerOptions.Builder<ShopPic>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Merchant_Data/"+auth.getUid()).child("Other"), ShopPic.class)
                        .build();
        //Log.i("jfbvkj", "FirebaseDatabase.getInstance().getReference().child(\"OtherPictures\"), ShopPic.class ");
        adap = new ShopPicAdapter(options);
        recycler.setAdapter(adap);
        recycler.setHasFixedSize(true);
        //end of code

        // For other Data
        database=  FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Merchant_Data/"+auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                shopOwnerDetails info = dataSnapshot.getValue(shopOwnerDetails.class);

               ((TextView)view.findViewById(R.id.shop_name)).setText(info != null ? info.getShopName() : "ShopName");
               ImageView ProfileImage= view.findViewById(R.id.rectangle_1);
             // String link = dataSnapshot.getValue(String.class);
               // String url = dataSnapshot.child("Merchant_Data/"+auth.getUid()).getValue(String.class);
                // loading that data into rImage
              //  Picasso.get().load(url).into(ProfileImage);
                // variable which is ImageView
                Picasso.get().load(info.getProfileImage()).into(ProfileImage);
               // ProfileImage = (String) info.getProfileImage();
              // Picasso..load(url).into(ProfileImage);
                ((TextView)view.findViewById(R.id.payment_ben_value)).setText(info != null ? info.getOwnerName() : "OwnerName");
                ((TextView)view.findViewById(R.id.Address)).setText(info != null ? info.getShopAddress() : "ShopAddress");
                ((TextView)view.findViewById(R.id.cafe)).setText(info != null ? info.getShopCategory() : "ShopCategory");


            }

            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {

            }
        });


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adap.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adap.stopListening();
    }
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