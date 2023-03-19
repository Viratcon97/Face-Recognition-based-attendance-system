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

public class Admin_Fragment_UserManagement extends Fragment {

    private RecyclerView recyclerView;
    private AdminUserManagementAdapter adapter;
    private ArrayList<registerAdapter> registerAdapterList  = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_user_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdminUserManagementAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setAdapterCallBack(new AdminUserManagementAdapter.CallBack() {
            @Override
            public void onDeleteSuccess() {
                loadData();
            }
        });

        //Initial Loading of Data
        loadData();
    }

    private void loadData(){

        Query query = FirebaseDatabase.getInstance().getReference("Attendance")
                .child("Register")
                .orderByChild("usertype")
                .equalTo("User");


        query.addListenerForSingleValueEvent(valueEventListener);

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            registerAdapterList.clear();

            if(dataSnapshot.exists()) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    registerAdapter adapter_ = ds.getValue(registerAdapter.class);
                    registerAdapterList.add(adapter_);
                }
                adapter.setData(registerAdapterList);
            } else {
                Toast.makeText(getActivity(),"Not Exist",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            Toast.makeText(getActivity(),"Error"+databaseError,Toast.LENGTH_LONG).show();
        }
    };

}
