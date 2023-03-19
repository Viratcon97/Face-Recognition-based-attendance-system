package com.attendancesystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.attendancesystem.Model.attendanceAdapter;

import java.util.List;

public class admin_recycler_adapter_eachAttendance extends RecyclerView.Adapter<admin_recycler_adapter_eachAttendance.View_each_Attendance> {

    Context context;
    List<attendanceAdapter> each_attendancelist;

    public admin_recycler_adapter_eachAttendance(Context context, List<attendanceAdapter> each_attendancelist) {
        this.context = context;
        this.each_attendancelist = each_attendancelist;
    }

    @NonNull
    @Override
    public View_each_Attendance onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_admin_individualattendance,null);
        return new admin_recycler_adapter_eachAttendance.View_each_Attendance(view);

    }

    @Override
    public void onBindViewHolder(@NonNull View_each_Attendance holder, int i) {

        final attendanceAdapter adapter = each_attendancelist.get(i);
        holder.setEmail.setText(adapter.getEmail());
        holder.setName.setText(adapter.getName());
         holder.setDate.setText(adapter.getDate());
        holder.setTime.setText(adapter.getTime());
          holder.setSRNo.setText(""+(i+1));

    }

    @Override
    public int getItemCount() {
        return each_attendancelist.size();
    }

    public class View_each_Attendance extends RecyclerView.ViewHolder {

        TextView setEmail,setName,setDate,setTime,setSRNo;


        public View_each_Attendance(@NonNull View itemView) {
            super(itemView);

            setEmail = itemView.findViewById(R.id.get_Email_);
            setName = itemView.findViewById(R.id.get_Name_);
            setDate = itemView.findViewById(R.id.get_Date_);
            setTime = itemView.findViewById(R.id.get_Time_);
            setSRNo = itemView.findViewById(R.id.get_SRNo_);
       }
    }
}
