package com.attendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class User_Home_Fragment extends Fragment {

    //GridView Object
    GridView gridView;
    Intent i;
    //Titles For Task
    public String[] gridViewTitles =
            {"Check Attendance",
                    "Search Attendance",
                    "Change Attendance Request",
            "Request Status"};

    public int[] gridViewImage =
            {R.drawable.user_home_check_attendance,
                    R.drawable.homescreen_searchrecords,
                    R.drawable.user_home_attendance_change_request,
                    R.drawable.icon_user_request_status};

    int[] gridhelpImage = {R.drawable.home_help_red,R.drawable.home_help_red,R.drawable.home_help_red,R.drawable.home_help_red};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_fragment_home,container,false);

        //Gridview
        gridView = (GridView)view.findViewById(R.id.gridview);

        User_Home_GridViewAdapter gridViewAdapter = new User_Home_GridViewAdapter(getContext(),gridViewTitles,gridViewImage,gridhelpImage);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

            }
        });
        return view;
    }
}
