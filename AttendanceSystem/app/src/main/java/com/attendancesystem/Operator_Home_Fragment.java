package com.attendancesystem;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


public class Operator_Home_Fragment extends Fragment {

    GridView gridView;
    Intent i;
    //Titles For Task
    public String[] gridViewTitles ={"Take Attendance"};

    public int[] gridViewImage =
            {R.drawable.home_attendance};
    int[] gridhelpImage = {R.drawable.home_help_red};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.operator_fragment_home,container,false);
        gridView = (GridView)view.findViewById(R.id.gridview);

        Operator_Home_GridViewAdapter gridViewAdapter = new Operator_Home_GridViewAdapter(getContext(),gridViewTitles,gridViewImage,gridhelpImage);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

            }
        });
        return view;
    }
}
