package com.test.opencvtest;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = MainActivity.class.getSimpleName();

    private JavaCameraView openCvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        openCvView = (JavaCameraView) findViewById(R.id.opencv);
        openCvView.setCvCameraViewListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO is this and the static loadLibrary doing the same thing?
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (openCvView != null) {
            openCvView.disableView();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");
                    openCvView.enableView();
                    break;

                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}