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
public class Admin_Fragment_requestReport extends Fragment {

    RecyclerView recyclerView;
    admin_recycler_adapter_requestreport adapter;
    List<attendancerequestreport> attendancerequestreportslist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.admin_fragment_request_report,container,false);
        ((HomeActivityAdmin)getActivity()).toolbar.setTitle("Attendance Change Request Report");

        attendancerequestreportslist = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Request_Report")
                .orderByChild("email");

        query.addListenerForSingleValueEvent(valueEventListener);
        return view;
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            attendancerequestreportslist.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    attendancerequestreport adapter_ = ds.getValue(attendancerequestreport.class);
                    attendancerequestreportslist.add(adapter_);
                    adapter = new admin_recycler_adapter_requestreport(getContext(),attendancerequestreportslist);
                    recyclerView.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getContext(),"Not Exist",Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            Toast.makeText(getContext(),"Error"+databaseError,Toast.LENGTH_LONG).show();
        }
    };
}
