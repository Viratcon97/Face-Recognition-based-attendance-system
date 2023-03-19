package com.attendancesystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.attendancesystem.Model.ImageUploadInfo;
import com.attendancesystem.Model.registerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;


//Recycler.Adapter
//Recycler.ViewHolder
//For Initial Attendance

public class AdminInitialAttendanceAdapter extends RecyclerView.Adapter<AdminInitialAttendanceAdapter.View_initial_attendance_View_adapter> {

    Context context;

    //List Object of Register Adapter Class
    List<registerAdapter> registerAdapterList;

    List<ImageUploadInfo> uploadInfoList;

   /* public AdminInitialAttendanceAdapter(Context context, List<ImageUploadInfo> uploadInfoList) {
        this.context = context;
        this.uploadInfoList = uploadInfoList;
    }*/

    public AdminInitialAttendanceAdapter(Context context, List<registerAdapter> registerAdapterList) {
        this.context = context;
        this.registerAdapterList = registerAdapterList;
    }

    @Override
    public View_initial_attendance_View_adapter onCreateViewHolder(ViewGroup viewGroup, int i) {

        uploadInfoList = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_admin_initial_attendance,null);
        return new View_initial_attendance_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(final View_initial_attendance_View_adapter holder, final int i) {


        holder.progressBar.setVisibility(View.VISIBLE);
        holder.getImage.setVisibility(View.INVISIBLE);

        final registerAdapter adapter = registerAdapterList.get(i);

        //Fetching Image
        Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Register").orderByChild("email")
                .equalTo(adapter.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if(snapshot.child("Image_URL").exists()){

                       Picasso.with(context).load(snapshot.child("Image_URL").getValue().toString())
                               .placeholder(R.drawable.progress_animation)
                                .into(holder.getImage);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.getImage.setVisibility(View.VISIBLE);
                    }else {
                        //Glide.with(context).load(R.drawable.nav_user_icon_64).into(holder.getImage);
                        Picasso.with(context).load(R.drawable.nav_user_icon_64)
                                .placeholder(R.drawable.progress_animation)
                                .into(holder.getImage);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.getImage.setVisibility(View.VISIBLE); }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //End - Fetching Image
        holder.getEmail.setText("Email id: "+adapter.getEmail());
        holder.getUsername.setText("Name: "+adapter.getName());
        holder.getSRNo.setText(""+(i+1));
        holder.getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new Face_Initial_Capture();
                Bundle b = new Bundle();
                b.putString("email",adapter.getEmail());
                fragment.setArguments(b);
                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame,fragment).addToBackStack(null);
                transaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return registerAdapterList.size();
    }

    class View_initial_attendance_View_adapter extends RecyclerView.ViewHolder{

        TextView getEmail,getUsername,getSRNo;
        ImageView getImage;
        ProgressBar progressBar;

        public View_initial_attendance_View_adapter(View itemView) {
            super(itemView);

            getSRNo = itemView.findViewById(R.id.get_SRNo_);
            getImage = itemView.findViewById(R.id.set_frontface_);
            getEmail = itemView.findViewById(R.id.set_email_);
            getUsername = itemView.findViewById(R.id.set_username_);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}
