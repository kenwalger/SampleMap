package com.zyzzyxtech.maplocation.LocationLookup;

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

/**
 * Created by Ken on 2/14/2015.
 */
public class LocationAddress {
    
    private static final String TAG = LocationAddress.class.getSimpleName();
    
    public static void getAddressFromLocation(final double latitude,
                                              final double longitude,
                                              final Context context,
                                              final Handler handler) {
        
        Thread thread = new Thread() {
            @Override
        public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        
                        // Display typical mailing address: Street, City, State, Postal Code
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                       //sb.append(address.getLocality()).append("\n");         // Displays city name separately
                       //sb.append(address.getPostalCode()).append("\n");       // Displays postal code separately
                        sb.append(address.getCountryName());                    // Displays country name
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude
                                + "\nLongitude: " + longitude +
                                "\n\nAddress: \n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude 
                                + "\nLongitude: " + longitude +
                                "\n\nAddress: \n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
