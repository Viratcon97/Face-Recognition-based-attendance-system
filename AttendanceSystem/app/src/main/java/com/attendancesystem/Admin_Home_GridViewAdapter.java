package com.attendancesystem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Admin_Home_GridViewAdapter extends BaseAdapter {

    Context context;
    String[] gridviewTitles;
    int[] gridImages;
    int[] gridImageshelp;

    public Admin_Home_GridViewAdapter(Context context, String[] gridviewTitles, int[] gridImages, int[] gridhelp){
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
            ((AppCompatActivity)context).getSupportActionBar().setTitle("iAttendance (Admin)");

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridViewAndroid = inflater.inflate(R.layout.layout_common_home_view, null);

        } else {
            ((AppCompatActivity)context).getSupportActionBar().setTitle("iAttendance (Admin)");
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
                    title.setText("INITIAL RECORDS"+"\n\n"+"This Will Help Admin to Enter User's Initial Face in Databsae. " +
                            "User have to Enter Front Facing photo in which Face Coordinates Can be Eaily Detected. User Should be Registered" +
                            " With iAttendance.");

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
                    title.setText("GENERATE ATTENDANCE"+"\n\n"+"This Will Help Admin to Generate All User's Attendance Records in Databsae." +
                            "All Details related to Attendance Can be generated from Here.");

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
                    title.setText("SEARCH RECORDS"+"\n\n"+"This Will Help Admin to Search User's Attendance Records in Databsae." +
                            "Admin Can Search Particular User's Attendance from Here.");

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
                    title.setText("PENDING REQUESTS"+"\n\n"+"This Will Help Admin to Accept or Reject User's Request." +
                            " Admin Can Also See All The Information About Request and Accordingly " +
                            " Accept or Reject It.");

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(position==4){

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    dialog.setContentView(R.layout.custom_help_dialog);
                    final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                    final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                    title.setText("ATTENDANCE REQUESTS REPORT"+"\n\n"+"This Will Help Admin to See All The" +
                            " Accepted and Rejected Requests of Different Users.");

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
                    Fragment fragment = new Admin_Home_Fragment_initialAttendance();
                    //You need to add the following line for this solution to work; thanks skayred
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Initial Attendance");
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null);
                    transaction.replace(R.id.content_frame,fragment).addToBackStack("Tag");
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }
                else if(position==1){
                    Fragment fragment = new Admin_Fragment_viewAttendance();
                    //You need to add the following line for this solution to work; thanks skayred
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("View Attendance");
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            ;
                    transaction.replace(R.id.content_frame,fragment).addToBackStack("Tag");
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }else if(position==2){
                    Fragment fragment = new Admin_Fragment_searchRecords();
                    //You need to add the following line for this solution to work; thanks skayred
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Search Records");
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame,fragment).addToBackStack("Tag");
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }
                else if(position==3){
                    Fragment fragment = new Admin_Fragment_changeAttendanceRequest();
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Attendance Change Request");
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame,fragment).addToBackStack("Tag");
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }
                else if(position==4){
                    Fragment fragment = new Admin_Fragment_requestReport();
                    ((AppCompatActivity)context).getSupportActionBar().setTitle("Change Request Report");
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame,fragment).addToBackStack("Tag");
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }
            }
        });

        //Image Click Event End Here

        return gridViewAndroid;
    }
}
