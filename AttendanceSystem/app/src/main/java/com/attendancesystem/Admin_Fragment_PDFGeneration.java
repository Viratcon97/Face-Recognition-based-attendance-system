package com.attendancesystem;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.android.gms.vision.L;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_Fragment_PDFGeneration extends Fragment {

    ImageView image_help, imageView_pdf,image_m_pdf,image_m_help;
    Dialog dialog;
    Spinner spinner;
    List<String> user;
    String email;
    String F__name,F__email,F__date,F__time;//Monthly Report
    String F_name, F_email, F_date, F_time,F_Total_Present; //General Report
    DatabaseReference reference;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.COURIER, 12);
    private static Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,Font.BOLD);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.admin_fragment_pdfgeneration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image_help = view.findViewById(R.id.image_general_help);
        imageView_pdf = view.findViewById(R.id.imageView_general_pdf);

        image_m_pdf = view.findViewById(R.id.imageView_month_pdf);
        image_m_help = view.findViewById(R.id.image_month_help);

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
                title.setText("GENERAL ATTENDANCE REPORT PDF"+"\n\n"+"This Will Help Admin to Generate PDF Report of Specific User. " +
                        "Admin Have to Select Particular User From The Dropdown List And Report Will be Generated in PDF Format with " +
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
                title.setText("MONTHLY ATTENDANCE REPORT PDF"+"\n\n"+"This Will Help Admin to Generate PDF Report of Specific User. " +
                        "Admin Have to Select Particular User and Particular Month with Year From The Dropdown List And " +
                        "Report Will be Generated in PDF Format with " +
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
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_admin_generate_pdf);
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
                //End
                //Button Click
                btn_generate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Date date = new Date();
                        //Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
                        final String stringdate = dt.format(date);

                        email = spinner.getSelectedItem().toString();


                        if (email.equals("") || email.equals("Select User")) {
                            Toast.makeText(getActivity(), "Please Select Any User to Send Report !", Toast.LENGTH_LONG).show();
                        } else {

                            Query query = reference.child("Attendance_Data").orderByChild("email").equalTo(email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    F_Total_Present = String.valueOf(dataSnapshot.getChildrenCount());

                                    if(F_Total_Present.equals("0")){
                                        Toast.makeText(getActivity(),"No Attendance Found",Toast.LENGTH_LONG).show();
                                    }
                                    Document doc = new Document(PageSize.A4,5,0,10,0);
                                    PdfPTable table = new PdfPTable(new float[]{1, 2, 2});

                                    Paragraph title = new Paragraph("General Attendance Report",boldFont);
                                    Paragraph h_date = new Paragraph("Date: "+stringdate,smallBold);
                                    Paragraph t_heading = new Paragraph("Details: ",subFont);

                                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.setSpacingBefore(10);
                                    table.setSpacingAfter(10);

                                    table.addCell("SR No.");
                                    table.addCell("Date");
                                    table.addCell("Time");
                                    table.setHeaderRows(1);

                                    int i = 1;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                        F_name = snapshot.child("name").getValue().toString();
                                        F_email = snapshot.child("email").getValue().toString();
                                        F_date = snapshot.child("date").getValue().toString();
                                        F_time = snapshot.child("time").getValue().toString();

                                        PdfPCell[] cells = table.getRow(0).getCells();

                                        for (int j = 0; j < cells.length; j++) {
                                            cells[j].setBackgroundColor(BaseColor.GRAY);
                                            cells[j].setPadding(5);
                                        }

                                        table.addCell(""+i);
                                        table.addCell("" + F_date);
                                        table.addCell("" + F_time);
                                        i++;
                                    }
                                    try {
                                        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/iAttendance";

                                        File dir = new File(path);
                                        if (!dir.exists())
                                            dir.mkdirs();

                                        File file = new File(dir, email+".pdf");
                                        FileOutputStream fOut = new FileOutputStream(file);

                                        PdfWriter.getInstance(doc, fOut);

                                        //open the document
                                        doc.open();

                                        Paragraph p1 = new Paragraph("Name: "+F_name);
                                        Paragraph p2 = new Paragraph("Email: "+F_email);
                                        Paragraph p3 = new Paragraph("Total Present: "+F_Total_Present);

                                        h_date.setAlignment(Paragraph.ALIGN_RIGHT);
                                        h_date.setIndentationRight(5);

                                        t_heading.setSpacingBefore(5);
                                        t_heading.setIndentationLeft(15);

                                        title.setAlignment(Paragraph.ALIGN_CENTER);
                                        title.setSpacingAfter(5);
                                        title.setSpacingBefore(5);


                                        p1.setAlignment(Paragraph.ALIGN_LEFT);
                                        p2.setAlignment(Paragraph.ALIGN_LEFT);
                                        p3.setAlignment(Paragraph.ALIGN_LEFT);
                                        t_heading.setAlignment(Paragraph.ALIGN_LEFT);
                                        //p1.setFont(paraFont);

                                        //add paragraph to document
                                        doc.add(h_date);
                                        doc.add(title);
                                        doc.add(p1);
                                        doc.add(p2);
                                        doc.add(p3);
                                        doc.add(t_heading);
                                        doc.add(table);
                                        Toast.makeText(getActivity(), "File Created", Toast.LENGTH_LONG).show();
                                    } catch (DocumentException de) {
                                        Log.e("PDFCreator", "DocumentException:" + de);
                                    } catch (IOException e) {
                                        Log.e("PDFCreator", "ioException:" + e);
                                    } finally {
                                        doc.close();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
                //End
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        image_m_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_admin_generate_month_pdf);
                dialog.setCancelable(false);

                ImageView close = (ImageView) dialog.findViewById(R.id.image_close);
                final TextView setDate = (TextView)dialog.findViewById(R.id.setDate);
                Button btn_generate = dialog.findViewById(R.id.btn_generate);


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
                spinner = dialog.findViewById(R.id.spinner_user);

                //Fetching User name
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
                //End

                btn_generate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Date date = new Date();
                        //Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
                        final String stringdate = dt.format(date);

                        //Getting Set Date
                        final String F_Date;
                        F_Date = setDate.getText().toString();

                        //PDF Table
                         // Database Date

                        //
                        email = spinner.getSelectedItem().toString();


                        if (email.equals("") || email.equals("Select User") || F_Date.equals("")) {
                            Toast.makeText(getActivity(), "Please Select Any User to Send Report !", Toast.LENGTH_LONG).show();
                        } else {


                            Query query = reference.child("Attendance_Data").orderByChild("email")
                            .equalTo(email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Document doc = new Document(PageSize.A4,5,0,10,0);
                                    PdfPTable table = new PdfPTable(new float[]{1, 2, 2});

                                    //Fetching Records

                                    Paragraph title = new Paragraph("Monthly Attendance Report",boldFont);
                                    Paragraph h_date = new Paragraph("Date: "+stringdate,smallBold);
                                    Paragraph t_heading = new Paragraph("Details: ",subFont);

                                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table.setSpacingBefore(10);
                                    table.setSpacingAfter(10);

                                    table.addCell("SR No.");
                                    table.addCell("Date");
                                    table.addCell("Time");
                                    table.setHeaderRows(1);

                                    String D_Date;
                                    int i = 1;

                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                                        D_Date = snapshot.child("date").getValue().toString();
                                        Date date1 = new Date();
                                        Date date2 = new Date();
                                        try {
                                            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(D_Date);
                                            date2 = new SimpleDateFormat("MM-yyyy").parse(F_Date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Calendar cal1 = Calendar.getInstance();
                                        cal1.setTime(date1);
                                        Calendar cal2 = Calendar.getInstance();
                                        cal2.setTime(date2);
                                        Log.i("DATA","D"+date1);
                                        Log.i("DATA","F"+date2);

                                        if((cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) &&
                                                (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))){

                                            //True means Date is Equal so Add to Table
                                            F__name = snapshot.child("name").getValue().toString();
                                            F__email = snapshot.child("email").getValue().toString();
                                            F__date = snapshot.child("date").getValue().toString();
                                            F__time = snapshot.child("time").getValue().toString();

                                            PdfPCell[] cells = table.getRow(0).getCells();

                                            for (int j = 0; j < cells.length; j++) {
                                                cells[j].setBackgroundColor(BaseColor.GRAY);
                                                cells[j].setPadding(5);
                                            }

                                            table.addCell(""+i);
                                            table.addCell("" + F__date);
                                            table.addCell("" + F__time);
                                            i++;

                                        }else {
                                            continue;
                                           // Toast.makeText(getActivity(),"Date Not Equal",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    //

                                    if(table.size() <= 1){
                                        Toast.makeText(getActivity(),"Data Not Found",Toast.LENGTH_LONG).show();
                                    }else {
                                        try {

                                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/iAttendance";

                                            Log.i("DATA__",path);

                                            File dir = new File(path);
                                            if (!dir.exists())
                                                dir.mkdirs();

                                            File file = new File(dir, email + "_"+F_Date+ ".pdf");
                                            FileOutputStream fOut = new FileOutputStream(file);

                                            PdfWriter.getInstance(doc, fOut);

                                            //open the document
                                            doc.open();

                                            Paragraph p1 = new Paragraph("Name: " + F__name);
                                            Paragraph p2 = new Paragraph("Email: " + F__email);
                                            Paragraph p3 = new Paragraph("Month: " + F_Date);

                                            h_date.setAlignment(Paragraph.ALIGN_RIGHT);
                                            h_date.setIndentationRight(5);

                                            t_heading.setSpacingBefore(5);
                                            t_heading.setIndentationLeft(15);

                                            title.setAlignment(Paragraph.ALIGN_CENTER);
                                            title.setSpacingAfter(5);
                                            title.setSpacingBefore(5);


                                            p1.setAlignment(Paragraph.ALIGN_LEFT);
                                            p2.setAlignment(Paragraph.ALIGN_LEFT);
                                            p3.setAlignment(Paragraph.ALIGN_LEFT);
                                            t_heading.setAlignment(Paragraph.ALIGN_LEFT);
                                            //p1.setFont(paraFont);

                                            //add paragraph to document
                                            doc.add(h_date);
                                            doc.add(title);
                                            doc.add(p1);
                                            doc.add(p2);
                                            doc.add(p3);
                                            doc.add(t_heading);
                                            doc.add(table);
                                            Toast.makeText(getActivity(), "File Created", Toast.LENGTH_LONG).show();
                                        } catch (DocumentException de) {
                                            Log.e("PDFCreator", "DocumentException:" + de);
                                        } catch (IOException e) {
                                            Log.e("PDFCreator", "ioException:" + e);
                                        } finally {
                                            doc.close();
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
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }
}