package com.attendancesystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.attendancesystem.Model.attendanceAdapter;
import com.attendancesystem.Model.registerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class User_Fragment_checkAttendance extends Fragment {

    RecyclerView recyclerView;
    user_recycler_adapter_check_attendance adapter;
    List<attendanceAdapter> registerAdapterList;
    String email;
    FirebaseDatabase database;
    //To Set Total Present
    TextView set_total_present;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_fragment_checkattendance, container, false);

        registerAdapterList = new ArrayList<>();
        email = getArguments().getString("email");

        set_total_present = (TextView) view.findViewById(R.id.set_total_present);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (email.equals("")) {

        } else {
            database = FirebaseDatabase.getInstance();
            Query query1 = FirebaseDatabase.getInstance().getReference("Attendance").child("Register")
                    .orderByChild("email").equalTo(email);

            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            set_total_present.setText(snapshot.child("totalpresent").getValue().toString());
                        }
                    } else {
                        Toast.makeText(getActivity(), "No Record", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_Data")
                    .orderByChild("email").equalTo(email);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    registerAdapterList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        attendanceAdapter adapter_ = snapshot.getValue(attendanceAdapter.class);
                        registerAdapterList.add(adapter_);
                        adapter = new user_recycler_adapter_check_attendance(getActivity(), registerAdapterList);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("TAG", databaseError.toString());

                }
            });
        }
            return view;
        }
}


