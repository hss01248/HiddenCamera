# HiddenCamera
use a transparent window(wrapped by dialog) whose type is TYPE_TOAST to take camera silently.

# usage 

## 拍照:

> CameraUtil.takePhotoQuitely:

```

	/**
     * @param context applicationcontext
     * @param isdebug debug模式,则文件夹创建于sd卡根目录下的00aa,正式模式,则创建在app私有目录下的ytx下,文件夹均以.nomedia隐藏  同时,isdebug也是日志开关
     * @param minWidth 小于300,则按内部的1000计
     * @param maxWidth 小于300,则按内部的1500计
     * @param callback
     *
     * 参考:minWidth=1500,maxWidth = 2500时,小米max拍得 1200*1600的照片(1600为原先的width,实际图片的高),质量80%,保存得文件大小为110k-140k
     * 内部默认值: minWidth=1000,maxWidth = 1500时,小米max拍得 768*1024的照片(1024为width),质量80%,保存得文件大小为50-70k
     */
    public static void takePhotoQuitely(Context context,boolean isdebug, int minWidth,int maxWidth,final PhotoCallback callback)
```

## 回调:

> PhotoCallback

```
void onFail();
void onSuccess(String path);//拿到本次拍照所得的图片
```

## 获取拍照文件存储的文件夹:

> CameraUtil.getPhotoDir

```
	/**debug 时,保存到sd卡根目录下的00aa文件夹下.
     * 非debug时,保存到应用私有目录下,并隐藏起来,不让其他应用和系统媒体库看到
     * @param context
     * @return
     */
    public static File getPhotoDir(Context context,boolean isdebug)
```



# 权限

> manifest会自动合并,无需再添加,但6.0以上动态申请还是要的:

> 注意小米6.0以下,Manifest.permission.SYSTEM_ALERT_WINDOW权限默认是关闭的.

```
//推荐用RxPermissions:
new RxPermissions(this)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                    }
                });
                
                
//记得添加gradle:
compile 'com.tbruyelle.rxpermissions2:rxpermissions:0.9.4@aar'
compile 'com.jakewharton.rxbinding2:rxbinding:2.0.0'
```

##  gradle 

> no need to add permissions 

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

```
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }

```

**Step 2.** Add the dependency

```
    dependencies {
            compile 'com.github.hss01248:HiddenCamera:lastest release'
    }
```
lastest release:https://github.com/hss01248/HiddenCamera/releases



# todo 

结合 https://github.com/zhaozepeng/FloatWindowPermission 适配permission.SYSTEM_ALERT_WINDOW权限

