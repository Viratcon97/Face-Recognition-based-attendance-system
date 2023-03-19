package com.attendancesystem;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class Admin_Home_Fragment extends Fragment {

    //GridView Object
    GridView gridView;

    //Titles For Task
    public String[] gridViewTitles =
            {"Initial Face Recognition",
                    "View Attendance",
                    "Search Records",
                    "Pending Request"
                    ,"Attendance Request Report"};

    public int[] gridViewImage =
            {R.drawable.homescreen_initialattendance,
                    R.drawable.homescreen_generateattendance,
                    R.drawable.homescreen_searchrecords,
                    R.drawable.homescreen_pendingrequest,
                    R.drawable.homescreen_managerequest};

    int[] gridhelpImage = {R.drawable.home_help_red,R.drawable.home_help_red,R.drawable.home_help_red,R.drawable.home_help_red,R.drawable.home_help_red};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.admin_fragment_home,container,false);

        //Gridview
        gridView = (GridView)view.findViewById(R.id.gridview);

        Admin_Home_GridViewAdapter gridViewAdapter = new Admin_Home_GridViewAdapter(getContext(),gridViewTitles,gridViewImage,gridhelpImage);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {


            }
        });
        return view;
    }
}
