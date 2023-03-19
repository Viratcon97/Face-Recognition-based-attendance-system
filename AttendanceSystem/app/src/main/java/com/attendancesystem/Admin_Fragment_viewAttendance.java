package com.attendancesystem;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.attendancesystem.Model.showAttendance;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Fragment_viewAttendance extends Fragment {

    RecyclerView recyclerView;
    AdminViewAttendanceAdapter adapter;
    List<showAttendance> viewAttendanceAdapterList;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);



        View view = inflater.inflate(R.layout.admin_fragment_view_attendance, container, false);
        ((HomeActivityAdmin) getActivity()).toolbar.setTitle("View Attendance");

        viewAttendanceAdapterList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance").child("Register");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = databaseReference.orderByChild("usertype").equalTo("User");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                viewAttendanceAdapterList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    showAttendance adapter_ = snapshot.getValue(showAttendance.class);
                    viewAttendanceAdapterList.add(adapter_);
                    adapter = new AdminViewAttendanceAdapter(getActivity(), viewAttendanceAdapterList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_admin_view_attendance_filter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_item:
                //Open Filter Dialog
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View alertLayout = inflater.inflate(R.layout.custom_admin_viewattendance_filter, null);
                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.RIGHT;


                final TextView txt_clear = alertLayout.findViewById(R.id.txt_clear);
                final RadioButton rb_name = alertLayout.findViewById(R.id.rb_name);
                final RadioButton rb_present = alertLayout.findViewById(R.id.rb_present);
                Button btn_apply = alertLayout.findViewById(R.id.btn_apply);

                //Clear Filter
                txt_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rb_name.setChecked(false);
                        rb_present.setChecked(false);


                        Query query = databaseReference.orderByChild("usertype").equalTo("User");
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                viewAttendanceAdapterList.clear();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    showAttendance adapter_ = snapshot.getValue(showAttendance.class);
                                    viewAttendanceAdapterList.add(adapter_);
                                    adapter = new AdminViewAttendanceAdapter(getActivity(), viewAttendanceAdapterList);
                                    recyclerView.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        dialog.dismiss();
                    }
                });

                btn_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rb_name.isChecked()){
                            Query query = databaseReference.orderByChild("email");

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    viewAttendanceAdapterList.clear();

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        String Fetch_User = snapshot.child("usertype").getValue().toString();
                                        if(Fetch_User.equals("User")) {

                                            showAttendance adapter_ = snapshot.getValue(showAttendance.class);
                                            viewAttendanceAdapterList.add(adapter_);
                                            adapter = new AdminViewAttendanceAdapter(getActivity(), viewAttendanceAdapterList);
                                            recyclerView.setAdapter(adapter);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            dialog.dismiss();
                        }else if(rb_present.isChecked()){
                            Query query = databaseReference.orderByChild("totalpresent");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    viewAttendanceAdapterList.clear();

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        String Fetch_User = snapshot.child("usertype").getValue().toString();
                                        if(Fetch_User.equals("User")) {

                                            showAttendance adapter_ = snapshot.getValue(showAttendance.class);
                                            viewAttendanceAdapterList.add(adapter_);
                                            adapter = new AdminViewAttendanceAdapter(getActivity(), viewAttendanceAdapterList);
                                            recyclerView.setAdapter(adapter);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            dialog.dismiss();
                        }
                    }
                });
                dialog.setView(alertLayout);
                dialog.show();

                break;
        }
        return false;
    }
}