package com.attendancesystem;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attendancesystem.Model.attendancerequestreport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class User_Fragment_Request_Status extends Fragment {

    RecyclerView recyclerView;
    user_recycler_adapter_requeststatus adapter;

    List<attendancerequestreport> requestList;

    String email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_fragment_request_status,container,false);

        email = getArguments().getString("email");

        requestList = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Fetching Pending Request From Attendance Change Request of Given Email
        Query query1 = FirebaseDatabase.getInstance().getReference("Attendance")
                .child("Attendance_change_request").orderByChild("email").equalTo(email);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        attendancerequestreport adapter__ = snapshot.getValue(attendancerequestreport.class);
                        requestList.add(adapter__);
                        adapter = new user_recycler_adapter_requeststatus(getActivity(),requestList);
                        recyclerView.setAdapter(adapter);
                    }
                }
                else {
                    Toast.makeText(getActivity(),"No Pending Request",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Fetching Accepted Request From Request Report
        Query query = FirebaseDatabase.getInstance().getReference("Attendance")
                .child("Request_Report").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        attendancerequestreport adapter_ = ds.getValue(attendancerequestreport.class);
                        requestList.add(adapter_);
                        adapter = new user_recycler_adapter_requeststatus(getActivity(),requestList);
                        recyclerView.setAdapter(adapter);
                    }
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
