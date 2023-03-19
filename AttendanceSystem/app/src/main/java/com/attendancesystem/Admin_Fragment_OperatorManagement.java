package com.attendancesystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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

import static com.attendancesystem.checkInternet.IS_NETWORK_AVAILABLE;

public class Admin_Fragment_OperatorManagement extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<registerAdapter> registerAdapterList = new ArrayList<>();
    private AdminOperatorManagementAdapter adapter;

    /*  BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String s = intent.getStringExtra("status");
            Log.i("DATA",s);
            Toast.makeText(getActivity(),""+s,Toast.LENGTH_LONG).show();
        }
    };
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.admin_fragment_operator_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Receiving Intent
        // LocalBroadcastManager.getInstance(getActivity()).registerReceiver(dataReceiver, new IntentFilter("Status"));


        IntentFilter intentFilter = new IntentFilter(checkInternet.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                Snackbar.make(getView(), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
            }
        }, intentFilter);


       // registerAdapterList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdminOperatorManagementAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setAdapterCallBack(new AdminOperatorManagementAdapter.CallBack() {
            @Override
            public void onDeleteSuccess() {
                //Load data after deletion of any entry
                loadData();
            }
        });

        //Load data first time
        loadData();
    }

    private void loadData() {
        Query query = FirebaseDatabase.getInstance().getReference("Attendance")
                .child("Register")
                .orderByChild("usertype")
                .equalTo("Operator");
        //           .child("usertype");
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            registerAdapterList.clear();
            if (dataSnapshot.exists()) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    registerAdapter adapter_ = ds.getValue(registerAdapter.class);
                    registerAdapterList.add(adapter_);
                }
                adapter.setData(registerAdapterList);
            } else {
                Toast.makeText(getActivity(), "Not Exist", Toast.LENGTH_LONG).show();
            }

        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getActivity(), "Error" + databaseError, Toast.LENGTH_LONG).show();
        }
    };
}