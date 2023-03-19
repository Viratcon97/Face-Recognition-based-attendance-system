package com.attendancesystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class User_Fragment_searchAttendance extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatabaseReference databaseReference;
    String email;
    String date, current_Date;
    EditText edittext_date;
    Button btn_search,btn_Date;
    SimpleDateFormat sdf;
    Boolean status;
    //Date Time Picker
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int day,month,year;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_fragment_search_attendance, container, false);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),R.style.DialogTheme ,this, year, month, day);

        edittext_date = view.findViewById(R.id.edittext_date);
        btn_search = view.findViewById(R.id.btn_search);
        btn_Date= view.findViewById(R.id.button_date);

        sdf = new SimpleDateFormat("dd-MM-yyyy");

        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_Data");
        email = getArguments().getString("email");

        btn_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edittext_date.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please Enter Date !",Toast.LENGTH_LONG).show();
                }else {

                    Query query = databaseReference.orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            current_Date = edittext_date.getText().toString();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                date=snapshot.child("date").getValue().toString();
                                if(current_Date.equals(date)){
                                    String key = snapshot.getKey();
                                    String database_time = dataSnapshot.child(key).child("time").getValue().toString();
                                    String database_date = dataSnapshot.child(key).child("date").getValue().toString();

                                    ImageView close_image;
                                    TextView setTime,setDate;
                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.setContentView(R.layout.custom_user_search_record);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setCancelable(true);
                                    dialog.show();
                                    setDate = (TextView)dialog.findViewById(R.id.get_Date_);
                                    setTime = (TextView)dialog.findViewById(R.id.get_Time_);
                                    close_image = (ImageView)dialog.findViewById(R.id.close_image);
                                    setDate.setText(database_date);
                                    setTime.setText(database_time);
                                    status = true;
                                    close_image.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                   }
                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    //    Toast.makeText(getActivity(),"No Data Found For Entered Date ! ",Toast.LENGTH_LONG).show();

                }
            }
        });
        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        month = month + 1;
        if(dayOfMonth < 10 || month < 10){
            String m = "0" + month;
            String d = "0" + dayOfMonth;
            if(month < 10 && dayOfMonth < 10){
                edittext_date.setText(d + "-" + m + "-" + year);
            }else if(month < 10){
                edittext_date.setText(dayOfMonth + "-" + m + "-" + year);
            }else if(dayOfMonth < 10){
                edittext_date.setText(d + "-" + month + "-" + year);
            }
        }
        else {
            edittext_date.setText(dayOfMonth + "-" + month + "-" + year);
        }
    }
}