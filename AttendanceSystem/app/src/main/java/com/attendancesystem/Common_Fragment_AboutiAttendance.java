package com.attendancesystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Common_Fragment_AboutiAttendance extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_fragment_about_iattendance,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txt_desc = view.findViewById(R.id.txt_desc);
        txt_desc.setText("          iAttendance is basically android based Attendance Management System for Employees. "+
                "iAttendance will deal with different users and mark attendance with face recognition. "+
                "User can access their Attendance data via their android smartphone. " +
                "User can generate request for Attendance change from device itself. " +
                "Even admin can manage their operation in device only. " +
                "No need of any Desktop or laptop to manage attendance of users. " +
                "Now Manage your Attendance with iAttendance.");
    }
}
