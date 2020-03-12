package com.example.faceandsmiledetection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button openbutton;
    private final static int REQUEST_IMAGE_CATURE=123;
    private FirebaseVisionImage image;
    private FirebaseVisionFaceDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        openbutton=findViewById(R.id.camera_button);
        openbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null)
                {
                    startActivityForResult(intent,REQUEST_IMAGE_CATURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_IMAGE_CATURE && resultCode==RESULT_OK)
        {
            Bundle bundle=data.getExtras();
            Bitmap bitmap= (Bitmap) bundle.get("data");
            detectFace(bitmap);
        }
    }
    private void detectFace(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .setMinFaceSize(0.15f).enableTracking().build();
        try {
            image=FirebaseVisionImage.fromBitmap(bitmap);
            detector= FirebaseVision.getInstance().getVisionFaceDetector(highAccuracyOpts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                String resultText="";
                int i=1;
                for (FirebaseVisionFace face:firebaseVisionFaces)
                {
                    if(face.getSmilingProbability()*100>=80) {

                        resultText = resultText.concat("\n" + i + ".").concat("\nSmile:" +
                                face.getSmilingProbability() * 100 + "%").concat("LeftEye" +
                                face.getLeftEyeOpenProbability() * 100 + "%").concat("\nRigtEye" +
                                face.getRightEyeOpenProbability() * 100 + "%").concat("Mood:Happy");
                    }
                    else if((face.getSmilingProbability()*100)<=80 && (face.getSmilingProbability()*100)>=50) {

                        resultText = resultText.concat("\n" + i + ".").concat("\nSmile:" +
                                face.getSmilingProbability() * 100 + "%").concat("LeftEye" +
                                face.getLeftEyeOpenProbability() * 100 + "%").concat("\nRigtEye" +
                                face.getRightEyeOpenProbability() * 100 + "%").concat("Mood:Not Really");
                    }
                    else if((face.getSmilingProbability()*100)<=50 && (face.getSmilingProbability()*100)>=0) {

                        resultText = resultText.concat("\n" + i + ".").concat("\nSmile:" +
                                face.getSmilingProbability() * 100 + "%").concat("LeftEye" +
                                face.getLeftEyeOpenProbability() * 100 + "%").concat("\nRigtEye" +
                                face.getRightEyeOpenProbability() * 100 + "%").concat("Mood:Sad");
                    }
                    i++;

                }
                if(firebaseVisionFaces.size()==0)
                {
                    Toast.makeText(MainActivity.this, "No Faces...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Bundle bundle=new Bundle();
                    bundle.putString(LOCFaceDetection.RESULT_TEXT,resultText);
                    DialogFragment resultdialog=new ResultDialog();
                    resultdialog.setArguments(bundle);
                    resultdialog.setCancelable(false);
                    resultdialog.show(getSupportFragmentManager(),LOCFaceDetection.RESULT_DIALOG);
                }
            }
        });
    }
}
