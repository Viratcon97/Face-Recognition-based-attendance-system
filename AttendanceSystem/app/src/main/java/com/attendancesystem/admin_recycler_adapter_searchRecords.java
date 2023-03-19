package com.attendancesystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attendancesystem.Model.attendanceAdapter;

import java.util.List;

public class admin_recycler_adapter_searchRecords extends RecyclerView.Adapter<admin_recycler_adapter_searchRecords.View_searchRecord> {

    Context context;
    List<attendanceAdapter> attendanceAdapterList;

    public admin_recycler_adapter_searchRecords(Context context, List<attendanceAdapter> attendanceAdapterList) {
        this.context = context;
        this.attendanceAdapterList = attendanceAdapterList;
    }

    @Override
    public View_searchRecord onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_admin_searchrecord,null);
        return new admin_recycler_adapter_searchRecords.View_searchRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_searchRecord holder, int i) {


        final attendanceAdapter adapter = attendanceAdapterList.get(i);
        holder.setEmail.setText(adapter.getEmail());
        holder.setName.setText(adapter.getName());
        holder.setDate.setText(adapter.getDate());
        holder.setTime.setText(adapter.getTime());
        holder.setSRNo.setText(""+(i+1));
    }

    @Override
    public int getItemCount() {
        return attendanceAdapterList.size();
    }

    public class View_searchRecord extends RecyclerView.ViewHolder {
        TextView setEmail,setName,setDate,setTime,setSRNo;

        public View_searchRecord(@NonNull View itemView) {
            super(itemView);

            setEmail = itemView.findViewById(R.id.get_Email_);
            setName = itemView.findViewById(R.id.get_Name_);
            setDate = itemView.findViewById(R.id.get_Date_);
            setTime = itemView.findViewById(R.id.get_Time_);
            setSRNo = itemView.findViewById(R.id.get_SRNo_);
        }
    }
}
