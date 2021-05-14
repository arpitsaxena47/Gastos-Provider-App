package com.gastos.gastosprovider;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CardFragment extends Fragment {

    RecyclerView userRV;
    ArrayList<UserModal> userModalArrayList;
     UserRVAdapter userRVAdapter;
    FirebaseDatabase database;
//    FirebaseAuth auth;
    // FirebaseDatabase database;
//   ArrayList listView;
   Context context;
//    //Typeface typeface;
//    ArrayList<UserModal> arrayList = new ArrayList<>();
//    boolean isDetailincomplete=false;
//    ArrayList<UserModal> subarraylist = new ArrayList<>();

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        userRV =view.findViewById(R.id.list_view_payment);
      //  userRV.setHasFixedSize(true);
        userModalArrayList=new ArrayList<>();
        userRVAdapter = new UserRVAdapter(userModalArrayList, container.getContext());
       // userRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
       // userRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

       // userRVAdapter=new UserRVAdapter(userModalArrayList,context);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        userRV.setLayoutManager(manager);
        userRV.setAdapter(userRVAdapter);

       // userRV.setAdapter(userRVAdapter);

        database=  FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Transaction_History_merchant");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
                for (DataSnapshot data :dataSnapshot.getChildren()) {
                    UserModal userModal=data.getValue(UserModal.class);
                    userModalArrayList.add(userModal);
                }
                userRVAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Retrive Data", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled ( @NonNull DatabaseError databaseError ) {
                Toast.makeText(context, "Not able to Retrive", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
//        userRV = view.findViewById(R.id.idRVUsers);
//        userModalArrayList = new ArrayList<>();
//        userModalArrayList.add(new UserModal("Santosh Sehgal", "200", "25 Dec 2020", "https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
//        userModalArrayList.add(new UserModal("Asjok Singh", "400", "25 Dec 2020", "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
//        userModalArrayList.add(new UserModal("Preeti Sehgal", "500", "25 Dec 2020", "https://images.pexels.com/photos/2625122/pexels-photo-2625122.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
//        userModalArrayList.add(new UserModal("Surya Kumar", "900", "25 Dec 2020", "https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
//        userModalArrayList.add(new UserModal("Santosh Koli", "1000", "25 Dec 2020", "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
//
//        userRVAdapter = new UserRVAdapter(userModalArrayList, container.getContext());
//        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        userRV.setLayoutManager(manager);
//        userRV.setAdapter(userRVAdapter);
      //  serchEdt = view.findViewById(R.id.idEdtSearch);
      /*  serchEdt.addTextChangedListener(new TextWatcher() {
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


    }

  /*  private void filterData(String query) {
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