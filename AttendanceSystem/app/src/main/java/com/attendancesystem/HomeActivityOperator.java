package com.attendancesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.attendancesystem.Helper.NavItem;
import com.attendancesystem.Model.DrawerListAdapter;

import java.util.ArrayList;

public class HomeActivityOperator extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progressDialog;
    Handler handler;
    Intent i;
    String Email;
    Toolbar toolbar;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    ListView mDrawerList;
    ArrayList<NavItem> mNavItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_operator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Customize Navigation
        mNavItems.add(new NavItem("Home", "iAttendance Home", R.drawable.menu_home));
        mNavItems.add(new NavItem("Profile", "Update your Personal Details", R.drawable.menu_profile));
        mNavItems.add(new NavItem("Change Pin", "Change Password", R.drawable.menu_change_pin));
        mNavItems.add(new NavItem("About iAttendance", "Get to know about us", R.drawable.menu_about_app));
        mNavItems.add(new NavItem("Logout", "", R.drawable.menu_logout));

        drawer = findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(HomeActivityOperator.this, mNavItems);

        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mDrawerList.requestFocus();
                selectItemFromDrawer(position);
                drawer.closeDrawer(GravityCompat.START);

            }
        });

        //End - Customize Navigation


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Operator_Home_Fragment()).commit();
    }

    private void selectItemFromDrawer(int position) {
        switch (position) {

            case 0:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new Operator_Home_Fragment()).commit();
                break;

            case  1:

                progressDialog = new ProgressDialog(this,R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        i = getIntent();
                        Email = i.getStringExtra("email");
                        Fragment fragment = new Operator_Fragment_Profile();
                        Bundle b = new Bundle();
                        b.putString("email",Email);
                        fragment.setArguments(b);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        transaction.replace(R.id.content_frame,fragment);
                        transaction.commit();
                        toolbar.setTitle("Profile");
                    }
                },2000);
                break;

            case  2:

                progressDialog = new ProgressDialog(this,R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        i = getIntent();
                        Email = i.getStringExtra("email");
                        Fragment fragment = new Common_Fragment_ChangePin();
                        Bundle b = new Bundle();
                        b.putString("email",Email);
                        fragment.setArguments(b);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        transaction.replace(R.id.content_frame,fragment);
                        transaction.commit();
                        toolbar.setTitle("Change Pin");
                    }
                },2000);
                break;

            case  3:

                progressDialog = new ProgressDialog(this,R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null).replace(R.id.content_frame, new Common_Fragment_AboutiAttendance()).commit();
                    }
                },2000);
                break;

            case  4:

                Intent i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                System.exit(0);

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    void displaySelectedScreen(int itemId) {

        switch (itemId) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.content_frame, new Operator_Home_Fragment()).commit();
                break;

            case  R.id.nav_profile:

                progressDialog = new ProgressDialog(this,R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                public void run() {

                    progressDialog.dismiss();
                    i = getIntent();
                    Email = i.getStringExtra("email");
                    Fragment fragment = new Operator_Fragment_Profile();
                    Bundle b = new Bundle();
                    b.putString("email",Email);
                    fragment.setArguments(b);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                    transaction.replace(R.id.content_frame,fragment);
                    transaction.commit();
                    toolbar.setTitle("Profile");
                }
                },2000);
                break;

            case  R.id.nav_changepin:

                progressDialog = new ProgressDialog(this,R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        i = getIntent();
                        Email = i.getStringExtra("email");
                        Fragment fragment = new Common_Fragment_ChangePin();
                        Bundle b = new Bundle();
                        b.putString("email",Email);
                        fragment.setArguments(b);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        transaction.replace(R.id.content_frame,fragment);
                        transaction.commit();
                        toolbar.setTitle("Change Pin");
                    }
                },2000);
                break;

            case  R.id.nav_about_app:

                progressDialog = new ProgressDialog(this,R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null).replace(R.id.content_frame, new Common_Fragment_AboutiAttendance()).commit();
                        }
                    },2000);
                break;

            case  R.id.nav_logout:

                Intent i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                System.exit(0);

        }
    }
}