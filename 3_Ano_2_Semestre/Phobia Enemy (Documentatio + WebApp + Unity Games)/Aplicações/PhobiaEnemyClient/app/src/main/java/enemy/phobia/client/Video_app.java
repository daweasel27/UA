package enemy.phobia.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;




public class    Video_app extends Activity {

    protected static final String TAG = null;
    private static Camera mCamera;
    private boolean existsCamera = false;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder=null;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private boolean isRecording = false;
    public final static String FILE_DIR = "/Phobia Enemy/";
    private File mediaFile;
    public static String nome_video;
    private String command = "ola";
    private TextView textView_tv;

    private BroadcastReceiver register =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            command = intent.getStringExtra("command");
            Log.e("x",command);
            if(command.equals("start")){
                start_video();
            }
            else if(command.equals("stop")){
                stop_video();
            }
            else if(command.equals("turnOff")){
                //Me.getSensor("Video").isConnected = false;
                finish();
            }

        }
    };
    public void start_video(){
        if (prepareVideoRecorder()) {
            // Camera is available and unlocked, MediaRecorder is prepared,
            // now you can start recording
            mMediaRecorder.start();
            isRecording = true;
            textView_tv.setText("Recording");
        } else {
            // prepare didn't work, release the camera
            Log.e("xxx","fdddd");
            isRecording = false;
            releaseMediaRecorder();
        }
    }
    public void stop_video(){
        // stop recording and release camera
        if(isRecording){
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            //mCamera.lock();         // take camera access back from MediaRecorder
            textView_tv.setText("");
            isRecording = false;
        }
        else{
            releaseMediaRecorder(); // release the MediaRecorder object
        }
    }

    public static boolean ExistsCamera(){
        return Camera.getNumberOfCameras() !=0;
    }
    /** A safe way to get an instance of the Camera object. */

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_app);
        LocalBroadcastManager.getInstance(this).registerReceiver(register, new IntentFilter("command"));
        // Create an instance of Camera
        if(mCamera == null)
            mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        if (preview != null) {
            preview.addView(mPreview);
        }
        // Add a listener to the Capture button
        textView_tv =  (TextView) findViewById(R.id.textView_tv);
       /* if (captureButton != null) {
            captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isRecording) {
                                stop_video();

                            } else {
                                start_video();
                            }
                        }
                    }
            );
        }*/
        Intent i = new Intent(getBaseContext(),BluetoothService.class);
        i.putExtra("command", "send" + ";" + "sensorOn" + Me.SC + "Video");
        startService(i);
        //Me.getSensor("Video").isConnected = true;
    }

    private boolean prepareVideoRecorder(){

        //Log.e(TAG, "mcamera object: " + mCamera.getNumberOfCameras());
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        try{
            mCamera.unlock();
        }catch (RuntimeException r){
            Log.d(TAG, "mcamera unlock: " + r.getMessage());
        }
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        Log.e("fds", getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.e(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    @Override
    public void finish(){
        Me.getSensor("Video").isConnected = false;
        //mCamera.reconnect();
        //releaseCamera();
        //releaseMediaRecorder();
        Intent i = new Intent(getBaseContext(),BluetoothService.class);
        i.putExtra("command", "send" + ";" + "sensorOff" + Me.SC + "Video");
        startService(i);
        super.finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        //releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        //@SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String timeStamp = Long.toString(BluetoothService.localId);
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
            nome_video = "VID_"+ timeStamp + ".mp4";
        } else {
            return null;
        }
        //Log.e("FILLLEEEEEEEEE",mediaFile.getPath().toString());
        return mediaFile;
    }

    /** A basic Camera preview class */
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview( Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d("x", "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();

            } catch (Exception e){
                Log.d("x", "Error starting camera preview: " + e.getMessage());
            }
        }
    }


}

