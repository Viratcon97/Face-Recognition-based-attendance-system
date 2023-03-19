package com.attendancesystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attendancesystem.Model.registerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Admin_Home_Fragment_initialAttendance extends Fragment {


    RecyclerView recyclerView;
    AdminInitialAttendanceAdapter adapter;
    List<registerAdapter> registerAdapterList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_home_fragment_initial_attendance, container, false);

        registerAdapterList = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        registerAdapterList.add(new registerAdapter("Virat@gmail.com","Virat Contractor"));
//        registerAdapterList.add(new registerAdapter("Rahul@gmail.com","Rahul Contractor"));

//        adapter = new AdminInitialAttendanceAdapter(getActivity(),registerAdapterList);
//        recyclerView.setAdapter(adapter);

        //Query to display Usertype = "user";


        Query query = FirebaseDatabase.getInstance().getReference("Attendance")
                .child("Register")
                .orderByChild("usertype")
                .equalTo("User");
        query.addListenerForSingleValueEvent(valueEventListener);
        return view;
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            registerAdapterList.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    registerAdapter adapter_ = ds.getValue(registerAdapter.class);
                    registerAdapterList.add(adapter_);
                    adapter = new AdminInitialAttendanceAdapter(getActivity(),registerAdapterList);
                    recyclerView.setAdapter(adapter);
                }
            }
            else {
                Toast.makeText(getActivity(),"Not Exist",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            Toast.makeText(getActivity(),"Error"+databaseError,Toast.LENGTH_LONG).show();
        }
    };


}
