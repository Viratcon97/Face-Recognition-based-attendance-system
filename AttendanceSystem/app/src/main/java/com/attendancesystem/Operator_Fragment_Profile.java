package com.attendancesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Operator_Fragment_Profile extends Fragment {

    TextView setemail,setname,setmobile,setuser;
    String email;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.operator_fragment_profile,container,false);

        email = getArguments().getString("email");

        setemail = (TextView)view.findViewById(R.id.profile_set_email);
        setname = (TextView)view.findViewById(R.id.profile_set_name);
        setmobile = (TextView)view.findViewById(R.id.profile_set_mobile);
        setuser = (TextView)view.findViewById(R.id.profile_set_usertype);

        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
        //String key = databaseReference.push().getKey();

        Query query = databaseReference.child("Register").orderByChild("email")
                .equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    String Email = snapshot.child("email").getValue().toString();
                    String Mobile = snapshot.child("mobile").getValue().toString();
                    String User = snapshot.child("usertype").getValue().toString();
                    String Name = snapshot.child("name").getValue().toString();
                    setemail.setText(Email);
                    setname.setText(Name);
                    setmobile.setText(Mobile);
                    setuser.setText(User);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_common_profile_edit,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_item:

                //Open Dialog
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View alertLayout = inflater.inflate(R.layout.custom_edit_profiledata, null);
                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

                final EditText name_ = alertLayout.findViewById(R.id.set_name_);
                final EditText mobile_ = alertLayout.findViewById(R.id.set_mobile_);
                final EditText email_ = alertLayout.findViewById(R.id.set_email_);
                Button update_ = alertLayout.findViewById(R.id.btn_update);
                Button clear_ = alertLayout.findViewById(R.id.btn_clear);

                Query query =databaseReference.child("Register")
                        .orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                name_.setText(snapshot.child("name").getValue().toString());
                                mobile_.setText(snapshot.child("mobile").getValue().toString());
                                email_.setText(snapshot.child("email").getValue().toString());
                            }

                        }else {
                            Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                update_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String updated_name = name_.getText().toString();
                        final String updated_email = email_.getText().toString();
                        final String updated_mobile = mobile_.getText().toString();

                        //Update in Register Table
                        Query query1 = databaseReference.child("Register")
                                .orderByChild("email").equalTo(email);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Fetch_key;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Fetch_key = snapshot.getKey();


                                    databaseReference.child("Register").child(Fetch_key).child("email").setValue(updated_email);
                                    databaseReference.child("Register").child(Fetch_key).child("name").setValue(updated_name);
                                    databaseReference.child("Register").child(Fetch_key).child("mobile").setValue(updated_mobile);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(getActivity(),"Details Updated !",Toast.LENGTH_LONG).show();
                    }
                });

                clear_.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name_.setText("");
                        mobile_.setText("");
                        email_.setText("");

                    }
                });

                dialog.setView(alertLayout);
                dialog.show();
                break;
        }
        return false;
    }
}
