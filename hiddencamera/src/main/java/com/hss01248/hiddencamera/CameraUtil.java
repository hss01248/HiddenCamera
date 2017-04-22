package com.hss01248.hiddencamera;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/4/21.
 */

public class CameraUtil {

    /**
     *
     * @param context
     * @param isdebug debug模式,则文件夹创建于sd卡根目录下的00aa,正式模式,则创建在app私有目录下的ytx下,文件夹均以.nomedia隐藏.同时,isdebug也是日志开关
     * @param minWidth 小于300,则按内部的1000计
     * @param maxWidth 小于300,则按内部的1500计
     * @param callback
     *
     * 参考:minWidth=1500,maxWidth = 2500时,小米max拍得 1200*1600的照片(1600为width),质量80%,保存得文件大小为110k-140k
     * 内部默认值: minWidth=1000,maxWidth = 1500时,小米max拍得 768*1024的照片(1024为width),质量80%,保存得文件大小为50-70k
     */
    public static void takePhotoQuitely(Context context,boolean isdebug, int minWidth,int maxWidth,final PhotoCallback callback){
        CameraPage page = new CameraPage(context);
        Log.setIsDebug(isdebug);
        page.setIsdebug(isdebug);

        page.setWidthRange(minWidth,maxWidth);
        final Dialog dialog = new Dialog(context);

        PhotoCallback callback2 = new PhotoCallback() {
            @Override
            public void onFail() {
                callback.onFail();
                if(dialog!=null && dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onSuccess(String path) {
                callback.onSuccess(path);
                if(dialog!=null && dialog.isShowing())
                dialog.dismiss();

            }
        };
        page.setCallback(callback2);
      WindowManager.LayoutParams params =  dialog.getWindow().getAttributes();
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        dialog.getWindow().setAttributes(params);
        dialog.setContentView(page.getRootView());
        dialog.show();
        page.begin();
    }


    /**debug 时,保存到sd卡根目录下的00aa文件夹下.
     * 非debug时,保存到应用私有目录下,并隐藏起来,不让其他应用和系统媒体库看到
     * @param context
     * @return
     */
    public static File getPhotoDir(Context context,boolean isdebug){
        File dir = null;
        if(isdebug){
            dir = Environment.getExternalStorageDirectory();
        }else {
            dir = context.getFilesDir();
        }
        File dir0 = new File(dir,isdebug ? "00aa" : "ytx");//context.getFilesDir()
        if(!dir0.exists()){
            dir0.mkdirs();
        }
        File file = new File(dir0,".nomedia");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dir0;
    }
}
