package com.attendancesystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attendancesystem.Helper.GraphicOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.L;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class Face_Take_Attendance extends Fragment {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    //Button capture_image
    FloatingActionButton capture_image;
    //String to Store Email

    //Saving Image
    Bitmap bitmap;
    //Constant Object
    constants constants;
    Uri downloadUrl;

    int t;
    //
    ProgressDialog dialog;

    //Storage
    DatabaseReference Ref_downloadURL;
    StorageReference photo;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    DatabaseReference Land_Map_rightEye;
    DatabaseReference Land_Map_leftEye;
    DatabaseReference Land_Map_rightEar;
    DatabaseReference Land_Map_leftEar;
    DatabaseReference Land_Map_rightCheek;
    DatabaseReference Land_Map_leftCheek;
    DatabaseReference Land_Map_mouthBottom;
    DatabaseReference Land_Map_mouthLeft;
    DatabaseReference Land_Map_mouthRight;
    DatabaseReference Land_Map_noseBase;

    //LandMarks
    Map<String, Float> Map_rightEye;
    Map<String, Float> Map_leftEye;
    Map<String, Float> Map_leftEar;
    Map<String, Float> Map_rightEar;
    Map<String, Float> Map_leftCheek;
    Map<String, Float> Map_rightCheek = new HashMap<>();
    Map<String, Float> Map_mouthBottom;
    Map<String, Float> Map_mouthLeft;
    Map<String, Float> Map_mouthRight;
    Map<String, Float> Map_noseBase;

    //LandMarks
    public float rightEye_Img_X;
    public float rightEye_Img_Y;
    public float leftEye_Img_X;
    public float leftEye_Img_Y;
    public float leftEar_Img_X;
    public float leftEar_Img_Y;
    public float rightEar_Img_X;
    public float rightEar_Img_Y;
    public float leftCheek_Img_X;
    public float leftCheek_Img_Y;
    public float rightCheek_Img_X;
    public float rightCheek_Img_Y;
    public float mouthBottom_Img_X;
    public float mouthBottom_Img_Y;
    public float mouthLeft_Img_X;
    public float mouthLeft_Img_Y;
    public float mouthRight_Img_X;
    public float mouthRight_Img_Y;
    public float noseBase_Img_X;
    public float noseBase_Img_Y;
    //

    //Attendance Data
    Map<String, String> Attendance_data;
    DatabaseReference A_Data, reference; //A_Data for Attendance Data, Reference for TOTAL PRESENT
    //
    //Date Function
    Date date;
    int GAP = 35;
    String key;
    int Total_count;
    int currentIndex;
    Boolean flag = false;
    String UserFacedemail = "";
    Map<String, Double> map1;

    //End
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_face_fragment_initial_capture, container, false);

        cameraView = (CameraView) view.findViewById(R.id.camera_view);
        capture_image = (FloatingActionButton) view.findViewById(R.id.capture_image);
        graphicOverlay = (GraphicOverlay) view.findViewById(R.id.graphic_overlay);

        dialog = new ProgressDialog(getActivity());

        //Firebase
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Ref_downloadURL = database.getReference("Attendance").child("Register");
        reference = database.getReference("Attendance").child("Register");

        photo = storageReference.child("Face Detection Photo");

        //Landmarks Hash map
        Map_rightEye = new HashMap<>();
        Map_leftEye = new HashMap<>();
        Map_leftEar = new HashMap<>();
        Map_rightEar = new HashMap<>();
        Map_leftCheek = new HashMap<>();
        Map_rightCheek = new HashMap<>();
        Map_mouthBottom = new HashMap<>();
        Map_mouthLeft = new HashMap<>();
        Map_mouthRight = new HashMap<>();
        Map_noseBase = new HashMap<>();
        //Landmarks End Hash Map
        //
        Attendance_data = new HashMap<>();

        //Firebase Coordinates Database Reference
        Land_Map_rightEye = database.getReference("Attendance").child("Register");
        Land_Map_leftEye = database.getReference("Attendance").child("Register");
        Land_Map_rightEar = database.getReference("Attendance").child("Register");
        Land_Map_leftEar = database.getReference("Attendance").child("Register");
        Land_Map_rightCheek = database.getReference("Attendance").child("Register");
        Land_Map_leftCheek = database.getReference("Attendance").child("Register");
        Land_Map_mouthBottom = database.getReference("Attendance").child("Register");
        Land_Map_mouthLeft = database.getReference("Attendance").child("Register");
        Land_Map_mouthRight = database.getReference("Attendance").child("Register");
        Land_Map_noseBase = database.getReference("Attendance").child("Register");
        //
        //
        A_Data = database.getReference("Attendance");
        //
        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();
                //graphicOverlay.clear();
                // dialog.setTitle("Please Wait");
                // dialog.setMessage("iAttendance is processsing Your Data");
                // dialog.setCancelable(false);
                // dialog.show();

            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);

                if (bitmap != null) {

                    Toast.makeText(getActivity(), "Image Captured !", Toast.LENGTH_LONG).show();


                    //Spots Alert Dialog
                    dialog.setTitle("Please Wait");
                    dialog.setMessage("iAttendance is processsing Your Data");
                    dialog.setCancelable(false);
                    dialog.show();

                    cameraView.setEnabled(false);

                    try {
                        runFaceDetector(bitmap);
                    } catch (Exception e) {
                        Log.e("ERROR", "" + e);
                    }
                }
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
        return view;
    }

    private void runFaceDetector(Bitmap bitmap) {


        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                        processFaceResult(firebaseVisionFaces);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void processFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {

        int count = 0;
        for (FirebaseVisionFace face : firebaseVisionFaces) {

            Rect bounds = face.getBoundingBox();


            //Landmarks
            FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
            rightEye_Img_X = rightEye.getPosition().getX();
            rightEye_Img_Y = rightEye.getPosition().getY();


            FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
            leftEye_Img_X = leftEye.getPosition().getX();
            leftEye_Img_Y = leftEye.getPosition().getY();

            FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
            leftEar_Img_X = leftEar.getPosition().getX();
            leftEar_Img_Y = leftEar.getPosition().getY();

            FirebaseVisionFaceLandmark rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
            rightEar_Img_X = rightEar.getPosition().getX();
            rightEar_Img_Y = rightEar.getPosition().getY();

            FirebaseVisionFaceLandmark leftCheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
            leftCheek_Img_X = leftCheek.getPosition().getX();
            leftCheek_Img_Y = leftCheek.getPosition().getY();

            FirebaseVisionFaceLandmark rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
            rightCheek_Img_X = rightCheek.getPosition().getX();
            rightCheek_Img_Y = rightCheek.getPosition().getY();

            FirebaseVisionFaceLandmark mouth_b = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM);
            mouthBottom_Img_X = mouth_b.getPosition().getX();
            mouthBottom_Img_Y = mouth_b.getPosition().getY();

            FirebaseVisionFaceLandmark mouth_l = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT);
            mouthLeft_Img_X = mouth_l.getPosition().getX();
            mouthLeft_Img_Y = mouth_l.getPosition().getY();

            FirebaseVisionFaceLandmark mouth_r = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT);
            mouthRight_Img_X = mouth_r.getPosition().getX();
            mouthRight_Img_Y = mouth_r.getPosition().getY();

            FirebaseVisionFaceLandmark noseBase = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
            noseBase_Img_X = noseBase.getPosition().getX();
            noseBase_Img_Y = noseBase.getPosition().getY();


            //graphicOverlay.add(rect);

            count++;

        }

        if (count > 0) {
            //   upload(bitmap);
            Toast.makeText(getActivity(), String.format("Face Detected"), Toast.LENGTH_LONG).show();

            compare(rightEye_Img_X, rightEye_Img_Y,
                    leftEye_Img_X, leftEye_Img_Y,
                    leftEar_Img_X, leftEar_Img_Y,
                    rightEar_Img_X, rightEar_Img_Y,
                    leftCheek_Img_X, leftCheek_Img_Y,
                    rightCheek_Img_X, rightCheek_Img_Y,
                    mouthBottom_Img_X, mouthBottom_Img_Y,
                    mouthLeft_Img_X, mouthLeft_Img_Y,
                    mouthRight_Img_X, mouthRight_Img_Y,
                    noseBase_Img_X, noseBase_Img_Y);

        } else {
            Toast.makeText(getActivity(), String.format("Face Not Detected..! Please Try Again..!"), Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }

    //CURRENT PHOTO DATA
    private void compare(final float CrightEye_img_x, final float CrightEye_img_y,
                         final float CleftEye_img_x, final float CleftEye_img_y,
                         final float CleftEar_img_x, final float CleftEar_img_y,
                         final float CrightEar_img_x, final float CrightEar_img_y,
                         final float CleftCheek_img_x, final float CleftCheek_img_y,
                         final float CrightCheek_img_x, final float CrightCheek_img_y,
                         final float CmouthBottom_img_x, final float CmouthBottom_img_y,
                         final float CmouthLeft_img_x, final float CmouthLeft_img_y,
                         final float CmouthRight_img_x, final float CmouthRight_img_y,
                         final float CnoseBase_img_x, final float CnoseBase_img_y) {

        Total_count = 0;
        t = 0;
        Log.d("IMAGE", "Inside Compare");
        flag = false;
        UserFacedemail = "";
        //To Fetch Data Whose Usertype is User
        Query query = FirebaseDatabase.getInstance().getReference("Attendance")
                .child("Register").orderByChild("usertype").equalTo("User");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("IMAGE", "Inside Compare Query");
                Log.d("IMAGE", "User's Data: " + dataSnapshot.getValue().toString());


                double rightEyeImgX, o_rightEyeImgX;
                double rightEyeImgY, o_rightEyeImgY;
                double leftEyeImgX, o_leftEyeImgX;
                double leftEyeImgY, o_leftEyeImgY;
                double rightEarImgX, o_rightEarImgX;
                double rightEarImgY, o_rightEarImgY;
                double leftEarImgX, o_leftEarImgX;
                double leftEarImgY, o_leftEarImgY;
                double rightCheekImgX, o_rightCheekImgX;
                double rightCheekImgY, o_rightCheekImgY;
                double leftCheekImgX, o_leftCheekImgX;
                double leftCheekImgY, o_leftCheekImgY;
                double mouthBottomImgX, o_mouthBottomImgX;
                double mouthBottomImgY, o_mouthBottomImgY;
                double mouthLeftImgX, o_mouthLeftImgX;
                double mouthLeftImgY, o_mouthLeftImgY;
                double mouthRightImgX, o_mouthRightImgX;
                double mouthRightImgY, o_mouthRightImgY;
                double noseBaseImgX, o_noseBaseImgX;
                double noseBaseImgY, o_noseBaseImgY;

                int count = 0;
                for (Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator(); it.hasNext(); ) {
                    DataSnapshot snapshot = it.next();

                    final String email = snapshot.child("email").getValue().toString();
                    final String uname = snapshot.child("name").getValue().toString();

                    int i;


                    //                    Log.i("TRUE",snapshot.child("Landmarks").getValue().toString());
/*
                    if(!snapshot.exists()) {
                        Log.i("TRUE","TRUE INSIDE CONTINUE");
                        continue;
                    }*/

                    //DATABASE DATA


                    if (!snapshot.child("totalpresent").exists()) {
                        continue;
                    }
                    if (snapshot.child("Landmarks").getChildrenCount() == 0) {
                        continue;
                    }
                    Log.v("FaceDetect", "GETCHILDRENCOUNT: " + snapshot.getChildrenCount());
                    for (i = 1; i < dataSnapshot.getChildrenCount(); i++) {
                        Log.v("FaceDetect", "INSID FOR LOOP GETCHILDRENCOUNT: " + snapshot.getChildrenCount());
                        map1 = (Map) snapshot.child("Landmarks").child("Right_Eye").getValue();
                        rightEyeImgX = map1.get("RightEye_Img_X");
                        rightEyeImgY = map1.get("RightEye_Img_Y");

                        Map<String, Double> map2 = (Map) snapshot.child("Landmarks").child("Left_Eye").getValue();
                        leftEyeImgX = map2.get("LeftEye_Img_X");
                        leftEyeImgY = map2.get("LeftEye_Img_Y");

                        Map<String, Double> map3 = (Map) snapshot.child("Landmarks").child("Right_Ear").getValue();
                        rightEarImgX = map3.get("RightEar_Img_X");
                        rightEarImgY = map3.get("RightEar_Img_Y");

                        Map<String, Double> map4 = (Map) snapshot.child("Landmarks").child("Left_Ear").getValue();
                        leftEarImgX = map4.get("LeftEar_Img_X");
                        leftEarImgY = map4.get("LeftEar_Img_Y");

                        Map<String, Double> map5 = (Map) snapshot.child("Landmarks").child("Left_Cheek").getValue();
                        leftCheekImgX = map5.get("LeftCheek_Img_X");
                        leftCheekImgY = map5.get("LeftCheek_Img_Y");

                        Map<String, Double> map6 = (Map) snapshot.child("Landmarks").child("Right_Cheek").getValue();
                        rightCheekImgX = map6.get("RightCheek_Img_X");
                        rightCheekImgY = map6.get("RightCheek_Img_Y");

                        Map<String, Double> map7 = (Map) snapshot.child("Landmarks").child("Mouth_Left").getValue();
                        mouthLeftImgX = map7.get("MouthLeft_Img_X");
                        mouthLeftImgY = map7.get("MouthLeft_Img_Y");

                        Map<String, Double> map8 = (Map) snapshot.child("Landmarks").child("Mouth_Right").getValue();
                        mouthRightImgX = map8.get("MouthRight_Img_X");
                        mouthRightImgY = map8.get("MouthRight_Img_Y");

                        Map<String, Double> map9 = (Map) snapshot.child("Landmarks").child("Mouth_Bottom").getValue();
                        mouthBottomImgX = map9.get("MouthBottom_Img_X");
                        mouthBottomImgY = map9.get("MouthBottom_Img_Y");

                        Map<String, Double> map10 = (Map) snapshot.child("Landmarks").child("Nose_Base").getValue();
                        noseBaseImgX = map10.get("NoseBase_Img_X");
                        noseBaseImgY = map10.get("NoseBase_Img_Y");


                        if (rightEyeImgX == 0 || rightEyeImgY == 0
                                || leftEyeImgX == 0 || leftEyeImgY == 0
                                || rightEarImgX == 0 || rightEarImgY == 0
                                || leftEarImgX == 0 || leftEarImgY == 0
                                || leftCheekImgX == 0 || leftCheekImgY == 0
                                || rightCheekImgX == 0 || rightCheekImgY == 0
                                || mouthLeftImgX == 0 || mouthLeftImgY == 0
                                || mouthRightImgX == 0 || mouthRightImgY == 0
                                || mouthBottomImgX == 0 || mouthBottomImgY == 0
                                || noseBaseImgX == 0 || noseBaseImgY == 0) {

                            Toast.makeText(getActivity(), "Please Put Face Again", Toast.LENGTH_LONG).show();
                        } else {


                            Log.i("DATA", "" + email + "Data Current " + "Right Eye X" + CrightEye_img_x + "Right Eye Y" + CrightEye_img_y
                                    + "Left Eye X" + CleftEye_img_x + "Left Eye Y" + CleftEye_img_y
                                    + "Right Ear X" + CrightEar_img_x + "Right Ear Y" + CrightEar_img_y
                                    + "Left Ear X" + CleftEar_img_x + "Left Ear Y" + CleftEar_img_y
                                    + "Right Cheek X" + CrightCheek_img_x + "Right Cheek Y" + CrightCheek_img_y);
                            Log.i("DATA", "" + email + "Database " + "Right Eye X" + rightEyeImgX + "Right Eye Y" + rightEyeImgY
                                    + "Left Eye X" + leftEyeImgX + "Left Eye Y" + leftEyeImgY
                                    + "Right Ear X" + rightEarImgX + "Right Ear Y" + rightEarImgY
                                    + "Left Ear X" + leftEarImgX + "Left Ear Y" + leftEarImgY
                                    + "Right Cheek X" + rightCheekImgX + "Right Cheek Y" + rightCheekImgY);

                            o_rightEyeImgX = Math.abs(CrightEye_img_x - rightEyeImgX);
                            o_rightEyeImgY = Math.abs(CrightEye_img_y - rightEyeImgY);
                            o_leftEyeImgX = Math.abs(CleftEye_img_x - leftEyeImgX);
                            o_leftEyeImgY = Math.abs(CleftEye_img_y - leftEyeImgY);
                            o_rightEarImgX = Math.abs(CrightEar_img_x - rightEarImgX);
                            o_rightEarImgY = Math.abs(CrightEar_img_y - rightEarImgY);
                            o_leftEarImgX = Math.abs(CleftEar_img_x - leftEarImgX);
                            o_leftEarImgY = Math.abs(CleftEar_img_y - leftEarImgY);
                            o_rightCheekImgX = Math.abs(CrightCheek_img_x - rightCheekImgX);
                            o_rightCheekImgY = Math.abs(CrightCheek_img_y - rightCheekImgY);
                            o_leftCheekImgX = Math.abs(CleftCheek_img_x - leftCheekImgX);
                            o_leftCheekImgY = Math.abs(CleftCheek_img_y - leftCheekImgY);
                            o_mouthRightImgX = Math.abs(CmouthRight_img_x - mouthRightImgX);
                            o_mouthRightImgY = Math.abs(CmouthRight_img_y - mouthRightImgY);
                            o_mouthLeftImgX = Math.abs(CmouthLeft_img_x - mouthLeftImgX);
                            o_mouthLeftImgY = Math.abs(CmouthLeft_img_y - mouthLeftImgY);
                            o_mouthBottomImgX = Math.abs(CmouthBottom_img_x - mouthBottomImgX);
                            o_mouthBottomImgY = Math.abs(CmouthBottom_img_y - mouthBottomImgY);
                            o_noseBaseImgX = Math.abs(CnoseBase_img_x - noseBaseImgX);
                            o_noseBaseImgY = Math.abs(CnoseBase_img_y - noseBaseImgY);

                            if (o_rightEyeImgX == 0 || o_rightEyeImgY == 0
                                    || o_leftEyeImgX == 0 || o_leftEyeImgY == 0
                                    || o_rightEarImgX == 0 || o_rightEarImgY == 0
                                    || o_leftEarImgX == 0 || o_leftEarImgY == 0
                                    || o_rightCheekImgX == 0 || o_rightCheekImgY == 0
                                    || o_leftCheekImgX == 0 || o_leftCheekImgY == 0
                                    || o_mouthBottomImgX == 0 || o_mouthBottomImgY == 0
                                    || o_mouthRightImgX == 0 || o_mouthRightImgY == 0
                                    || o_mouthLeftImgX == 0 || o_mouthLeftImgY == 0
                                    || o_noseBaseImgX == 0 || o_noseBaseImgY == 0) {
                                Toast.makeText(getActivity(), "Please Put Face Again", Toast.LENGTH_LONG).show();
                            } else {

                                Total_count++;

                                if (o_rightEyeImgX <= GAP && o_rightEyeImgY <= GAP
                                        && o_leftEyeImgX <= GAP && o_leftEyeImgY <= GAP
                                        && o_rightEarImgX <= GAP && o_rightEarImgY <= GAP
                                        && o_leftEarImgX <= GAP && o_leftEarImgY <= GAP
                                        && o_rightCheekImgX <= GAP && o_rightCheekImgY <= GAP
                                        && o_leftCheekImgX <= GAP && o_leftCheekImgY <= GAP
                                        && o_mouthBottomImgX <= GAP && o_mouthBottomImgY <= GAP
                                        && o_mouthRightImgX <= GAP && o_mouthRightImgY <= GAP
                                        && o_mouthLeftImgX <= GAP && o_mouthLeftImgY <= GAP
                                        && o_noseBaseImgX <= GAP && o_noseBaseImgY <= GAP) {
                                    flag = true;
                                    UserFacedemail = email;
                                    Log.i("DATA", email);
                                    //Database Insert
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
                                    Query query = databaseReference.child("Register").orderByChild("email").equalTo(email);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                key = snapshot.getKey();
                                                //For Date
                                                date = new Date();
                                                //Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                                                SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
                                                String stringdate = dt.format(date);
                                                //For Time
                                                String format = "hh:mm:ss a";
                                                String timeZone = "IST";
                                                SimpleDateFormat sdf = new SimpleDateFormat(format);
                                                timeZone = Calendar.getInstance().getTimeZone().getID();

                                                // create SimpleDateFormat object with input format
                                                // default system timezone if passed null or empty
                                                if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
                                                    timeZone = Calendar.getInstance().getTimeZone().getID();
                                                }
                                                // set timezone to SimpleDateFormat
                                                sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
                                                // return Date in required format with timezone as String

                                                //
                                                Attendance_data.put("email", email);
                                                Attendance_data.put("name", uname);
                                                Attendance_data.put("date", stringdate);
                                                Attendance_data.put("time", sdf.format(date));

                                                //A_Data.child(snapshot.getKey()).child("Attendance_Data").child(stringdate).setValue(Attendance_data);


                                                A_Data.child("Attendance_Data").push().setValue(Attendance_data);
                                                //reference.child(key).setValue(constants.TOTAL_PRESENT+1);
                                                //constants.TOTAL_PRESENT = constants.TOTAL_PRESENT +1;
                                                           Total_count = Integer.parseInt(snapshot.child("totalpresent").getValue().toString());
                                                           Total_count += 1;
                                                //Total Present + 1
                                                reference.child(key).child("totalpresent").setValue(String.valueOf(Total_count));
                                                //Toast.makeText(getActivity(),status.toString(),Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                                //flag = true;
                                                Log.i("MSG", "" + flag);
                                                //toastflag(flag, email);
                                                break;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    //Database Insert End
                                    break;
                                } else {

                                    Log.i("MSG", "FALSE");
                                    Log.i("MSG", "" + flag);
                                    if (!flag)
                                        flag = false;
                                    dialog.dismiss();
                                    //toastflag(flag, email);

                                    break;
                                }
                            }

                        }
                    }
                    if (flag) {
                        Toast.makeText(getActivity(), "User found " + UserFacedemail, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Not found ", Toast.LENGTH_LONG).show();
                    }
                    //For End
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

   /* public void toastflag(Boolean flag, String email) {


        Log.i("DATA", "" + Total_count + "" + flag);


        if (flag.equals(true)) {
            Toast.makeText(getActivity(), "Attendance taken for " + " " + email, Toast.LENGTH_LONG).show();
        } else if (flag.equals(false)) {
            t++;
            Log.i("DATA", "Inside False" + t);
        }
        Log.i("DATA", "Final T:" + t);

    }*/

    @Override
    public void onPause() {
        super.onPause();

        cameraView.stop();
    }

    @Override
    public void onResume() {
        super.onResume();

        cameraView.start();
    }
}
