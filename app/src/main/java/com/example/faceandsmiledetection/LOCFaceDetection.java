package com.example.faceandsmiledetection;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class LOCFaceDetection extends Application {
    public static final String RESULT_TEXT="RESULT_TEXT";
    public static final String RESULT_DIALOG="RESULT_DIALOG";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
