package com.attendancesystem;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Common_Fragment_ChangePin extends Fragment {

    String email,oldPin;
    TextView setEmail;
    EditText setPin;
    Button btn_submit;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        email = getArguments().getString("email");

        return inflater.inflate(R.layout.common_fragment_change_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPin = view.findViewById(R.id.edittext_getpin);
        btn_submit = view.findViewById(R.id.btn_submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPin = setPin.getText().toString();

                if(oldPin.equals("") || oldPin.length() < 4){
                    setPin.setError("Please Enter Pin");
                    setPin.requestFocus();
                }else {

                    Query query =  databaseReference.child("Register").orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                String Fetch_Pin = snapshot.child("password").getValue().toString();
                                final String Fetch_key = snapshot.getKey();

                                if(oldPin.equals(Fetch_Pin)){

                                    //Pin Match
                                    //Open Dialog For New Pin, Set New Pin and Store it into Database
                                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                                    View alertLayout = inflater.inflate(R.layout.custom_common_changepin, null);
                                    final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

                                    Button set_btn = alertLayout.findViewById(R.id.btn_setpin);
                                    Button set_clear = alertLayout.findViewById(R.id.btn_clear);
                                    ImageView btn_close = alertLayout.findViewById(R.id.btn_close);
                                    final EditText new_pin = alertLayout.findViewById(R.id.edittext_new_getpin);
                                    final EditText new_c_pin = alertLayout.findViewById(R.id.edittext_new_c_getpin);

                                    set_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String newpin = new_pin.getText().toString();
                                            String newconpin = new_c_pin.getText().toString();
                                            if(newpin.equals("") || newpin.length() < 4){
                                                new_pin.setError("Please Enter New Pin");
                                                new_pin.requestFocus();
                                            }else if(newconpin.equals("") || newconpin.length() < 4){
                                                new_c_pin.setError("Please Confirm New Pin");
                                                new_c_pin.requestFocus();
                                            }else if(!newpin.equals(newconpin)){
                                                Snackbar.make(view,"Pin Does Not Match !",Snackbar.LENGTH_LONG).show();
                                            }else if(newpin.equals(newconpin)){


                                                databaseReference.child("Register").child(Fetch_key).child("password").setValue(newpin);
                                                Toast.makeText(getActivity(),"Pin Successfully Changed",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    set_clear.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            new_pin.setText("");
                                            new_c_pin.setText("");
                                        }
                                    });
                                    btn_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.setView(alertLayout);
                                    dialog.show();

                                }else {
                                    Snackbar.make(view,"Pin Does Not Match !",Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
