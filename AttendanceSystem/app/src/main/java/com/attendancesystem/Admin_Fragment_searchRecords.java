package com.attendancesystem;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.attendancesystem.Model.attendanceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_Fragment_searchRecords extends Fragment implements DatePickerDialog.OnDateSetListener{


    EditText editText_Search, editText_Date;
    Button btn_Date, btn_search;
    String Email,Date;
    //Date Time Picker
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int day, month, year;

    List<attendanceAdapter> attendanceAdaptersList;
    RecyclerView recyclerView;
    admin_recycler_adapter_searchRecords adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_search_records, container, false);

        attendanceAdaptersList = new ArrayList<>();;
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);

        editText_Search = view.findViewById(R.id.edittext_search);

        editText_Date = view.findViewById(R.id.edittext_date);
        btn_search = view.findViewById(R.id.btn_search);
        btn_Date = view.findViewById(R.id.button_date);

        btn_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Email = editText_Search.getText().toString();
               Date = editText_Date.getText().toString();

               if(Email.equals("") && Date.equals("")){

                  //Both Are Empty

                   Toast.makeText(getActivity(),"Please Enter Any Email or Select Any Date !",Toast.LENGTH_LONG).show();
               }else if(!Email.equals("") && Date.equals("")){

                   //Email is Filled

                   Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_Data")
                           .orderByChild("email").equalTo(Email);
                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           attendanceAdaptersList.clear();

                           if(dataSnapshot.exists()){
                               for(DataSnapshot ds : dataSnapshot.getChildren()){
                                   attendanceAdapter adapter_ = ds.getValue(attendanceAdapter.class);
                                   attendanceAdaptersList.add(adapter_);
                                   adapter = new admin_recycler_adapter_searchRecords(getActivity(),attendanceAdaptersList);
                                   recyclerView.setAdapter(adapter);
                               }
                           }
                           else {
                               Toast.makeText(getActivity(),"Not Exist",Toast.LENGTH_LONG).show();
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
               }else if(Email.equals("") && !Date.equals("")){

                   //Date is Filled
                   Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_Data")
                           .orderByChild("date").equalTo(Date);
                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           attendanceAdaptersList.clear();

                           if(dataSnapshot.exists()){
                               for(DataSnapshot ds : dataSnapshot.getChildren()){
                                   attendanceAdapter adapter_ = ds.getValue(attendanceAdapter.class);
                                   attendanceAdaptersList.add(adapter_);
                                   adapter = new admin_recycler_adapter_searchRecords(getActivity(),attendanceAdaptersList);
                                   recyclerView.setAdapter(adapter);
                               }
                           }
                           else {
                               Toast.makeText(getActivity(),"Not Exist",Toast.LENGTH_LONG).show();
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

               } else if(!Email.equals("") && !Date.equals("")){

                   //Both are Filled
                    Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_Data")
                            .orderByChild("date").equalTo(Date);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            attendanceAdaptersList.clear();
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    String F_Email = ds.child("email").getValue().toString();
                                    if (F_Email.equals(Email)) {
                                        attendanceAdapter adapter_ = ds.getValue(attendanceAdapter.class);
                                        attendanceAdaptersList.add(adapter_);
                                        adapter = new admin_recycler_adapter_searchRecords(getActivity(), attendanceAdaptersList);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            }
                            else {
                                Toast.makeText(getActivity(),"Not Exist",Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
               }
            }
        });
        return view;
    }
        @Override
        public void onDateSet (DatePicker view,int year, int month, int dayOfMonth){

            month = month + 1;
            if(dayOfMonth < 10 || month < 10){
                String m = "0" + month;
                String d = "0" + dayOfMonth;
                if(month < 10 && dayOfMonth < 10){
                    editText_Date.setText(d + "-" + m + "-" + year);
                }else if(month < 10){
                    editText_Date.setText(dayOfMonth + "-" + m + "-" + year);
                }else if(dayOfMonth < 10){
                    editText_Date.setText(d + "-" + month + "-" + year);
                }
            }
            else {
                editText_Date.setText(dayOfMonth + "-" + month + "-" + year);
            }
        }

}