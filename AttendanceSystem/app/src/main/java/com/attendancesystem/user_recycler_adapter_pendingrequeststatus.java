package com.attendancesystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.attendancesystem.Model.attendancerequestreport;

import java.util.List;

public class user_recycler_adapter_pendingrequeststatus extends RecyclerView.Adapter<user_recycler_adapter_pendingrequeststatus.View_requestStatus> {


    Context context;
    List<attendancerequestreport> attendancerequestreportslist;

    public user_recycler_adapter_pendingrequeststatus(Context context, List<attendancerequestreport> attendancerequestreportslist) {
        this.context = context;
        this.attendancerequestreportslist = attendancerequestreportslist;
    }

    @NonNull
    @Override
    public View_requestStatus onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_user_request_report,null);
        return new user_recycler_adapter_pendingrequeststatus.View_requestStatus(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_requestStatus holder, int i) {

        final attendancerequestreport adapter = attendancerequestreportslist.get(i);
        holder.setDate.setText(adapter.getDate());
        holder.setStatus.setText(adapter.getStatus());
        holder.setSRNo.setText(""+(i+1));
    }

    @Override
    public int getItemCount() {
        return attendancerequestreportslist.size();
    }

    public class View_requestStatus extends RecyclerView.ViewHolder {

        TextView setDate,setStatus,setSRNo;
        public View_requestStatus(@NonNull View itemView) {
            super(itemView);

            setSRNo = itemView.findViewById(R.id.getSRNo);
            setDate = itemView.findViewById(R.id.getDate);
            setStatus = itemView.findViewById(R.id.getStatus);
        }
    }
}
