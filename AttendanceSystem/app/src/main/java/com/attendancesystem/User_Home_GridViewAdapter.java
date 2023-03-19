package com.attendancesystem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class User_Home_GridViewAdapter extends BaseAdapter {

    Context context;
    String[] gridviewTitles;
    int[] gridImages;
    int[] gridImageshelp;
    Intent i;

    public User_Home_GridViewAdapter(Context context, String[] gridviewTitles, int[] gridImages, int[] gridhelp){
        this.context = context;
        this.gridviewTitles = gridviewTitles;
        this.gridImages = gridImages;
        this.gridImageshelp = gridhelp;
    }   

    @Override
    public int getCount() {
        return gridviewTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View gridViewAndroid;

        ((AppCompatActivity)context).getSupportActionBar().setTitle("");

        if (convertView == null) {

            ((AppCompatActivity)context).getSupportActionBar().setTitle("iAttendance");
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridViewAndroid = inflater.inflate(R.layout.layout_common_home_view, null);

        } else {
            ((AppCompatActivity)context).getSupportActionBar().setTitle("iAttendance");
            gridViewAndroid = (View) convertView;
        }


        TextView textView = (TextView) gridViewAndroid.findViewById(R.id.textView);
        ImageView imageView = (ImageView) gridViewAndroid.findViewById(R.id.imageView);
        ImageView imageView_help = (ImageView) gridViewAndroid.findViewById(R.id.image_help);
        textView.setText(gridviewTitles[position]);
        imageView.setImageResource(gridImages[position]);
        imageView_help.setImageResource(gridImageshelp[position]);

        //Help Dialog If Condition Start Here
        imageView_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    dialog.setContentView(R.layout.custom_help_dialog);
                    final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                    final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                    title.setText("CHECK ATTENDANCE"+"\n\n"+"This Will Help User to Check Their Attendace With All " +
                            "Other Information With Date and Time.");

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();


                }
                else if(position==1){

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    dialog.setContentView(R.layout.custom_help_dialog);
                    final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                    final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                    title.setText("SEARCH ATTENDANCE"+"\n\n"+"This Will Help User to Search Attendance. " +
                            "User Can Search Any Particular Date Record and It Will Display Date with Time.");

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(position==2){

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    dialog.setContentView(R.layout.custom_help_dialog);
                    final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                    final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                    title.setText("ATTENDANCE CHANGE REQUEST"+"\n\n"+"This Will Help User to Generate Request " +
                            "To Change Any Attendance Which May Requires Any Change To Update.");

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(position==3){

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    dialog.setContentView(R.layout.custom_help_dialog);
                    final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                    final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                    title.setText("REQUEST STATUS"+"\n\n"+"This Will Help User to Check Request Status. " +
                            "Any Request To User is Either Accepted or Rejected and Status will Generated Here.");

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            }
        });
        //Image Click Event Start Here

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    //Intent i = new Intent();
                    Fragment fragment = new User_Fragment_checkAttendance();
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Check Attendance");
                    i = ((Activity)context).getIntent();
                    String Email = i.getStringExtra("email");
                    Bundle b = new Bundle();
                    b.putString("email",Email);
                    fragment.setArguments(b);
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null);
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }else if(position==1){
                    //Intent i = new Intent();
                    Fragment fragment = new User_Fragment_searchAttendance();
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Search Attendance");
                    i = ((Activity)context).getIntent();
                    String Email = i.getStringExtra("email");
                    Bundle b = new Bundle();
                    b.putString("email",Email);
                    fragment.setArguments(b);
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null);
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }else if(position==2){
                    //Intent i = new Intent();
                    Fragment fragment = new User_Fragment_changeAttendanceRequest();
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Change Attendance Request");
                    i = ((Activity)context).getIntent();
                    String Email = i.getStringExtra("email");
                    Bundle b = new Bundle();
                    b.putString("email",Email);
                    fragment.setArguments(b);
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null);
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }else if(position==3){
                    //Intent i = new Intent();
                    Fragment fragment = new User_Fragment_Request_Status();
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Attendance Change Request Status");
                    i = ((Activity)context).getIntent();
                    String Email = i.getStringExtra("email");
                    Bundle b = new Bundle();
                    b.putString("email",Email);
                    fragment.setArguments(b);
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null);
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }
            }
        });

        //Image Click Event End Here

        return gridViewAndroid;
    }
}
