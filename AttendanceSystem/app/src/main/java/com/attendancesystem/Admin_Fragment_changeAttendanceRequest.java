package com.attendancesystem;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attendancesystem.Model.changeAttendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Fragment_changeAttendanceRequest extends Fragment {

    private RecyclerView recyclerView;
    private AdminChangeAttendanceRequestAdapter adapter;
    private ArrayList<changeAttendance> changeAttendanceList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_fragment_change_attendance_request,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdminChangeAttendanceRequestAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setAdapterCallBack(new AdminChangeAttendanceRequestAdapter.CallBack() {
            @Override
            public void onDeleteSuccess() {
                //Load data after deletion of any entry
                loadData();
            }
        });
        loadData();
    }

    private void loadData(){
        Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_change_request")
                .orderByChild("email");

        query.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            changeAttendanceList.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    changeAttendance adapter_ = ds.getValue(changeAttendance.class);
                    changeAttendanceList.add(adapter_);
                }
                adapter.setData(changeAttendanceList);
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
