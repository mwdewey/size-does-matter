package theperfectfit.sizedoesmatter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.*;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.graphics.PixelFormat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
public class CameraFragment extends android.support.v4.app.Fragment {
    private Preview mPreview;
    Camera mCamera;
    int mNumberOfCameras;
    int mCurrentCamera;  // Camera ID currently chosen
    int mCameraCurrentlyLocked;  // Camera ID that's actually acquired
    // The first rear facing camera
    int mDefaultCameraId;
    Button mButton;
    View mainview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a container that will hold a SurfaceView for camera previews
        //mPreview = new Preview(this.getActivity());
        mainview = new View(this.getActivity());
        RelativeLayout relativeLayout = new RelativeLayout(this.getActivity());
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT );


        //relativeLayout.addView(mPreview);
        //this.getActivity().setContentView(R.layout.c_frag);
       // mPreview = (Preview) this.getActivity().findViewById(R.id.camView);


    }
    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Add an up arrow to the "home" button, indicating that the button will go "up"
        // one activity in the app's Activity heirarchy.
        // Calls to getActionBar() aren't guaranteed to return the ActionBar when called
        // from within the Fragment's onCreate method, because the Window's decor hasn't been
        // initialized yet.  Either call for the ActionBar reference in Activity.onCreate()
        // (after the setContentView(...) call), or in the Fragment's onActivityCreated method.
        Activity activity = this.getActivity();
        ActionBar actionBar = activity.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relativeLayout = new RelativeLayout(this.getActivity());
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);

        //      mCamera = Camera.open(mCurrentCamera);
//        mPreview.setCamera(mCamera);
        View endV = inflater.inflate(R.layout.c_frag, relativeLayout, false);
        mButton = (Button) endV.findViewById(R.id.take_button);
        mPreview = (Preview) endV.findViewById(R.id.camView);
        mCamera = mPreview.getCamera();
        mButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           PictureCallback mPicture = new PictureCallback() {
                                               @Override
                                               public void onPictureTaken(byte[] data, Camera camera) {

                                                   System.out.println("byte count is " + data.length);

                                                   ByteArrayInputStream stream = new ByteArrayInputStream(data);

                                                   Bitmap bitmap = BitmapFactory.decodeStream(stream);

                                                   currentImage.getInstance().setBit(bitmap);
                                                   System.out.println("Width is " + bitmap.getWidth());
                                                   android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                                   fragmentManager.popBackStack();
                                               }
                                           };

                                           if (mCamera == null) System.out.println("cam is null");

                                           mCamera.takePicture(null, null, mPicture);
                                       }
                                   }


        );
        return endV;
    }
    @Override
    public void onResume() {
        super.onResume();

        // Use mCurrentCamera to select the camera desired to safely restore
        // the fragment after the camera has been changed
       // mCamera = Camera.open(mCurrentCamera);
       // mCameraCurrentlyLocked = mCurrentCamera;
       // mPreview.setCamera(mCamera);
    }

    /*public void takePicture(View cView){
        PictureCallback mPicture = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                ByteArrayInputStream stream = new ByteArrayInputStream(data);

                Bitmap bitmap = BitmapFactory.decodeStream(stream);

                System.out.println(bitmap.getWidth());


            }
        };
    }*/


    @Override
    public void onPause() {
        super.onPause();
        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mNumberOfCameras > 1) {
            // Inflate our menu which can gather user input for switching camera
            inflater.inflate(R.menu.camera_menu, menu);
        } else {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_switch_cam:
                // Release this camera -> mCameraCurrentlyLocked
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mPreview.setCamera(null);
                    mCamera.release();
                    mCamera = null;
                }
                // Acquire the next camera and request Preview to reconfigure
                // parameters.
                mCurrentCamera = (mCameraCurrentlyLocked + 1) % mNumberOfCameras;
                mCamera = Camera.open(mCurrentCamera);
                mCameraCurrentlyLocked = mCurrentCamera;
                mPreview.switchCamera(mCamera);
                // Start the preview
                mCamera.startPreview();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this.getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
// ----------------------------------------------------------------------
/**
 * A simple wrapper around a Camera and a SurfaceView that renders a centered
 * preview of the Camera to the surface. We need to center the SurfaceView
 * because not all devices have cameras that support preview sizes at the same
 * aspect ratio as the device's display.
 */
