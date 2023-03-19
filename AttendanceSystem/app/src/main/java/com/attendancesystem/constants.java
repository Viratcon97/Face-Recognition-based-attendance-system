package com.attendancesystem;

public class constants {

    //User Types String Array


    //Email


    //
    static int TOTAL_PRESENT;
    static String[] data = {"Admin","User","Operator"};
    static String[] category = {"Select Category","Category A","Category B"};

    static String[] reasons = {"Missed Entry","Registered, But Face Not Recognized"
            ,"Face Recognized But Showing Absent","Other Reason"};
    //Validation Pattern
    static String emailPattern ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";//Regex Pattern
    static String mobilePattern = "^[6-9][0-9]{9}$"; //Regex Pattern
    static String fullnamePattern = "^[gradientbackground-zA-Z\\s]*$"; //Regex Pattern

    //Start Coordinates Float
    public Float rightEye_Img1_X,rightEye_Img2_X,rightEye_Img3_X;
    public Float rightEye_Img1_Y,rightEye_Img2_Y,rightEye_Img3_Y;
    public Float leftEye_Img1_X,leftEye_Img2_X,leftEye_Img3_X;
    public Float leftEye_Img1_Y,leftEye_Img2_Y,leftEye_Img3_Y;
    public Float leftEar_Img1_X,leftEar_Img2_X,leftEar_Img3_X;
    public Float leftEar_Img1_Y,leftEar_Img2_Y,leftEar_Img3_Y;
    public Float rightEar_Img1_X,rightEar_Img2_X,rightEar_Img3_X;
    public Float rightEar_Img1_Y,rightEar_Img2_Y,rightEar_Img3_Y;
    public Float leftCheek_Img1_X,leftCheek_Img2_X,leftCheek_Img3_X;
    public Float leftCheek_Img1_Y,leftCheek_Img2_Y,leftCheek_Img3_Y;
    public Float rightCheek_Img1_X,rightCheek_Img2_X,rightCheek_Img3_X;
    public Float rightCheek_Img1_Y,rightCheek_Img2_Y,rightCheek_Img3_Y;
    public Float mouthBottom_Img1_X,mouthBottom_Img2_X,mouthBottom_Img3_X;
    public Float mouthBottom_Img1_Y,mouthBottom_Img2_Y,mouthBottom_Img3_Y;
    public Float mouthLeft_Img1_X,mouthLeft_Img2_X,mouthLeft_Img3_X;
    public float mouthLeft_Img1_Y,mouthLeft_Img2_Y,mouthLeft_Img3_Y;
    public Float mouthRight_Img1_X,mouthRight_Img2_X,mouthRight_Img3_X;
    public Float mouthRight_Img1_Y,mouthRight_Img2_Y,mouthRight_Img3_Y;
    public Float noseBase_Img1_X,noseBase_Img2_X,noseBase_Img3_X;
    public Float noseBase_Img1_Y,noseBase_Img2_Y,noseBase_Img3_Y;
    //End Coordinates FLoat




}
