package com.gastos.gastosprovider.Setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.gastos.gastosprovider.DynamicWebview;
import com.gastos.gastosprovider.Setting.PaymentInformation.PaymentInformation;
import com.gastos.gastosprovider.PhoneNumberActivity;
import com.gastos.gastosprovider.R;
import com.gastos.gastosprovider.Setting.AccountInformation.AccountInformation;
import com.gastos.gastosprovider.Setting.ShopInformation.ShopInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFragment extends Fragment {

    private ImageView accountCV, shopCV, paymentCV;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public Context context;
    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        context = container.getContext();

        accountCV = view.findViewById(R.id.idbtn1);
        shopCV = view.findViewById(R.id.idbtn2);
        paymentCV = view.findViewById(R.id.idbtn3);

        accountCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment fragment = new AccountFragment();
//                getFragmentManager().beginTransaction().replace(R.id.idFLContainer,fragment).commit();

                Intent accountIntent = new Intent(context , AccountInformation.class);
                startActivity(accountIntent);
            }
        });

        shopCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment fragment = new ShopInformation();
//                getFragmentManager().beginTransaction().replace(R.id.idFLContainer,fragment).commit();
                Intent shopIntent = new Intent(context , ShopInformation.class);
                startActivity(shopIntent);

            }
        });

        paymentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(context , PaymentInformation.class);
                startActivity(paymentIntent);

            }
        });

//        view.findViewById(R.id.rateus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                }
//            }
//        });

        view.findViewById(R.id.Contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context, DynamicWebview.class );
                intent1.putExtra("Mode","Contact_us");
                startActivity(intent1);
            }
        });

        view.findViewById(R.id.legal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context,DynamicWebview.class );
                intent1.putExtra("Mode","Legal_Provider");
                startActivity(intent1);
            }
        });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                firebaseAuth = FirebaseAuth.getInstance();

//                                callbackManager = CallbackManager.Factory.create();
//                                mGoogleSignInClient = GoogleSignIn.getClient(Profile_Activity.this,  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                                        .requestIdToken(getString(R.string.default_web_client_id)).build());
//                                mGoogleSignInClient.signOut();
                                firebaseAuth.signOut();
                               // LoginManager.getInstance().logOut();
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                //if user is signed in, we call a helper method to save the user details to Firebase
                                if (user == null) {
                                    // User is signed in
                                    // you could place other firebase code
                                    //logic to save the user details to Firebase
                                    Intent intent = new Intent(context, PhoneNumberActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });



                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Logout?");
                alert.show();

            }
        });


        return view;
    }
}