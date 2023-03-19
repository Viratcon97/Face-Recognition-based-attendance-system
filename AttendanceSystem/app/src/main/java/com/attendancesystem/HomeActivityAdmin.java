package com.attendancesystem;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.attendancesystem.Helper.NavItem;
import com.attendancesystem.Model.DrawerListAdapter;

import java.util.ArrayList;

public class HomeActivityAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Constant Object
    constants constants;
    NavigationView navigationView;
    String title = "iAttendance (Admin)";
    Toolbar toolbar;
    ProgressDialog progressDialog;
    Handler handler;
    Window w;
    Intent i;
    String Email;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    ListView mDrawerList;
    ArrayList<NavItem> mNavItems = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("iAttendance (Admin)");

        setTitle("iAttendance (Admin)");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        toolbar.setTitle("iAttendance (Admin)");
        setTitle("iAttendance (Admin)");
        setActionBarTitle("iAttendance (Admin)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle("iAttendance (Admin)");
        setTitle("iAttendance (Admin)");
        setActionBarTitle("iAttendance (Admin)");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        toolbar.setTitle("iAttendance (Admin)");
        setTitle("iAttendance (Admin)");
        setActionBarTitle("iAttendance (Admin)");
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("iAttendance (Admin)");



        //Customize Navigation
        mNavItems.add(new NavItem("Home", "iAttendance Home", R.drawable.menu_home));
        mNavItems.add(new NavItem("User Management", "Manage User Data", R.drawable.menu_user_management));
        mNavItems.add(new NavItem("Operator Management", "Manage Operator Data", R.drawable.menu_operator_management));
        mNavItems.add(new NavItem("SMS Report", "SMS Attendance Report", R.drawable.menu_sms_reports));
        mNavItems.add(new NavItem("PDF Generation", "Generate user's PDF", R.drawable.menu_pdf));
        mNavItems.add(new NavItem("Email PDF Report", "Email PDF Report", R.drawable.menu_email));
        mNavItems.add(new NavItem("Change Pin", "Change Password", R.drawable.menu_change_pin));
        mNavItems.add(new NavItem("About iAttendance", "Get to know about us", R.drawable.menu_about_app));
        mNavItems.add(new NavItem("Logout", "", R.drawable.menu_logout));

        drawer = findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(HomeActivityAdmin.this, mNavItems);

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

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setActionBarTitle(title);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Default Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Home_Fragment()).commit();
        toolbar.setTitle("iAttendance (Admin)");
        //  displaySelectedScreen(R.id.nav_home);
    }


    private void selectItemFromDrawer(int position) {


        switch (position) {

            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Home_Fragment()).addToBackStack(null).commit();
                toolbar.setTitle("iAttendance (Admin)");

                break;

            case 1:
                progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Fragment_UserManagement()).addToBackStack(null).commit();
                        toolbar.setTitle("User Management");
                    }
                }, 2000);
                break;

            case 2:

                progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();

                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Fragment_OperatorManagement()).addToBackStack(null).commit();
                        toolbar.setTitle("Operator Management");
                    }
                }, 2000);
                break;

            case 3:

                progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();

                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Fragment_SMSReports()).addToBackStack(null).commit();
                        toolbar.setTitle("SMS Report");
                    }
                }, 2000);
                break;
            case 4:

                progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();

                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Fragment_PDFGeneration()).addToBackStack(null).commit();
                        toolbar.setTitle("PDF Generation");
                    }
                }, 2000);
                break;

            case 5:

                progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();

                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Fragment_email_pdf()).addToBackStack(null).commit();
                        toolbar.setTitle("Email PDF Report");
                    }
                }, 2000);
                break;

            case 6:

                progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
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
                        b.putString("email", Email);
                        fragment.setArguments(b);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        transaction.replace(R.id.content_frame, fragment);
                        transaction.commit();
                        toolbar.setTitle("Change Pin");
                    }
                }, 2000);
                break;
            case 7:

                progressDialog = new ProgressDialog(this, R.style.CustomProgressDialog);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {

                        progressDialog.dismiss();

                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Common_Fragment_AboutiAttendance()).addToBackStack(null).commit();
                        toolbar.setTitle("About iAttendance");
                    }
                }, 2000);
                break;

            case 8:

                Intent i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                System.exit(0);
        }
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    //On BackPressed event
   /*  @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

      //  getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Home_Fragment()).commit();

       else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("Quit iAttendance ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Admin_Home_Fragment()).commit();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            moveTaskToBack(false);
                        }
                    });

            builder.show();
        }*/

}
