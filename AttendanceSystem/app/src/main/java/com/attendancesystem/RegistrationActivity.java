package com.attendancesystem;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.attendancesystem.Model.registerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class RegistrationActivity extends AppCompatActivity {

    //Declaration

    //Object of class Constant
    constants constants;

    String userId; //Storing Key if needed

    String s_fullname,s_mobile,s_email,s_password,s_gender,s_usertype,fbpath;

    CheckBox showpassword_check;
    RadioButton male_rb,female_rb;
    MaterialBetterSpinner materialDesignSpinner;

    EditText fullname_et,mobile_et,email_et,password_et;
    Button login_btn,register_btn;

    //Firebase Database
    DatabaseReference databaseReference;

    //Shared Preference
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Spinner Binding and Array Adapter Binding with Custom Spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, constants.data);

         materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.android_material_design_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);

        //To add Custom Action Bar in Android
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customactionbarregister_);

        //Binding Register
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");

        //Binding
        fullname_et = (EditText)findViewById(R.id.edittext_fullname);
        mobile_et = (EditText)findViewById(R.id.edittext_mobile);
        email_et = (EditText)findViewById(R.id.edittext_email);
        password_et = (EditText)findViewById(R.id.edittext_password);

        male_rb = (RadioButton)findViewById(R.id.rb_male);
        female_rb = (RadioButton)findViewById(R.id.rb_female);

        showpassword_check = (CheckBox)findViewById(R.id.checkbox_showpassword);

        login_btn = (Button)findViewById(R.id.btn_sendOTP);
        register_btn = (Button)findViewById(R.id.btn_register);

        //Password Checkbox
        showpassword_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked)
                {
                    password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        //Registration Activity
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_fullname = fullname_et.getText().toString();
                s_mobile = mobile_et.getText().toString();
                s_email = email_et.getText().toString();
                s_password = password_et.getText().toString();
                fbpath = email_et.getText().toString();

                //Gender Selection
                if(male_rb.isChecked()) {
                    s_gender = "Male";
                } else if(female_rb.isChecked()) {
                    s_gender = "Female";
                } else{
                    s_gender = "";
                }

                //UserType Selection
                if(materialDesignSpinner.getText().toString().equals("Admin")){
                    s_usertype = "Admin";
                }else if(materialDesignSpinner.getText().toString().equals("User")){
                    s_usertype = "User";
                }else if(materialDesignSpinner.getText().toString().equals("Operator")){
                    s_usertype = "Operator";
                }else {
                    s_usertype = "";
                }

                //Click Event
                if(s_fullname.equals("") || (s_fullname.length() >30) || s_fullname.length() < 6  || !s_fullname.matches(constants.fullnamePattern)){
                    fullname_et.setError("Empty or Invalid");
                    fullname_et.requestFocus();
                }else if(s_mobile.equals("") || !s_mobile.matches(constants.mobilePattern) || (s_mobile.length() < 10)){
                    mobile_et.setError("Empty or Invalid");
                    mobile_et.requestFocus();
                }else if(s_email.equals("") || !s_email.matches(constants.emailPattern)){
                    email_et.setError("Empty or Invalid");
                    email_et.requestFocus();
                }else if(s_password.equals("") || s_password.length() < 4){
                    password_et.setError("Empty or Too Short");
                    password_et.requestFocus();
                }else if(s_gender.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Select Gender !",Toast.LENGTH_SHORT).show();
                }else if(s_usertype.equals("")){
                    materialDesignSpinner.setError("Please Select Any One");
                    materialDesignSpinner.requestFocus();
                }else{

                        Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Register").orderByChild("email").equalTo(s_email);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getChildrenCount() > 0)
                            {
                                Toast.makeText(getApplicationContext(),"Please Try Registration With Another Email ID !",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                //Insert New Records
                                userId = databaseReference.push().getKey();
                                String s_totalpresent = "0";
                                registerAdapter adapter = new registerAdapter(s_fullname,s_mobile,s_email,s_password,s_gender,s_usertype,s_totalpresent);
                                databaseReference.child("Register").child(userId).setValue(adapter);
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegistrationActivity.this,LoginActivity.class);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(),"Please Login with "+s_email,Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    databaseReference.setValue(adapter);

                }

            }
        });
        //Redirect to LoginActivity Page
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }


    //On BackPressed event
   /* @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Quit iAttendance ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        moveTaskToBack(true);
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
