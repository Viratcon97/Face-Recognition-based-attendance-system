package com.attendancesystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class User_Fragment_changeAttendanceRequest extends DialogFragment implements AdapterView.OnItemClickListener,DatePickerDialog.OnDateSetListener
    ,TimePickerDialog.OnTimeSetListener
{

    String reason;

    Button request_btn;
    //Get Email
    String email;
    Button btn_Date,btn_Time;
    EditText txt_time;
    EditText txt_date;
    //Date Picker
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int day,month,year;
    int hour,minute;
    String am_pm;
    String result;
    boolean status = false;
    //Database
    Map<String,String> Attendance_change_data;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_change_attendance_request,container,false);

        email = getArguments().getString("email");

        txt_date = (EditText)view.findViewById(R.id.edittext_date);
        txt_time = (EditText)view.findViewById(R.id.edittext_time);

        btn_Date = (Button)view.findViewById(R.id.button_date);
        btn_Time = (Button)view.findViewById(R.id.button_time);
        request_btn = (Button)view.findViewById(R.id.btn_request);

        //
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
        Attendance_change_data = new HashMap<>();
        //Date Picker
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        datePickerDialog = new DatePickerDialog(getActivity(),R.style.DialogTheme ,this, year, month, day);
        timePickerDialog = new TimePickerDialog(getActivity(),R.style.DialogTheme,this,hour,minute, DateFormat.is24HourFormat(getActivity()));

        //Binding Spinner
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,
                constants.reasons);

        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                view.findViewById(R.id.spinner_reason);
        materialDesignSpinner.setAdapter(arrayAdapter);
        materialDesignSpinner.setOnItemClickListener(this);
        //Binding Spinner End

        btn_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        btn_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(materialDesignSpinner.getText().toString().equals("Missed Entry")){
                    reason = "Missed Entry";
                }else if(materialDesignSpinner.getText().toString().equals("Registered, But Face Not Recognized")){
                    reason = "Registered, But Face Not Recognized";
                }else if(materialDesignSpinner.getText().toString().equals("Face Recognized But Showing Absent")){
                    reason = "Face Recognized But Showing Absent";
                }else if(materialDesignSpinner.getText().toString().equals("Other Reason")){
                    reason = "Other Reason";
                }else{
                    reason = "";
                }

                if(txt_date.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please Select Any Date !",Toast.LENGTH_LONG).show();
                }else if(txt_time.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please Select Time !",Toast.LENGTH_LONG).show();
                }else if(reason.equals("")){
                    materialDesignSpinner.setError("Please Select Any Reason");
                    materialDesignSpinner.requestFocus();
                }else {

                    Log.i("DATA",""+email);
                    //Check if User have data for selected Date
                    Query query_ = databaseReference.child("Attendance_Data").orderByChild("email").equalTo(email);
                    query_.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.i("DATA","Inside Query");

                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                String Fetch_Date = snapshot.child("date").getValue().toString();
                                String Fetch_Mail = snapshot.child("email").getValue().toString();

                                Log.i("DATA",""+Fetch_Date);
                                Log.i("DATA",""+txt_date.getText().toString());
                                Log.i("DATA",""+Fetch_Mail);
                                Log.i("DATA",""+email);

                                if(Fetch_Date.equals(txt_date.getText().toString()) && Fetch_Mail.equals(email)) {
                                 //   Toast.makeText(getActivity(),"Data is Already Available for Selected Date !",Toast.LENGTH_LONG).show();

                                    status = false;
                                   break;
                                }else {
                                   status = true;
                                continue;
                                }
                            }

                            Log.i("DATA",""+status);
                            CheckAvailability(status);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    //End - Check if User have data for selected Date
                /*
                    Attendance_change_data.put("email",email);
                    Attendance_change_data.put("date",txt_date.getText().toString());
                    Attendance_change_data.put("time",txt_time.getText().toString());
                    Attendance_change_data.put("reason",reason);
                    Attendance_change_data.put("status","Pending");
                    databaseReference.child("Attendance_change_request").push().setValue(Attendance_change_data);
                    Toast.makeText(getActivity(),"Request Submitted !",Toast.LENGTH_LONG).show();

                    Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Register")
                            .orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                                String mobile_no = snapshot.child("mobile").getValue().toString();
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(mobile_no, null,"Attendance Change Requset is Successfully Generated for Email" +
                                        " id: "+email+" For The Date "+txt_date.getText().toString()+".", null, null);
                                Toast.makeText(getActivity(), "SMS Sent.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "SMS Not Sent.",
                                    Toast.LENGTH_LONG).show();

                        }
                    });
                    */
                }
            }
        });
        return view;
    }


    private void CheckAvailability(boolean status) {

        //Compare Date with Future Date

        if(status == true){
            Date Fetch_date = new Date();
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");

            String new_date = txt_date.getText().toString(); // Fetched Date

            try {
                Fetch_date = dt.parse(new_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (new Date().before(Fetch_date)) {
                Toast.makeText(getActivity(),"User Can Not Generate request for Future Date",Toast.LENGTH_LONG).show();
            } else {
                Attendance_change_data.put("email",email);
                Attendance_change_data.put("date",txt_date.getText().toString());
                Attendance_change_data.put("time",txt_time.getText().toString());
                Attendance_change_data.put("reason",reason);
                Attendance_change_data.put("status","Pending");
                databaseReference.child("Attendance_change_request").push().setValue(Attendance_change_data);
                Toast.makeText(getActivity(),"Request Submitted !",Toast.LENGTH_LONG).show();


                Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Register")
                        .orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String mobile_no ;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            mobile_no  = snapshot.child("mobile").getValue().toString();

                            sendSMS(mobile_no);
                            break;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "SMS Not Sent.",
                                Toast.LENGTH_LONG).show();

                    }
                });

            }
        }else {
            Toast.makeText(getActivity(),"Data is Already Available for Selected Date !",Toast.LENGTH_LONG).show();
        }


        //Compare Date With Future Date
    }

    private void sendSMS(String mobile_no) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobile_no, null,"Attendance Change Requset is Successfully Generated for Email" +
                " id: "+email+" For The Date "+txt_date.getText().toString()+".", null, null);
        Toast.makeText(getActivity(), "SMS Sent.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        reason = constants.reasons[position];
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        txt_date = (EditText) getActivity().findViewById(R.id.edittext_date);


        month = month + 1;
/*
        if (dayOfMonth < 10) {
            String d = "0" + dayOfMonth;
            txt_date.setText(d + "-" + month + "-" + year);
        } else if(month < 10){
            String m = "0" + month;
            txt_date.setText(dayOfMonth + "-" + m + "-" + year);
        } else*/
        if(dayOfMonth < 10 || month < 10){
            String m = "0" + month;
            String d = "0" + dayOfMonth;
            if(month < 10 && dayOfMonth < 10){
                txt_date.setText(d + "-" + m + "-" + year);
            }else if(month < 10){
                txt_date.setText(dayOfMonth + "-" + m + "-" + year);
            }else if(dayOfMonth < 10){
                txt_date.setText(d + "-" + month + "-" + year);
            }
        }
        else {
            txt_date.setText(dayOfMonth + "-" + month + "-" + year);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        txt_time = (EditText) getActivity().findViewById(R.id.edittext_time);

        if (minute < 10) {

            String min = "0"+minute;

            if (hourOfDay > 12) {
                hourOfDay -= 12;
                am_pm = "PM";
            } else if (hourOfDay == 0) {
                hourOfDay += 12;
                am_pm = "AM";
            } else if (hourOfDay == 12)
                am_pm = "PM";
            else
                am_pm = "AM";

            txt_time.setText(hourOfDay + ":" + min + " " + am_pm);
        }else {

            if (hourOfDay > 12) {
                hourOfDay -= 12;
                am_pm = "PM";
            } else if (hourOfDay == 0) {
                hourOfDay += 12;
                am_pm = "AM";
            } else if (hourOfDay == 12)
                am_pm = "PM";
            else
                am_pm = "AM";

            txt_time.setText(hourOfDay + ":" + minute + " " + am_pm);
        }
    }
}
