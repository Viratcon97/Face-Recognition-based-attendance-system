package com.attendancesystem;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Admin_Fragment_email_pdf extends Fragment {

    ImageView image_help, imageView_pdf,image_m_pdf,image_m_help;
    Dialog dialog;
    Spinner spinner;
    List<String> user;
    String email;
    String Username;
    DatabaseReference reference;

    private String mailhost = "smtp.gmail.com";
    public String userid = "iattendancedemo@gmail.com";
    public String password = "Qwerty@123";
    public String from = "iattendancedemo@gmail.com";
    public String subject = "iAttendance Attendance Report";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_fragment_email_pdf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image_help = view.findViewById(R.id.image_general_help);
        imageView_pdf = view.findViewById(R.id.imageView_general_pdf);

        image_m_pdf = view.findViewById(R.id.imageView_month_pdf);
        image_m_help = view.findViewById(R.id.image_month_help);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        reference = FirebaseDatabase.getInstance().getReference("Attendance");

        image_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);

                dialog.setContentView(R.layout.custom_help_dialog);
                final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                title.setText("EMAIL GENERAL REPORT"+"\n\n"+"This Will Help Admin to Send Generated PDF Report to Specific User. " +
                        "Admin Have to Select Particular User from The Dropdown List And " +
                        "Report Will be Send directly to registered Email id with Attachment of Report in PDF Format and " +
                        "Email id as a Name of the File.");

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        image_m_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);

                dialog.setContentView(R.layout.custom_help_dialog);
                final ImageView close = (ImageView)dialog.findViewById(R.id.custom_dialog_close);
                final TextView title = (TextView)dialog.findViewById(R.id.custom_dialog_title);
                title.setText("EMAIL MONTHLY ATTENDANCE REPORT"+"\n\n"+"This Will Help Admin to Send Generated Monthly PDF Report to Specific User. " +
                        "Admin Have to Select Particular User and Particular Month with Year From The Dropdown List And " +
                        "report will be Send directly to registered Email id with Attachment of Report in PDF Format " +
                        "Email id as a Name of the File and Selected Month.");

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        imageView_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_admin_email_general_pdf);
                dialog.setCancelable(false);

                ImageView close = (ImageView) dialog.findViewById(R.id.image_close);
                Button btn_generate = dialog.findViewById(R.id.btn_generate);
                spinner = dialog.findViewById(R.id.spinner_user);

                //Fetching user name

                reference.child("Register").orderByChild("usertype").equalTo("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user = new ArrayList<>();
                        user.add("Select User");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            email = snapshot.child("email").getValue().toString();
                            user.add(email);
                        }

                        spinner = dialog.findViewById(R.id.spinner_user);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, user);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                btn_generate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        email = spinner.getSelectedItem().toString();
                        String path = "/storage/emulated/0/iAttendance/"+""+email+".pdf";
                        final File file = new File(path);

                        if (!file.exists() || !file.canRead()) {
                            Toast.makeText(getActivity(),"File Not Found",Toast.LENGTH_LONG).show();
                            return;
                        }


                        if (email.equals("") || email.equals("Select User")) {
                            Toast.makeText(getActivity(), "Please Select Any User to Send Report !", Toast.LENGTH_LONG).show();
                        }else {

                            //Sending Mail
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {


                                        Properties props = new Properties();
                                        props.setProperty("mail.transport.protocol","smtp");
                                        props.setProperty("mail.host", mailhost);
                                        props.put("mail.smtp.auth", "true");
                                        props.put("mail.smtp.port","465");
                                        props.put("mail.smtp.socketFactory.port","465");
                                        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                                        props.put("mail.smtp.socketFactory.fallback","false");
                                        props.setProperty("mail.smtp.quitwait","false");
                                        Session session = Session.getInstance(props,
                                                new javax.mail.Authenticator() {
                                                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                                                        return new javax.mail.PasswordAuthentication(
                                                                userid,password);
                                                    }
                                                });
                                        // TODO Auto-generated method stub
                                        Message message = new MimeMessage(session);
                                        BodyPart bodyPart = new MimeBodyPart();
                                        message.setFrom(new InternetAddress(from));
                                        message.setFrom(new InternetAddress("iattendancedemo@gmail.com"));

                                        message.setRecipients(Message.RecipientType.TO,
                                                InternetAddress.parse(email));
                                        message.setSubject(subject);

                                        ((MimeBodyPart) bodyPart).setText("Hello, "+email+"\n"+
                                                "Here I am Sending your General Attendance Report ! \n"+
                                                "Please Go With Below Attachment !");

                                        if (!"".equals(file)) {
                                            Multipart _multipart = new MimeMultipart();
                                            BodyPart messageBodyPart = new MimeBodyPart();
                                            DataSource source = new FileDataSource(file);

                                            ((MimeBodyPart) messageBodyPart).addHeaderLine("Hello, "+email);
                                             messageBodyPart.setText("Hello, "+email
                                                    + "\n\n Please Go With Below Attachment.");
                                            messageBodyPart.setDataHandler(new DataHandler(source));
                                            messageBodyPart.setFileName(""+email+".pdf");

                                            _multipart.addBodyPart(bodyPart);
                                            _multipart.addBodyPart(messageBodyPart);
                                            message.setContent(_multipart);
                                        }
                                        Transport.send(message);
                                    } catch (MessagingException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }).start();

                            Toast.makeText(getActivity(),"Mail Sent",Toast.LENGTH_LONG).show();


                            //End Sending Mail
                        }

                    }
                });
                dialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        image_m_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_admin_email_month_pdf);
                dialog.setCancelable(false);

                ImageView close = (ImageView) dialog.findViewById(R.id.image_close);
                Button btn_generate = dialog.findViewById(R.id.btn_generate);
                final TextView setDate = (TextView)dialog.findViewById(R.id.setDate);
                spinner = dialog.findViewById(R.id.spinner_user);

                reference.child("Register").orderByChild("usertype").equalTo("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user = new ArrayList<>();
                        user.add("Select User");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            email = snapshot.child("email").getValue().toString();
                            user.add(email);
                        }

                        spinner = dialog.findViewById(R.id.spinner_user);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, user);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                final Calendar calendar = Calendar.getInstance();
                calendar.set(2010,01,01);

                final YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(getActivity(),
                        new YearMonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onYearMonthSet(int year, int month) {

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);

                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");

                                setDate.setText(dateFormat.format(calendar.getTime()));
                            }
                        });
                setDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        yearMonthPickerDialog.show();

                    }
                });

                btn_generate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String Fetch_Date;
                        final String Fetch_Email;

                        Fetch_Email = spinner.getSelectedItem().toString();
                        Fetch_Date = setDate.getText().toString();
                        if (Fetch_Email.equals("") || Fetch_Email.equals("Select User")){
                            Toast.makeText(getActivity(),"Please Select Any User !",Toast.LENGTH_LONG).show();
                        }else if(Fetch_Date.equals("")){
                            Toast.makeText(getActivity(),"Please Select Any Date !",Toast.LENGTH_LONG).show();
                        }else {
                            String path = "/storage/emulated/0/iAttendance/"+""+Fetch_Email+"_"+Fetch_Date+".pdf";
                            final File file = new File(path);

                            if (!file.exists() || !file.canRead()) {
                                Toast.makeText(getActivity(),"File Not Found",Toast.LENGTH_LONG).show();
                                return;
                            }
                            new Thread(new Runnable() {


                                @Override
                                public void run() {
                                    try {

                                        Properties props = new Properties();
                                        props.setProperty("mail.transport.protocol","smtp");
                                        props.setProperty("mail.host", mailhost);
                                        props.put("mail.smtp.auth", "true");
                                        props.put("mail.smtp.port","465");
                                        props.put("mail.smtp.socketFactory.port","465");
                                        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                                        props.put("mail.smtp.socketFactory.fallback","false");
                                        props.setProperty("mail.smtp.quitwait","false");
                                        Session session = Session.getInstance(props,
                                                new javax.mail.Authenticator() {
                                                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                                                        return new javax.mail.PasswordAuthentication(
                                                                userid,password);
                                                    }
                                                });
                                        // TODO Auto-generated method stub
                                        Message message = new MimeMessage(session);
                                        BodyPart bodyPart = new MimeBodyPart();

                                        message.setFrom(new InternetAddress("iattendancedemo@gmail.com"));

                                        message.setRecipients(Message.RecipientType.TO,
                                                InternetAddress.parse(Fetch_Email));
                                        message.setSubject(subject);

                                        ((MimeBodyPart) bodyPart).setText("Hello, "+Fetch_Email+"\n"+
                                                "Here I am Sending your Monthly Attendance Report ! \n"+
                                                "Month:- "+Fetch_Date+"\n"+
                                                "Please Go With Below Attachment !");


                                        if (!"".equals(file)) {

                                            Multipart _multipart = new MimeMultipart();
                                            BodyPart messageBodyPart = new MimeBodyPart();
                                            DataSource source = new FileDataSource(file);
                                            _multipart.addBodyPart(bodyPart);

                                            messageBodyPart.setDataHandler(new DataHandler(source));
                                            messageBodyPart.setFileName(""+Fetch_Email+"_"+Fetch_Date+".pdf");

                                            _multipart.addBodyPart(messageBodyPart);
                                            message.setContent(_multipart);
                                        }
                                        Transport.send(message);

                                    } catch (MessagingException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }).start();
                            Toast.makeText(getActivity(),"Mail Sent",Toast.LENGTH_LONG).show();



                        }
                    }
                });
                dialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
}
