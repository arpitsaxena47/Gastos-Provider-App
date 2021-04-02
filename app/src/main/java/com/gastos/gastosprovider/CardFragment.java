package com.gastos.gastosprovider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardFragment extends Fragment {

    private RecyclerView userRV;
    private ArrayList<UserModal> userModalArrayList;
    private EditText serchEdt;
    private UserRVAdapter userRVAdapter;

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        userRV = view.findViewById(R.id.idRVUsers);
        userModalArrayList = new ArrayList<>();
        userModalArrayList.add(new UserModal("Santosh Sehgal", "200", "25 Dec 2020", "https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Asjok Singh", "400", "25 Dec 2020", "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Preeti Sehgal", "500", "25 Dec 2020", "https://images.pexels.com/photos/2625122/pexels-photo-2625122.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Surya Kumar", "900", "25 Dec 2020", "https://images.pexels.com/photos/614810/pexels-photo-614810.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));
        userModalArrayList.add(new UserModal("Santosh Koli", "1000", "25 Dec 2020", "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"));

        userRVAdapter = new UserRVAdapter(userModalArrayList, container.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        userRV.setLayoutManager(manager);
        userRV.setAdapter(userRVAdapter);
        serchEdt = view.findViewById(R.id.idEdtSearch);
        serchEdt.addTextChangedListener(new TextWatcher() {
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

        return view;
    }

    private void filterData(String query) {
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

}