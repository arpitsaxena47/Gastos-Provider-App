package com.gastos.gastosprovider.Card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gastos.gastosprovider.R;
import com.google.firebase.database.FirebaseDatabase;

public class CardFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter adapter;
//    RecyclerView userRV;
//    ArrayList<UserModal>  userModalArrayList;
//     UserRVAdapter userRVAdapter;
//    FirebaseDatabase database;
//    FirebaseAuth auth;
    // FirebaseDatabase database;
//   ArrayList listView;
   //Context context;
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
//         database = FirebaseDatabase.getInstance();
//        userModalArrayList=new ArrayList<>();
//        DatabaseReference ref = database.getReference("Transaction_History_merchant");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange ( @NonNull DataSnapshot dataSnapshot ) {
//                for (DataSnapshot data :dataSnapshot.getChildren()) {
//                   // UserModal userModal=data.getValue(UserModal.class);
//                   // userModalArrayList.add(userModal);
//
//                 //   entries.add(ds.getValue(LogEntry.class));
//                    userModalArrayList.add(data.getValue(UserModal.class));
//                }
//                userRVAdapter.notifyDataSetChanged();
////                Toast.makeText(context, "Retrive Data", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onCancelled ( @NonNull DatabaseError databaseError ) {
//                //        Toast.makeText(context, "Not able to Retrive", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//
        //New code
        recyclerView = view.findViewById(R.id.list_view_payment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Transaction_History_Merchant"), Post.class)
                        .build();

        adapter = new PostAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        //end of code

//
//        userRV =view.findViewById(R.id.list_view_payment);
//        database=  FirebaseDatabase.getInstance();
//        userRVAdapter = new UserRVAdapter(userModalArrayList);
//        userRV.setHasFixedSize(true);
//
//    //   userRVAdapter = new UserRVAdapter(userModalArrayList, container.getContext());
//       // userRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//
//       // userRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
//
//       // userRVAdapter=new UserRVAdapter(userModalArrayList,context);
//
//       // LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//       // userRV.setLayoutManager(manager);
//        //userRV.setAdapter(userRVAdapter);
//       // userRV.setLayoutManager(new LinearLayoutManager(container.getContext()));
//        userRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        userRV.setAdapter(userRVAdapter);
//       // userRV.setAdapter(userRVAdapter);



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
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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