package com.attendancesystem;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.attendancesystem.Model.attendanceAdapter;
import com.attendancesystem.Model.showAttendance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminViewAttendanceAdapter extends RecyclerView.Adapter<AdminViewAttendanceAdapter.ViewAttendance> {

    Context context;
    //List Object of Register Adapter
    List<showAttendance> registerAdapterList;

    List<attendanceAdapter> eachAttendanceAdapterList;
    admin_recycler_adapter_eachAttendance adap;
    RecyclerView recyclerView;
    ImageView close_image;

    public AdminViewAttendanceAdapter(Context context, List<showAttendance> registerAdapterList) {
        this.context = context;
        this.registerAdapterList = registerAdapterList;
    }

    @Override
    public ViewAttendance onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        eachAttendanceAdapterList = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_admin_viewattendance_,null);
        return new AdminViewAttendanceAdapter.ViewAttendance(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewAttendance holder, int i) {

        final showAttendance adapter = registerAdapterList.get(i);
        holder.setEmail.setText(adapter.getEmail());
        holder.setName.setText(adapter.getName());
        holder.setTotalPresent.setText(adapter.getTotalpresent());

        holder.setInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context,R.style.DialogTheme);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.custom_view_each_attendance);
                close_image = dialog.findViewById(R.id.image_close);
                recyclerView = dialog.findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                dialog.show();

                //Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_Data")
                //        .orderByChild("email").equalTo(adapter.getEmail());

                Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Attendance_Data")
                        .orderByChild("date");

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        eachAttendanceAdapterList.clear();

                        String Fetch_email;

                        if(dataSnapshot.exists()) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                                Fetch_email = snapshot.child("email").getValue().toString();

                                if(Fetch_email.equals(adapter.getEmail())) {

                                    attendanceAdapter adapter_ = snapshot.getValue(attendanceAdapter.class);
                                    eachAttendanceAdapterList.add(adapter_);
                                    adap = new admin_recycler_adapter_eachAttendance(context, eachAttendanceAdapterList);
                                    recyclerView.setAdapter(adap);
                                    adap.notifyDataSetChanged();
                                }
                            }
                        }else {
                            Toast.makeText(context,"No Records",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                close_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return registerAdapterList.size();
    }

    public class ViewAttendance extends RecyclerView.ViewHolder {

        TextView setEmail,setName,setTotalPresent;
        ImageView setInfo;
        public ViewAttendance(@NonNull View itemView) {
            super(itemView);
            setEmail = itemView.findViewById(R.id.getEmail);
            setName = itemView.findViewById(R.id.getUsername);
            setTotalPresent = itemView.findViewById(R.id.getTotalPresent);
            setInfo = itemView.findViewById(R.id.getInfo);
        }
    }
}