class Preview extends ViewGroup implements SurfaceHolder.Callback {
    private final String TAG = "Preview";
    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    Size mPreviewSize;
    Button mButton;
    List<Size> mSupportedPreviewSizes;
    Camera mCamera;
    boolean mSurfaceCreated = false;
    public Preview(Context context) {
        super(context);

        mSurfaceView = new SurfaceView(context);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        //mSurfaceView.setLayoutParams(lp);
        //addView(mSurfaceView);
        addViewInLayout(mSurfaceView,0,lp);
        setCamera(Camera.open(CameraInfo.CAMERA_FACING_BACK));

       /* Button b = new Button(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
        params.leftMargin = 0;
        params.topMargin = 0;
        //b.setLayoutParams(lb);
        //addView(b);
        addViewInLayout(b,1,params);*/

       /* mSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                PictureCallback mPicture = new PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                        System.out.println("byte count is " + data.length);

                        ByteArrayInputStream stream = new ByteArrayInputStream(data);

                        Bitmap bitmap = BitmapFactory.decodeStream(stream);


                        System.out.println("Width is " + bitmap.getWidth());
                    }
                };

                if(mCamera == null) System.out.println("cam is null");

                mCamera.takePicture(null,null,mPicture);

            }
        });*/

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    public Preview(Context context, AttributeSet attrs) {
        super(context,attrs);

        mSurfaceView = new SurfaceView(context);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        //mSurfaceView.setLayoutParams(lp);
        //addView(mSurfaceView);
        addViewInLayout(mSurfaceView,0,lp);
        setCamera(Camera.open(CameraInfo.CAMERA_FACING_BACK));



       /* Button b = new Button(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
        params.leftMargin = 0;
        params.topMargin = 0;
        //b.setLayoutParams(lb);
        //addView(b);
        addViewInLayout(b,1,params);*/

       /* mSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                PictureCallback mPicture = new PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                        System.out.println("byte count is " + data.length);

                        ByteArrayInputStream stream = new ByteArrayInputStream(data);

                        Bitmap bitmap = BitmapFactory.decodeStream(stream);


                        System.out.println("Width is " + bitmap.getWidth());
                    }
                };

                if(mCamera == null) System.out.println("cam is null");

                mCamera.takePicture(null,null,mPicture);

            }
        });*/

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    public Preview(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs, defStyle);

        mSurfaceView = new SurfaceView(context);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);


        //mSurfaceView.setLayoutParams(lp);
        //addView(mSurfaceView);
        addViewInLayout(mSurfaceView,0,lp);
        setCamera(Camera.open(CameraInfo.CAMERA_FACING_BACK));


       /* Button b = new Button(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30, 40);
        params.leftMargin = 0;
        params.topMargin = 0;
        //b.setLayoutParams(lb);
        //addView(b);
        addViewInLayout(b,1,params);*/


        /*mSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                PictureCallback mPicture = new PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                        System.out.println("byte count is " + data.length);

                        ByteArrayInputStream stream = new ByteArrayInputStream(data);

                        Bitmap bitmap = BitmapFactory.decodeStream(stream);


                        System.out.println("Width is " + bitmap.getWidth());
                    }
                };

                if(mCamera == null) System.out.println("cam is null");

                mCamera.takePicture(null,null,mPicture);

            }
        });*/

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters()
                    .getSupportedPreviewSizes();
            if (mSurfaceCreated) requestLayout();
            //mCamera.setDisplayOrientation(90);
        }
    }
    public void switchCamera(Camera camera) {
        setCamera(camera);
        try {
            camera.setPreviewDisplay(mHolder);
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }
    //@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        setMeasuredDimension(width, height);
        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
                    height);
        }
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
            mCamera.setParameters(parameters);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            final int width = r - l;
            final int height = b - t;
            child.layout(0, 0,
                    width, height);
            //child.layout(0, (height - scaledChildHeight) / 2, width,
            //        (height + scaledChildHeight) / 2);
            // Center the child SurfaceView within the parent.
            /*if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height
                        / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width
                        / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2, width,
                        (height + scaledChildHeight) / 2);
            }*/
        }
    }
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
        if (mPreviewSize == null) requestLayout();
        mSurfaceCreated = true;
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.


        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.height, mPreviewSize.width);
        parameters.setPictureSize(2560,1920);
        parameters.setRotation(90);
        parameters.set("jpeg-quality", 70);
        parameters.setPictureFormat(PixelFormat.JPEG);
        requestLayout();

       // for(Size s : parameters.getSupportedPictureSizes()) System.out.println(s.width + " " + s.height);

        mCamera.setDisplayOrientation(90);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    public Camera getCamera(){
        return mCamera;
    }



}


