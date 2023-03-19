package com.attendancesystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;


public class SplashActivity extends AppCompatActivity{

    //Constant Object
    constants constants;
    //Permissions
    int ALL_PERMISSION = 1;
    String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET
    };
    int id; //For Checking User is Regstered or not
    SharedPreferences sharedPreferences;


    //Sleep Time
    int SPLASH_TIME_OUT = 4000; //Waiting Time 4000ms (i.e. 4 Seconds)

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    sharedPreferences = getSharedPreferences("MyPreference",MODE_PRIVATE);
        id = sharedPreferences.getInt("Id",-1);

        //End Shared Preference Declaration


        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                SplashActivity.this, Manifest.permission.SEND_SMS)
                + ContextCompat.checkSelfPermission(
                SplashActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Requires Permission")
                    .setCancelable(false)
                    .setMessage("iAttendance needs SMS and Camera Permissions!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (!hasPermission(SplashActivity.this, permissions)) {
                                ActivityCompat.requestPermissions(SplashActivity.this, permissions, ALL_PERMISSION);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            AlertDialog.Builder nestedBuilder = new AlertDialog.Builder(SplashActivity.this)
                                    .setTitle("Warning")
                                    .setCancelable(false)
                                    .setMessage("iAttendance needs SMS and Camera Permissions to work well without problems!")
                                    .setPositiveButton("Ok, Exit app", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            System.exit(0);
                                        }
                                    })
                                    .setNegativeButton("Permissions", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ActivityCompat.requestPermissions(SplashActivity.this, permissions, ALL_PERMISSION);
                                        }
                                    });
                            nestedBuilder.show();
                        }
                    });

            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), "All Permissions Granted", Toast.LENGTH_LONG).show();

            //Delay for Checking
            new Handler().postDelayed(new Runnable() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void run() {


                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    } else {
                    Toast.makeText(getApplicationContext(),"No Internet Connection ! Please Turn on Internet and Restart Application.",
                            Toast.LENGTH_LONG).show();
                    }

/*
                   if(message.equals("true")){
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    } else {

                        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),0);
                            }
                        });

                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        snackbar.show();

                    }*/

                }
            }, SPLASH_TIME_OUT);

        }

    }


    //Method hasPermission for asking permission in for loop
    private boolean hasPermission(Context context, String[] permissions) {

        if(context != null && permissions != null)
        {
            for(String perm : permissions)
            {
                if(ActivityCompat.checkSelfPermission(context,perm) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }

        return true;
    }

    //Checking Everytime which permission is granted and which is not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == ALL_PERMISSION)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"All Permission Granted",Toast.LENGTH_LONG).show();
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
            }
            else
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,Manifest.permission.CAMERA)
                        && ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,Manifest.permission.SEND_SMS)
                        && ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,Manifest.permission.INTERNET)
                )
                {
                    Toast.makeText(getApplicationContext(),"Permission Denied but not permanent",Toast.LENGTH_LONG).show();
                    System.exit(0);
                }
                else
                {
                    //  Toast.makeText(getApplicationContext(),"Permission Denied Permanently, You can Change this from Settings>Apps",Toast.LENGTH_LONG).show();
                    Snackbar.make(getWindow().getDecorView().getRootView(),"Change From Settings",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Settings", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        }
    }
    @Override
    protected void onPause() {

        super.onPause();
        MyApplication.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
        MyApplication.activityResumed();// On Resume notify the Application
    }


}
