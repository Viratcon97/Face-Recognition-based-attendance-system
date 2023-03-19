package com.attendancesystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attendancesystem.Model.attendanceAdapter;
import com.attendancesystem.Model.registerAdapter;
import com.google.android.gms.vision.L;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class user_recycler_adapter_check_attendance extends RecyclerView.Adapter<user_recycler_adapter_check_attendance.View_check_attendance_View_adapter> {

    Context context;
    List<attendanceAdapter> registerAdapterList;

    public user_recycler_adapter_check_attendance(Context context, List<attendanceAdapter> registerAdapterList) {
        this.context = context;
        this.registerAdapterList = registerAdapterList;
    }

    @NonNull
    @Override
    public View_check_attendance_View_adapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_user_checkattendance,null);
        return new user_recycler_adapter_check_attendance.View_check_attendance_View_adapter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull View_check_attendance_View_adapter holder, int i) {

        final attendanceAdapter adapter = registerAdapterList.get(i);

        holder.set_SRNo_.setText(""+(i+1));
        holder.set_Date_.setText(adapter.getDate());
        holder.set_Time_.setText(adapter.getTime());
     //   holder.set_Name_.setText(adapter.getName());
    }

    @Override
    public int getItemCount() {
        return registerAdapterList.size();
    }

    public class View_check_attendance_View_adapter extends RecyclerView.ViewHolder {

        TextView set_SRNo_,set_Date_,set_Time_,set_Name_;
        public View_check_attendance_View_adapter(@NonNull View itemView) {
            super(itemView);

            set_SRNo_ = itemView.findViewById(R.id.get_SRNo_);
            set_Date_ = itemView.findViewById(R.id.get_Date_);
            set_Time_ = itemView.findViewById(R.id.get_Time_);
        }
    }
}
