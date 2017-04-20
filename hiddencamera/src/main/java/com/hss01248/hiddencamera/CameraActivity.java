package com.hss01248.hiddencamera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 *
 * https://item.congci.com/-/content/android-camera-houtai-paizhao
 */

public class CameraActivity extends Activity {

    private SurfaceView mySurfaceView;
    private SurfaceHolder myHolder;
    private Camera myCamera;
    private int width;
    private int height;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //设置布局
        setContentView(R.layout.activity_camera);

        Log.d("Demo", "oncreate");

        //初始化surface
        initSurface();

        //这里得开线程进行拍照，因为Activity还未完全显示的时候，是无法进行拍照的，SurfaceView必须先显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化camera并对焦拍照
                initCamera();
            }
        }).start();

    }

    //初始化surface
    @SuppressWarnings("deprecation")
    private void initSurface()
    {
        //初始化surfaceview
        mySurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);

        //初始化surfaceholder
        myHolder = mySurfaceView.getHolder();
        myHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    //初始化摄像头
    private void initCamera() {

        //如果存在摄像头
        if(checkCameraHardware(getApplicationContext()))
        {
            //获取摄像头（首选前置，无前置选后置）
            if(openFacingFrontCamera())
            {
                Log.d("Demo", "openCameraSuccess");
                //进行对焦
                autoFocus();
            }
            else {
                Log.d("Demo", "openCameraFailed");
            }

        }
    }

    //对焦并拍照
    private void autoFocus() {

        try {
            //因为开启摄像头需要时间，这里让线程睡两秒
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //自动对焦
        myCamera.autoFocus(myAutoFocus);

Camera.Parameters para = myCamera.getParameters();
        List<Camera.Size> list = para.getSupportedPictureSizes();
        Camera.Size sizePreview = list.get(list.size()/2);
        Camera.Size sizeTake = list.get(list.size()/2);
        for(Camera.Size  size : list){
            Log.e("size","width:"+size.width+"--height:"+size.height);
            if(size.width>300 && size.width <700){
                sizePreview = size;
            }
            if(size.width>=1000 && size.width <1500){
                sizeTake = size;
            }

        }
        para.setPictureSize(sizeTake.width, sizeTake.height);
        Log.e("preview size","width:"+sizePreview.width+"--height:"+sizePreview.height);
        Log.e("take size","width:"+sizeTake.width+"--height:"+sizeTake.height);
        para.setPreviewSize(sizePreview.width, sizePreview.height);
        width = sizeTake.width;
        height = sizeTake.height;
        myCamera.setParameters(para);

        //对焦后拍照
        myCamera.takePicture(null, null, myPicCallback);
    }



    //判断是否存在摄像头
    private boolean checkCameraHardware(Context context) {

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // 设备存在摄像头
            return true;
        } else {
            // 设备不存在摄像头
            return false;
        }

    }



    //得到后置摄像头
    private boolean openFacingFrontCamera() {

        //尝试开启前置摄像头
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    Log.d("Demo", "tryToOpenCamera");
                    myCamera = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        //如果开启前置失败（无前置）则开启后置
        if (myCamera == null) {
            for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        myCamera = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        return false;
                    }
                }
            }
        }

        try {
            //这里的myCamera为已经初始化的Camera对象
            myCamera.setPreviewDisplay(myHolder);
        } catch (IOException e) {
            e.printStackTrace();
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }

        myCamera.startPreview();

        return true;
    }

    //自动对焦回调函数(空实现)
    private Camera.AutoFocusCallback myAutoFocus = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };

    //拍照成功回调函数
    private Camera.PictureCallback myPicCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //完成拍照后关闭Activity
            CameraActivity.this.finish();
            Bitmap bitmap0 = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.e("size","bitmap0---------width:"+bitmap0.getWidth()+"--height:"+bitmap0.getHeight());
            //将得到的照片进行270°旋转，使其竖直
            Bitmap bitmap = byteToBitmap(data,width,height);
            /*Matrix matrix = new Matrix();
            matrix.preRotate(270);*/
            Bitmap bitmap2 = rotateBitmap(bitmap, 270, true);
            Log.e("size","bitmap2---------width:"+bitmap2.getWidth()+"--height:"+bitmap2.getHeight());
            //创建并保存图片文件
            File pictureFile = new File(getDir(), "camera.jpg");
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (Exception error) {
                Toast.makeText(CameraActivity.this, "拍照失败", Toast.LENGTH_SHORT).show();;
                Log.d("Demo", "保存照片失败" + error.toString());
                error.printStackTrace();
                myCamera.stopPreview();
                myCamera.release();
                myCamera = null;
            }

            Log.d("Demo", "获取照片成功");
            Toast.makeText(CameraActivity.this, "获取照片成功", Toast.LENGTH_SHORT).show();;
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
    };

    public static Bitmap byteToBitmap(byte[] b, int targetWidth, int targeHeight) {
        if(b != null && b.length != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(b, 0, b.length, options);
            int sourceWidth = options.outWidth;
            int sourceHeight = options.outHeight;
            options.inJustDecodeBounds = false;

            int inSampleSize;
            for(inSampleSize = 1; sourceWidth / inSampleSize > targetWidth; ++inSampleSize) {
                ;
            }

            while(sourceHeight / inSampleSize > targeHeight) {
                ++inSampleSize;
            }

            if(inSampleSize <= 0) {
                inSampleSize = 1;
            }

            options.inSampleSize = inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degress, boolean needRecycle) {
        Matrix m = new Matrix();
        m.postRotate((float)degress);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        if(needRecycle) {
            bitmap.recycle();
        }

        return bm;
    }

    //获取文件夹
    private File getDir()
    {
        //得到SD卡根目录
        File dir = Environment.getExternalStorageDirectory();

        if (dir.exists()) {
            return dir;
        }
        else {
            dir.mkdirs();
            return dir;
        }
    }
}

