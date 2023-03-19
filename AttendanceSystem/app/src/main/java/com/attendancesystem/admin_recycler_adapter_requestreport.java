package com.attendancesystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attendancesystem.Model.attendancerequestreport;

import java.util.List;

public class admin_recycler_adapter_requestreport extends RecyclerView.Adapter<admin_recycler_adapter_requestreport.View_requestreport_adapter> {

    Context context;
    List<attendancerequestreport> attendancerequestreportslist;

    public admin_recycler_adapter_requestreport(Context context, List<attendancerequestreport> attendancerequestreportslist) {
        this.context = context;
        this.attendancerequestreportslist = attendancerequestreportslist;
    }

    @Override
    public View_requestreport_adapter onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_admin_request_report,null);
        return new admin_recycler_adapter_requestreport.View_requestreport_adapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_requestreport_adapter holder, int i) {

        final attendancerequestreport adapter = attendancerequestreportslist.get(i);
        holder.setEmail.setText(adapter.getEmail());
        holder.setDate.setText(adapter.getDate());
        holder.setStatus.setText(adapter.getStatus());
    }

    @Override
    public int getItemCount() {
        return attendancerequestreportslist.size();
    }

    public class View_requestreport_adapter extends RecyclerView.ViewHolder {

        TextView setEmail,setDate,setStatus;

        public View_requestreport_adapter(@NonNull View itemView) {
            super(itemView);

            setEmail = itemView.findViewById(R.id.getEmail);
            setDate = itemView.findViewById(R.id.getDate);
            setStatus = itemView.findViewById(R.id.getStatus);

        }
    }
}
