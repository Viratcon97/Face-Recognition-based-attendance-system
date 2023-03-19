package com.attendancesystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.attendancesystem.Model.changeAttendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminChangeAttendanceRequestAdapter extends RecyclerView.Adapter<AdminChangeAttendanceRequestAdapter.View_changeAttendanceRequest_View_adapter> {

    Context context;
    ArrayList<changeAttendance> changeAttendanceList;
    String Fetch_Email,Fetch_Uname,Fetch_Key,Fetch_Date;
    int Fetch_TotalPresent;
    Map<String,String> attendance_data;

    Map<String,String> request_report;
    DatabaseReference databaseReference;

    public AdminChangeAttendanceRequestAdapter(Context context) {
        this.context = context;
        this.changeAttendanceList = new ArrayList<>();
    }
    public void setData(ArrayList<changeAttendance> changeAttendanceList){
        this.changeAttendanceList.clear();
        this.changeAttendanceList.addAll(changeAttendanceList);
        notifyDataSetChanged();
    }

    @Override
    public View_changeAttendanceRequest_View_adapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        attendance_data = new HashMap<>();
        request_report = new HashMap<>();

        View view = inflater.inflate(R.layout.layout_admin_change_attendance,null);
        return new AdminChangeAttendanceRequestAdapter.View_changeAttendanceRequest_View_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final View_changeAttendanceRequest_View_adapter holder, int i) {

        final changeAttendance adapter = changeAttendanceList.get(i);
        holder.setEmail.setText(adapter.getEmail());
        holder.setDate.setText(adapter.getDate());

        holder.info_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_attendance_info_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView setEmail = dialog.findViewById(R.id.get_Email_);
                TextView setDate = dialog.findViewById(R.id.get_Date_);
                TextView setTime = dialog.findViewById(R.id.get_Time_);
                TextView setReason = dialog.findViewById(R.id.get_Reason_);
                ImageView close_image = dialog.findViewById(R.id.close_image);
                dialog.setCancelable(true);
                setEmail.setText(adapter.getEmail());
                setDate.setText(adapter.getDate());
                setTime.setText(adapter.getTime());
                setReason.setText(adapter.getReason());
                dialog.show();
                close_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        holder.accept_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("CONFIRMATION");
                alert.setMessage("Are You Sure You Want to Accept The Following Request ? ");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Database Operation To Accept User Request and And Add 1 Attendance To User's Account
                        //final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance")
                        //        .child("Attendance_Data");

                        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance")
                                .child("Register");

                        //final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Attendance")
                        //        .child("Request_Report");

                        //To Remove Value
                        //final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Attendance")
                        //        .child("Attendance_change_request");
                        //Fetch Record
                        Log.i("DATA","INSIDE");


                        Query query = databaseReference.orderByChild("email").equalTo(adapter.getEmail());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Fetch_Email = snapshot.child("email").getValue().toString();
                                    Fetch_Uname = snapshot.child("name").getValue().toString();
                                    Fetch_TotalPresent = Integer.parseInt(snapshot.child("totalpresent").getValue().toString());
                                    Fetch_Key = snapshot.getKey();
                                    Log.i("DATA","INSIDE QUERY");

                                    //Total Present Incremented and Inserted
                                    Fetch_TotalPresent += 1;
                                    databaseReference.child(Fetch_Key).child("totalpresent").setValue(String.valueOf(Fetch_TotalPresent));

                                    //Check IF Data is available for Data Given or Not


                                    //Add Attendance to Attendance_Data Record
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance")
                                            .child("Attendance_Data");

                                    attendance_data.put("date",adapter.getDate());
                                    attendance_data.put("email",Fetch_Email);
                                    attendance_data.put("name",Fetch_Uname);
                                    attendance_data.put("time",adapter.getTime());

                                    reference.push().setValue(attendance_data);



                                    //Add Record To Request Report when Accepted
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Attendance")
                                            .child("Request_Report");

                                    request_report.put("email",Fetch_Email);
                                    request_report.put("date",adapter.getDate());
                                    request_report.put("status","Accepted");

                                    reference1.push().setValue(request_report);
                                    Toast.makeText(context,"REQUEST ACCEPTED",Toast.LENGTH_LONG).show();


                                    //Remove Record From Attendance_change_request when Accepted

                                    final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Attendance")
                                            .child("Attendance_change_request");

                                    Query query1 = reference2.orderByChild("date").equalTo(adapter.getDate());
                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                                                String get_mail = snapshot1.child("email").getValue().toString();

                                                if(get_mail.equals(adapter.getEmail())){

                                                    reference2.child(snapshot1.getKey()).removeValue();

                                                }else {
                                                    Log.i("DATA","NOT FOUND");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    Toast.makeText(context,"REQUEST ACCEPTED",Toast.LENGTH_LONG).show();
                                    adapterCallBack.onDeleteSuccess();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        holder.reject_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("WARNING");
                alert.setMessage("Are You Sure You Want to Reject Following Request ?");

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Add Record To Request Report when Accepted
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Attendance")
                                .child("Request_Report");

                        request_report.put("email",adapter.getEmail());
                        request_report.put("date",adapter.getDate());
                        request_report.put("status","Rejected");

                        reference1.push().setValue(request_report);

                        //Remove Record From Attendance_change_request when Accepted

                        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Attendance")
                                .child("Attendance_change_request");

                        Query query1 = reference2.orderByChild("date").equalTo(adapter.getDate());
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){

                                    String get_mail = snapshot1.child("email").getValue().toString();

                                    if(get_mail.equals(adapter.getEmail())){

                                        reference2.child(snapshot1.getKey()).removeValue();

                                    }else {
                                        Log.i("DATA","NOT FOUND");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(context,"REQUEST REJECTED",Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

    }
    public CallBack adapterCallBack;  //Interface Object

    public void setAdapterCallBack(CallBack adapterCallBack) {
        this.adapterCallBack = adapterCallBack;
    }

    //Interface
    public interface CallBack {
        void onDeleteSuccess();
    }
    @Override
    public int getItemCount() {
        return changeAttendanceList.size();
    }

    public class View_changeAttendanceRequest_View_adapter extends RecyclerView.ViewHolder {

        TextView setEmail,setDate,accept_req,reject_req;
        ImageView info_Dialog;

        public View_changeAttendanceRequest_View_adapter(@NonNull View itemView) {
            super(itemView);
            setEmail = itemView.findViewById(R.id.getEmail);
            setDate = itemView.findViewById(R.id.getDate);
            accept_req = itemView.findViewById(R.id.accept_req);
            reject_req = itemView.findViewById(R.id.reject_req);
            info_Dialog = itemView.findViewById(R.id.info_Dialog);
        }
    }
}
