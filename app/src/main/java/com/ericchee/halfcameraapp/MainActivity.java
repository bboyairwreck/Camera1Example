package com.ericchee.halfcameraapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import static com.ericchee.halfcameraapp.WaveCameraManager.hasCameraPermission;
import static com.ericchee.halfcameraapp.WaveCameraManager.requestCameraPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasCameraPermission(this)) {
            requestCameraPermission(this, 300);
            return;
        }

        WaveCameraManager cameraPreview = new WaveCameraManager(this, WaveCameraManager.getFrontCameraInstance());
        ViewGroup cameraContainer = (ViewGroup) findViewById(R.id.cameraContainer);
        cameraContainer.addView(cameraPreview);
    }
}
