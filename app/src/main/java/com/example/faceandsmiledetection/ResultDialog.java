package com.example.faceandsmiledetection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ResultDialog extends DialogFragment {
    private Button okButton;
    private TextView resultTV;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_result,container,false);
        String result="";
        okButton=view.findViewById(R.id.result_ok_button);
        resultTV=view.findViewById(R.id.result_text_view);
        Bundle bundle=getArguments();
        result=bundle.getString(LOCFaceDetection.RESULT_TEXT);
        resultTV.setText(result);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
}
