package com.attendancesystem;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
            {

    //Declaration
    constants constants; //Constant class Object

    String v_email,v_password,v_usertype; // Verifying String
    Button register_btn,login_btn;
    EditText password_edittext,email_edittext;
    CheckBox showpassword_check;
    String user;
    TextView forgotpass;
    String s_email,s_password,s_usertype;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //

        //Spinner Binding and Array Adapter Binding with Custom Spinner
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, constants.data);

        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.spinner_user);
        materialDesignSpinner.setAdapter(arrayAdapter);

        materialDesignSpinner.setOnItemClickListener(this);

        //To add Custom Action Bar in Android
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.customactionbarlogin_);

        //Binding
        password_edittext = (EditText)findViewById(R.id.edittext_password);
        email_edittext = (EditText)findViewById(R.id.edittext_email);
        showpassword_check = (CheckBox)findViewById(R.id.checkbox_showpassword);
        register_btn = (Button)findViewById(R.id.btn_register);
        login_btn = (Button)findViewById(R.id.btn_sendOTP);
        forgotpass = (TextView)findViewById(R.id.forgotpass);
        reference = FirebaseDatabase.getInstance().getReference("Attendance");

        //Forgot Password Activity
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(LoginActivity.this,R.style.DialogTheme);
                dialog.setContentView(R.layout.custom_forgotpassword);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setCancelable(true);
                final EditText email = dialog.findViewById(R.id.edittext_email);
                ImageView help = dialog.findViewById(R.id.image_help);
                ImageView close = dialog.findViewById(R.id.image_close);
                Button send_otp = dialog.findViewById(R.id.btn_sendOTP);
                dialog.show();
                send_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String getEmail = email.getText().toString();

                        if(getEmail.equals("")){
                            email.setError("Please Enter Email");
                            email.requestFocus();
                        }else if(!getEmail.matches(constants.emailPattern)){
                            email.setError("Please Enter Valid Email");
                            email.requestFocus();
                        }else {
                            //If Email Match
                            //Fetch phone Number and Pin of User
                            Query query = reference.child("Register").orderByChild("email")
                                    .equalTo(getEmail);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()){

                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            String email_ = snapshot.child("email").getValue().toString();
                                            String mobile_ = snapshot.child("mobile").getValue().toString();
                                            String pin_ = snapshot.child("password").getValue().toString();

                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(mobile_, null,"iAttendance Forgot Password \n"+email_+", " +
                                                    "Your Password is "+pin_+". \nPlease Change Password Once You Receive message. ", null, null);
                                            Toast.makeText(LoginActivity.this, "SMS sent.",
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    }else {
                                        Toast.makeText(LoginActivity.this, "Please Enter Registered Email id",
                                                Toast.LENGTH_LONG).show();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(LoginActivity.this,"Please Enter Registered Email id",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(true);

                        dialog.setContentView(R.layout.custom_help_dialog);
                        final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                        final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                        title.setText("FORGOT PASSWORD"+"\n\n"+"This Will User/Admin/Operator to Get Their Password. " +
                                "By Simply Entering Registered email Id, User/Admin/Operator Will Receive SMS on" +
                                " Their Registered Mobile Number.");

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

        //Password Checkbox
        showpassword_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked)
                {
                    password_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    password_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Redirect to RegistrationActivity Page
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        //Redirect to Home Activity Page
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_email = email_edittext.getText().toString().trim();
                s_password = password_edittext.getText().toString().trim();

                //User type Selection
                if(materialDesignSpinner.getText().toString().equals("Admin")){
                    s_usertype = "Admin";
                }else if(materialDesignSpinner.getText().toString().equals("User")){
                    s_usertype = "User";
                }else if(materialDesignSpinner.getText().toString().equals("Operator")){
                    s_usertype = "Operator";
                }else {
                    s_usertype = "";
                }

                if(s_email.equals("") || !s_email.matches(constants.emailPattern)){
                    email_edittext.setError("Empty or Invalid");
                    email_edittext.requestFocus();
                }else if(s_password.equals("") || s_password.length() < 4){
                    password_edittext.setError("Empty or Too Short");
                    password_edittext.requestFocus();
                }else if(s_usertype.equals("")) {
                    materialDesignSpinner.setError("Please Select Any One");
                    materialDesignSpinner.requestFocus();
                }else
                {

                    Query query = FirebaseDatabase.getInstance().getReference("Attendance").child("Register").orderByChild("email").limitToFirst(1).equalTo(s_email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                                v_email=childSnapshot.child("email").getValue().toString();
                                v_password=childSnapshot.child("password").getValue().toString();
                                v_usertype = childSnapshot.child("usertype").getValue().toString();
                            }
                            if(s_email.equals(v_email) && s_password.equals(v_password) && s_usertype.equals(v_usertype))
                            {
                                if(v_usertype.equals("Admin")) {
                                    Intent i = new Intent(LoginActivity.this,HomeActivityAdmin.class);
                                    i.putExtra("email",v_email);
                                    startActivity(i);
                                }
                                else if(v_usertype.equals("User")) {
                                    Intent i = new Intent(LoginActivity.this,HomeActivityUser.class);
                                    i.putExtra("email",v_email);
                                    startActivity(i);
                                }
                                else if(v_usertype.equals("Operator")) {
                                    Intent i = new Intent(LoginActivity.this,HomeActivityOperator.class);
                                    i.putExtra("email",v_email);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Please Try Again With Registered User !",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please Try Again With Registered User !",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),""+databaseError,Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        user = constants.data[position];
    }

}
