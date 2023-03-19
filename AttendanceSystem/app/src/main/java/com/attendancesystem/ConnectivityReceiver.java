package com.attendancesystem;

/**
 * Created by Lincoln on 18/03/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConnectivityReceiver extends BroadcastReceiver {


    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            boolean isVisible = MyApplication.isActivityVisible();// Check if

            Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

            // If it is visible then trigger the task else do nothing
            if (isVisible == true) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();

                // Check internet connection and accrding to state change the
                // text of activity by calling method
                if (networkInfo != null && networkInfo.isConnected()) {

               //     new SplashActivity().changeTextStatus(true);
                } else {
                //    new SplashActivity().changeTextStatus(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}