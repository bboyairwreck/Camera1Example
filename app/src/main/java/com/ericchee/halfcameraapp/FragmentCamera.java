package com.ericchee.halfcameraapp;


import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCamera extends Fragment {
    private static final String TAG = FragmentCamera.class.getSimpleName();
    public static final String FRAGMENT_TAG = "CAMERA";

    View mView;
    protected ViewGroup mCameraSurfaceViewContainer;
    protected ImageButton mFullscreenCameraButton;
    protected ImageButton mTakePictureButton;
    protected boolean isFullScreenCamera = false;
    protected WaveCameraSurfaceView mCameraView;
    protected Camera.PictureCallback mPictureCallback;
    protected View mAttachmentDetailProgressbar;

    public FragmentCamera() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_camera, container, false);

        mCameraSurfaceViewContainer = (ViewGroup) mView.findViewById(R.id.camera_surface_view_container);
        mTakePictureButton = (ImageButton) mView.findViewById(R.id.button_fragment_event_take_picture);
        mAttachmentDetailProgressbar = mView.findViewById(R.id.attachment_details_progressbar);

        setupFullscreenButton();

        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAttachmentDetailProgressbar.setVisibility(View.VISIBLE);
                Log.i(TAG, "mTakePictureButton.setOnClickListener() clicked");
                if (mCameraView != null && mPictureCallback != null) {
                    boolean successful = mCameraView.takeAPicture(mPictureCallback);

                    if (!successful) {
                        Log.e(TAG, "mTakePictureButton.setOnClickListener() - Picture could not be taken");
                    }
                } else if (mPictureCallback == null) {
                    Log.w(TAG, "mTakePictureButton.setOnClickListener() - No picture callback set");
                }
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCameraView == null) {
            addCameraSurfaceView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCameraSurfaceView();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeCameraSurfaceView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeCameraSurfaceView();
    }

    private void addCameraSurfaceView() {
        mCameraView = new WaveCameraSurfaceView(getContext(), WaveCameraSurfaceView.getBackCameraInstance());
        mCameraSurfaceViewContainer.addView(mCameraView);
    }

    public void removeCameraSurfaceView() {
        if (mCameraView != null) {
            mCameraView.clearCamera();
            mCameraView = null;
        }
        mCameraSurfaceViewContainer.removeAllViews();
    }

    private void setupFullscreenButton() {
        mFullscreenCameraButton = (ImageButton) mView.findViewById(R.id.button_fragment_event_fullscreen_camera);
        mFullscreenCameraButton.setEnabled(false);  // Todo [WIP] remove this line once full screen is enabled
        mFullscreenCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFullScreenCamera) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
                    ViewGroup.LayoutParams params = mCameraSurfaceViewContainer.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    isFullScreenCamera = true;
                } else {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                    ViewGroup.LayoutParams params = mCameraSurfaceViewContainer.getLayoutParams();
                    params.height = (int) convertDpToPixel(300, getContext());
                    isFullScreenCamera = false;
                }
            }
        });
    }

    public void setPictureCallback(Camera.PictureCallback pictureCallback) {
        this.mPictureCallback = pictureCallback;
    }

    public void refreshCamera() {
        mAttachmentDetailProgressbar.setVisibility(View.GONE);
        if (mCameraView != null) {
            mCameraView.refreshCamera();
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}
