package com.hss01248.hiddencamera;

/**
 * Created by Administrator on 2017/4/22.
 */

public class Log {
    public static void setIsDebug(boolean isDebug) {
        Log.isDebug = isDebug;
    }

    private static boolean isDebug;


    public static void i(String tag,String msg){
        if(isDebug){
            android.util.Log.i(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if(isDebug){
            android.util.Log.e(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(isDebug){
            android.util.Log.d(tag,msg);
        }
    }
}
