package com.attendancesystem;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.attendancesystem.Face_Take_Attendance;
import com.attendancesystem.R;
import com.attendancesystem.User_Fragment_checkAttendance;

public class Operator_Home_GridViewAdapter extends BaseAdapter {

    Context context;
    String[] gridviewTitles;
    int[] gridImages;
    int[] gridImageshelp;
    Intent i;

    public Operator_Home_GridViewAdapter(Context context, String[] gridviewTitles, int[] gridImages, int[] gridImageshelp) {
        this.context = context;
        this.gridviewTitles = gridviewTitles;
        this.gridImages = gridImages;
        this.gridImageshelp = gridImageshelp;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

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

        imageView_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position ==0){

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    dialog.setContentView(R.layout.custom_help_dialog);
                    final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                    final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                    title.setText("TAKE ATTENDANCE"+"\n\n"+"This Will Help User to Take Photo as an Attendance. " +
                            "After Clicking on Take Attendance, User have to Put Front Face in CameraView in which " +
                            "Face Coordinates Can be Eaily Detected. For this, User Should be Registered" +
                            " With iAttendance and Initial Registration of User should be completed.");

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                   // Fragment fragment = new Face_Take_Attendance();
                   // FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                   // transaction.replace(R.id.content_frame,fragment).addToBackStack(null);
                   // transaction.commit();


                    Fragment fragment = new Face_Take_Attendance();
                    FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            .addToBackStack(null);
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                }
            }
        });
        return gridViewAndroid;
    }
}
