package com.gastos.gastosprovider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private CardView accountCV, shopCV, paymentCV;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        accountCV = view.findViewById(R.id.idCVAccount);
        shopCV = view.findViewById(R.id.idCVShop);
        paymentCV = view.findViewById(R.id.idCVPayment);

        accountCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AccountFragment();
                getFragmentManager().beginTransaction().replace(R.id.idFLContainer,fragment).commit();
            }
        });

        shopCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShopInformation();
                getFragmentManager().beginTransaction().replace(R.id.idFLContainer,fragment).commit();

            }
        });

        paymentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PaymentInformation();
                getFragmentManager().beginTransaction().replace(R.id.idFLContainer,fragment).commit();

            }
        });

        return view;
    }
}