package com.attendancesystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attendancesystem.Helper.GraphicOverlay;
import com.attendancesystem.Model.registerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class Face_Initial_Capture extends Fragment {

    CameraView cameraView;
    GraphicOverlay graphicOverlay;
    //Button capture_image
    FloatingActionButton capture_image;
    //String to Store Email
    String email;
    //Saving Image
    Bitmap bitmap;
    //Constant Object
    constants constants;
    Uri downloadUrl;

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
    Map<String,Float> Map_rightEye;
    Map<String,Float> Map_leftEye;
    Map<String,Float> Map_leftEar;
    Map<String,Float> Map_rightEar;
    Map<String,Float> Map_leftCheek;
    Map<String,Float> Map_rightCheek = new HashMap<>();
    Map<String,Float> Map_mouthBottom;
    Map<String,Float> Map_mouthLeft;
    Map<String,Float> Map_mouthRight;
    Map<String,Float> Map_noseBase;

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

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_face_fragment_initial_capture, container, false);

        email = getArguments().getString("email");
        cameraView = (CameraView)view.findViewById(R.id.camera_view);
        capture_image = (FloatingActionButton)view.findViewById(R.id.capture_image);
        graphicOverlay = (GraphicOverlay)view.findViewById(R.id.graphic_overlay);

        dialog = new ProgressDialog(getActivity());

        //Firebase
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Ref_downloadURL = database.getReference("Attendance").child("Register");

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

        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
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
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);

                if(bitmap!=null){

                    Toast.makeText(getActivity(),"Image Captured !",Toast.LENGTH_LONG).show();

                    //Spots Alert Dialog
                    dialog.setTitle("Please Wait");
                    dialog.setMessage("iAttendance is processsing Your Data");
                    dialog.setCancelable(false);
                    dialog.show();

                   cameraView.setEnabled(false);

                    try {
                        runFaceDetector(bitmap);
                    }
                    catch (Exception e){
                        Log.e("ERROR",""+e);
                    }
                }else {
                    Toast.makeText(getActivity(),"Image Not Captured ! Please Try Again !",Toast.LENGTH_LONG).show();

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
        for(FirebaseVisionFace face : firebaseVisionFaces){

            Rect bounds = face.getBoundingBox();

            float rightEyeOpenProb = face.getRightEyeOpenProbability();
            float leftEyeOpenProb = face.getLeftEyeOpenProbability();

            Log.i("BOUNDS","R:"+rightEyeOpenProb);
            Log.i("BOUNDS","L:"+leftEyeOpenProb);

          //  if(rightEyeOpenProb == )
            //Landmarks
            FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
            rightEye_Img_X = rightEye.getPosition().getX();
            rightEye_Img_Y = rightEye.getPosition().getY();

            Map_rightEye.put("RightEye_Img_X",rightEye_Img_X);
            Map_rightEye.put("RightEye_Img_Y",rightEye_Img_Y);

            FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
            leftEye_Img_X = leftEye.getPosition().getX();
            leftEye_Img_Y = leftEye.getPosition().getY();

            Map_leftEye.put("LeftEye_Img_X",leftEye_Img_X);
            Map_leftEye.put("LeftEye_Img_Y",leftEye_Img_Y);

            FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
            leftEar_Img_X = leftEar.getPosition().getX();
            leftEar_Img_Y = leftEar.getPosition().getY();

            Map_leftEar.put("LeftEar_Img_X",leftEar_Img_X);
            Map_leftEar.put("LeftEar_Img_Y",leftEar_Img_Y);

            FirebaseVisionFaceLandmark rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
            rightEar_Img_X = rightEar.getPosition().getX();
            rightEar_Img_Y = rightEar.getPosition().getY();

            Map_rightEar.put("RightEar_Img_X",rightEar_Img_X);
            Map_rightEar.put("RightEar_Img_Y",rightEar_Img_Y);

            FirebaseVisionFaceLandmark leftCheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
            leftCheek_Img_X = leftCheek.getPosition().getX();
            leftCheek_Img_Y = leftCheek.getPosition().getY();

            Map_leftCheek.put("LeftCheek_Img_X",leftCheek_Img_X);
            Map_leftCheek.put("LeftCheek_Img_Y",leftCheek_Img_Y);

            FirebaseVisionFaceLandmark rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
            rightCheek_Img_X = rightCheek.getPosition().getX();
            rightCheek_Img_Y = rightCheek.getPosition().getY();

            Map_rightCheek.put("RightCheek_Img_X",rightCheek_Img_X);
            Map_rightCheek.put("RightCheek_Img_Y",rightCheek_Img_Y);

            FirebaseVisionFaceLandmark mouth_b = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM);
            mouthBottom_Img_X = mouth_b.getPosition().getX();
            mouthBottom_Img_Y = mouth_b.getPosition().getY();

            Map_mouthBottom.put("MouthBottom_Img_X",mouthBottom_Img_X);
            Map_mouthBottom.put("MouthBottom_Img_Y",mouthBottom_Img_Y);

            FirebaseVisionFaceLandmark mouth_l = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT);
            mouthLeft_Img_X = mouth_l.getPosition().getX();
            mouthLeft_Img_Y = mouth_l.getPosition().getY();

            Map_mouthLeft.put("MouthLeft_Img_X",mouthLeft_Img_X);
            Map_mouthLeft.put("MouthLeft_Img_Y",mouthLeft_Img_Y);

            FirebaseVisionFaceLandmark mouth_r = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT);
            mouthRight_Img_X = mouth_r.getPosition().getX();
            mouthRight_Img_Y = mouth_r.getPosition().getY();

            Map_mouthRight.put("MouthRight_Img_X",mouthRight_Img_X);
            Map_mouthRight.put("MouthRight_Img_Y",mouthRight_Img_Y);

            FirebaseVisionFaceLandmark noseBase = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE);
            noseBase_Img_X = noseBase.getPosition().getX();
            noseBase_Img_Y = noseBase.getPosition().getY();

            Map_noseBase.put("NoseBase_Img_X",noseBase_Img_X);
            Map_noseBase.put("NoseBase_Img_Y",noseBase_Img_Y);

            //graphicOverlay.add(rect);

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance")
                    .child("Register");

            //Query To Find Key To Set Particular Data
            databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
            Query query = databaseReference.child("Register").orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                        //   databaseReference.child("Register").child(snapshot.getKey()).setValue(adapter);

                        Land_Map_rightEye.child(snapshot.getKey()).child("Landmarks").child("Right_Eye").setValue(Map_rightEye);
                        Land_Map_leftEye.child(snapshot.getKey()).child("Landmarks").child("Left_Eye").setValue(Map_leftEye);
                        Land_Map_leftEar.child(snapshot.getKey()).child("Landmarks").child("Left_Ear").setValue(Map_leftEar);
                        Land_Map_rightEar.child(snapshot.getKey()).child("Landmarks").child("Right_Ear").setValue(Map_rightEar);
                        Land_Map_leftCheek.child(snapshot.getKey()).child("Landmarks").child("Left_Cheek").setValue(Map_leftCheek);
                        Land_Map_rightCheek.child(snapshot.getKey()).child("Landmarks").child("Right_Cheek").setValue(Map_rightCheek);
                        Land_Map_mouthBottom.child(snapshot.getKey()).child("Landmarks").child("Mouth_Bottom").setValue(Map_mouthBottom);
                        Land_Map_mouthLeft.child(snapshot.getKey()).child("Landmarks").child("Mouth_Left").setValue(Map_mouthLeft);
                        Land_Map_mouthRight.child(snapshot.getKey()).child("Landmarks").child("Mouth_Right").setValue(Map_mouthRight);
                        Land_Map_noseBase.child(snapshot.getKey()).child("Landmarks").child("Nose_Base").setValue(Map_noseBase);
                        reference.child(snapshot.getKey()).child("totalpresent").setValue("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(),"Key Not Found "+databaseError,Toast.LENGTH_SHORT).show();
                }
            });
            //
            count++;

        }

        if(count > 0){
            upload(bitmap);

            Toast.makeText(getActivity(),String.format("Face Detected"),Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity(),String.format("Face Not Detected"),Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(),String.format("Uploading Cancel"),Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }

    private void upload(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = photo.child(email).child("Image").putBytes(data);


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getActivity(),"Uploaded",Toast.LENGTH_LONG).show();
                sendURL(taskSnapshot);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Error"+e,Toast.LENGTH_LONG).show();


            }
        });
    }

    private void sendURL(UploadTask.TaskSnapshot taskSnapshot) {
        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
        while (!urlTask.isSuccessful());
        downloadUrl = urlTask.getResult();

        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
        Query query = databaseReference.child("Register").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Ref_downloadURL.child(snapshot.getKey()).child("Image_URL").setValue(downloadUrl.toString());
                    Toast.makeText(getActivity(), "Link Uploaded", Toast.LENGTH_LONG).show();

                   dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Key Not Found "+databaseError,Toast.LENGTH_SHORT).show();

            }
        });

        capture_image.setEnabled(true);
    }

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
