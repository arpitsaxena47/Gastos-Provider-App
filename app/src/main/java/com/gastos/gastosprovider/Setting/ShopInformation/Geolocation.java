package com.gastos.gastosprovider.Setting.ShopInformation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geolocation {

    private static final String TAG = "GeocodingLocation";
    private static String locationAddress;
    private static Context context;
    private static Handler handler;

    public static void getAddress(final String locationAddress,
                                  final Context context, final Handler handler) {
        //Geolocation.locationAddress = locationAddress;
       // Geolocation.context = context;
       // Geolocation.handler = handler;
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String r1 = null;
                String r2 = null;
                //String result = null;
                try {
                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        StringBuilder sb1 = new StringBuilder();
                        StringBuilder sb2 = new StringBuilder();
                        sb1.append(address.getLatitude());
                        sb2.append(address.getLongitude());
                        r1 = sb1.toString();
                        r2 = sb2.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (r1 != null  && r2!=null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("lati", r1);
                        bundle.putString("logi", r2);
                        message.setData(bundle);
                    }
//                    else {
//                        message.what = 1;
//                        Bundle bundle = new Bundle();
//                        result = "Address: " + locationAddress +
//                                "\n Unable to get Latitude and Longitude for this address location.";
//                        bundle.putString("address", result);
//                        message.setData(bundle);
//                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
