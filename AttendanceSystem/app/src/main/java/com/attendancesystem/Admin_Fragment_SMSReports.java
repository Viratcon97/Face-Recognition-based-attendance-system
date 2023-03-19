package com.attendancesystem;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

public class Admin_Fragment_SMSReports extends Fragment implements AdapterView.OnItemClickListener{

    ImageView image_general_help,image_low_help,imageView_general,imageView_low;
    DatabaseReference reference;
    String email;
    String category;
    Dialog dialog;
    String uname;
    List<String> user;
    Spinner spinner;
    Spinner spinner_user,spinner_category;
    MaterialBetterSpinner materialDesignSpinner;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment_sms_reports,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image_general_help = view.findViewById(R.id.image_help);
        image_low_help = view.findViewById(R.id.image_low_help);
        imageView_general = view.findViewById(R.id.imageView_general);
        imageView_low = view.findViewById(R.id.imageView_low);

        reference = FirebaseDatabase.getInstance().getReference("Attendance");

        image_general_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);

                dialog.setContentView(R.layout.custom_help_dialog);
                final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                title.setText("GENERAL ATTENDANCE REPORT"+"\n\n"+"This Will Help Admin to Send General SMS Report to Specific User. " +
                        "Admin Have to Select Particular User From The Dropdown List And SMS is send to User's Registered Number.");

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        image_low_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);

                dialog.setContentView(R.layout.custom_help_dialog);
                final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                title.setText("LOW ATTENDANCE REPORT"+"\n\n"+"This Will Help Admin to Send General SMS Report to Specific User. " +
                        "Admin Have to Select Particular User From The Dropdown List And SMS is send to User's Registered Number." +
                        "\nHere Admin Have To Select Category of Attendance of User (i.e. Category A and Category B).");

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });

        imageView_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_admin_general_sms);
                dialog.setCancelable(false);

                ImageView image_close = dialog.findViewById(R.id.image_close);
                Button btn_send = dialog.findViewById(R.id.btn_send);
                spinner = dialog.findViewById(R.id.spinner_user);

                //Fetching user name

                reference.child("Register").orderByChild("usertype").equalTo("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user = new ArrayList<>();
                        user.add("Select User");
                        user.add("All User");
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            uname = snapshot.child("email").getValue().toString();
                            user.add(uname);
                        }

                        spinner = dialog.findViewById(R.id.spinner_user);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item,user);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //End
                //Button Click
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        email = spinner.getSelectedItem().toString();

                        if(email.equals("") || email.equals("Select User")){
                            Toast.makeText(getActivity(),"Please Select Any User to Send Report !",Toast.LENGTH_LONG).show();
                        }else if(email.equals("All User")){

                            Query query = reference.child("Register").orderByChild("usertype").equalTo("User");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Log.i("DATA",snapshot.getValue().toString());
                                        String mobile = snapshot.child("mobile").getValue().toString();
                                        String username = snapshot.child("name").getValue().toString();
                                        String totalpresent = snapshot.child("totalpresent").getValue().toString();
                                        Log.i("DATA",snapshot.child("email").getValue().toString());

                                        Log.i("DATA",mobile);
                                        Log.i("DATA",username);
                                        Log.i("DATA",totalpresent);


                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(mobile, null,"Attendance Report \n"+username+", " +
                                                "Your Total Present is "+totalpresent+". \nPlease Generate Change Request " +
                                                " if You have any Query Regarding Attendance.", null, null);

                                    }
                                    Toast.makeText(getActivity(), "SMS sent.",
                                            Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else
                            {

                            Query query = reference.child("Register").orderByChild("email").equalTo(email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String mobile = null;
                                    String username = null;
                                    String totalpresent = null;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        mobile = snapshot.child("mobile").getValue().toString();
                                        username = snapshot.child("name").getValue().toString();
                                        totalpresent = snapshot.child("totalpresent").getValue().toString();
                                        Log.i("DATA",mobile);

                                    }

                                    Log.i("DATA",mobile);
                                    Log.i("DATA",username);
                                    Log.i("DATA",totalpresent);
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(mobile, null,"Attendance Report \n"+username+", " +
                                            "Your Total Present is "+totalpresent+". \nPlease Generate Change Request " +
                                            " if You have any Query Regarding Attendance.", null, null);
                                    Toast.makeText(getActivity(), "SMS sent.",
                                            Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

                //End

              image_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
                });

                dialog.show();

            }
        });

        imageView_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_admin_low_sms);
                dialog.setCancelable(false);

                ImageView image_close = dialog.findViewById(R.id.image_close);
                Button btn_send = dialog.findViewById(R.id.btn_send);
                spinner_user = dialog.findViewById(R.id.spinner_user);
                spinner_category = dialog.findViewById(R.id.spinner_category);
                //Fetching user name

                reference.child("Register").orderByChild("usertype").equalTo("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        user = new ArrayList<>();
                        user.add("Select User");
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            uname = snapshot.child("email").getValue().toString();
                            user.add(uname);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item,user);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_user.setSelection(0);
                        spinner_user.setAdapter(adapter);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,constants.category);
                spinner_category.setAdapter(adapter);
                spinner_category.setSelection(0);
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        email = spinner_user.getSelectedItem().toString();
                        category = spinner_category.getSelectedItem().toString();

                        if(email.equals("") || category.equals("") || email.equals("Select User") || category.equals("Select Category") ){
                            Toast.makeText(getActivity(),"Please Select Any User and Category to Send Report !",Toast.LENGTH_LONG).show();
                        }else {

                            Query query = reference.child("Register").orderByChild("email").equalTo(email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String mobile_ = null;
                                    String username_ = null;
                                    String totalpresent_ = null;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        mobile_ = snapshot.child("mobile").getValue().toString();
                                        username_ = snapshot.child("name").getValue().toString();
                                        totalpresent_ = snapshot.child("totalpresent").getValue().toString();

                                    }

                                    Log.i("DATA",mobile_);
                                    Log.i("DATA",username_);
                                    Log.i("DATA",totalpresent_);

                                  /*  SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(mobile_, null,"Attendance Report \n"+username_+", " +
                                            "Your Total Present for Last Month is "+totalpresent_+"."+
                                            "You Are In "+category+".\n"+
                                            "\nPlease Generate Change Request " +
                                            " if You have any Query Regarding Attendance.", null, null);
                                  */
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(mobile_, null,"Low Attendance Report \n"+""+category+"\n"+username_+", " +
                                            "Your Total Present is "+totalpresent_+". \nPlease Generate Change Request " +
                                            " if You have any Query Regarding Attendance.", null, null);
                                  Toast.makeText(getActivity(), "SMS sent.",
                                            Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
                image_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        uname = user.get(position);
    }
}
