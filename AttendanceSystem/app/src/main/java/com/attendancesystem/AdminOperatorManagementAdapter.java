package com.attendancesystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.attendancesystem.Model.registerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


//Recycler.Adapter
//Recycler.ViewHolder
//For User Management

public class AdminOperatorManagementAdapter extends RecyclerView.Adapter<AdminOperatorManagementAdapter.View_operator_management_View_adapter> {

    Context context;
    //List Object of Register Adapter
    ArrayList<registerAdapter> registerAdapterList;

    //
    CheckBox showpassword_check;
    RadioButton male_rb, female_rb;
    String s_gender;
    String s__gender;
    DatabaseReference databaseReference;
    String s__fullname;
    String s__mobile;
    String s__email;
    String s__password;
    String s__usertype;

    public AdminOperatorManagementAdapter(Context context) {
        this.context = context;
        this.registerAdapterList = new ArrayList<>();
    }

    @Override
    public View_operator_management_View_adapter onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_operator_management, null);
        return new AdminOperatorManagementAdapter.View_operator_management_View_adapter(view);
    }

    public void setData(ArrayList<registerAdapter> registerAdapterList) {
        this.registerAdapterList.clear();
        this.registerAdapterList.addAll(registerAdapterList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final View_operator_management_View_adapter holder, final int i) {

        //  notifyDataSetChanged();
        final registerAdapter adapter = registerAdapterList.get(i);

        holder.getEmail.setText("Email id: " + adapter.getEmail());
        holder.getName.setText("Name: " + adapter.getName());
        holder.getMobile.setText("Mobile No: " + adapter.getMobile());
        holder.getSRno.setText("" + (i + 1));

        //Update Activity
        holder.update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.custom_update_user);
                final ImageView close = (ImageView) dialog.findViewById(R.id.btn_close);
                final Button btn_update = (Button) dialog.findViewById(R.id.btn_update);
                final Button btn_clear = (Button) dialog.findViewById(R.id.btn_clear);
                final EditText set_name_ = (EditText) dialog.findViewById(R.id.set_name_);
                final EditText set_email_ = (EditText) dialog.findViewById(R.id.set_email_);
                final EditText set_mobile_ = (EditText) dialog.findViewById(R.id.set_mobile_);
                final EditText set_password_ = (EditText) dialog.findViewById(R.id.set_password_);

                male_rb = (RadioButton) dialog.findViewById(R.id.rb_male);
                female_rb = (RadioButton) dialog.findViewById(R.id.rb_female);
                showpassword_check = (CheckBox) dialog.findViewById(R.id.checkbox_showpassword);

                set_name_.setText(adapter.getName());
                set_mobile_.setText(adapter.getMobile());
                set_email_.setText(adapter.getEmail());
                set_password_.setText(adapter.getPassword());

                s_gender = adapter.getGender();
                if (s_gender.equals("Male")) {
                    male_rb.setChecked(true);
                } else {
                    female_rb.setChecked(true);
                }

                dialog.show();
                //Password Checkbox
                showpassword_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (!isChecked) {
                            set_password_.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            set_password_.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        s__fullname = set_name_.getText().toString();
                        s__mobile = set_mobile_.getText().toString();
                        s__email = set_email_.getText().toString();
                        s__password = set_password_.getText().toString();
                        s__usertype = "Operator";

                        //Gender Selection
                        if (male_rb.isChecked()) {
                            s__gender = "Male";
                        } else if (female_rb.isChecked()) {
                            s__gender = "Female";
                        } else {
                            s__gender = "";
                        }

                        //Click Event
                        if (s__fullname.equals("") || (s__fullname.length() > 20) || s__fullname.length() < 6 || !s__fullname.matches(constants.fullnamePattern)) {
                            set_name_.setError("Empty or Invalid");
                            set_name_.requestFocus();
                        } else if (s__mobile.equals("") || !s__mobile.matches(constants.mobilePattern) || (s__mobile.length() < 10)) {
                            set_mobile_.setError("Empty or Invalid");
                            set_mobile_.requestFocus();
                        } else if (s__email.equals("") || !s__email.matches(constants.emailPattern)) {
                            set_email_.setError("Empty or Invalid");
                            set_email_.requestFocus();
                        } else if (s__password.equals("") || s__password.length() < 4) {
                            set_password_.setError("Empty or Too Short");
                            set_password_.requestFocus();
                        } else if (s__gender.equals("")) {
                            Toast.makeText(context, "Please Select Gender !", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
                            //String key = databaseReference.push().getKey();

                            Query query = databaseReference.child("Register").orderByChild("email")
                                    .equalTo(adapter.getEmail());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        registerAdapter adapter = new registerAdapter(s__fullname, s__mobile, s__email, s__password, s__gender, s__usertype);

                                        Log.e("DATA", s__usertype);

                                        databaseReference.child("Register").child(snapshot.getKey()).setValue(adapter);
                                        holder.getEmail.setText("Email id: " + s__email);
                                        holder.getName.setText("Name: " + s__fullname);
                                        holder.getMobile.setText("Mobile No: " + s__mobile);

                                        Toast.makeText(context, "Updated !", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

                btn_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        set_name_.setText("");
                        set_mobile_.setText("");
                        set_email_.setText("");
                        set_password_.setText("");
                        male_rb.setChecked(false);
                        female_rb.setChecked(false);

                    }
                });
            }
        });

        //Delete Activity
        holder.delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("WARNING");
                alert.setMessage("Are You Sure You Want to Delete the Operator " + adapter.getName() + " ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
                        Query query = databaseReference.child("Register").orderByChild("email")
                                .equalTo(adapter.getEmail());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue();
                                    Toast.makeText(context, "Removed", Toast.LENGTH_LONG).show();
                                    adapterCallBack.onDeleteSuccess();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Toast.makeText(context, "Error" + databaseError, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).show();

            }
        });
    }


    public CallBack adapterCallBack;  //Interface Object

    public void setAdapterCallBack(CallBack adapterCallBack) {
        this.adapterCallBack = adapterCallBack;
    }

    //Interface
    public interface CallBack {
        void onDeleteSuccess();
    }

    @Override
    public int getItemCount() {
        return registerAdapterList.size();
    }

    public class View_operator_management_View_adapter extends RecyclerView.ViewHolder {

        TextView getEmail, getName, getMobile, getSRno;
        ImageView update_user, delete_user;

        public View_operator_management_View_adapter(@NonNull View itemView) {
            super(itemView);

            getSRno = itemView.findViewById(R.id.get_SRNo_);
            getEmail = itemView.findViewById(R.id.get_Email_);
            getName = itemView.findViewById(R.id.get_Name_);
            getMobile = itemView.findViewById(R.id.get_Mobile_);
            update_user = itemView.findViewById(R.id.update_user);
            delete_user = itemView.findViewById(R.id.delete_user);
        }
    }
}
