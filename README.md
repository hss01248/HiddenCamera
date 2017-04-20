# HiddenCamera
use a transparent activity to take camera silently



# usage 

only two files in the project: 

CameraActivity.java

activity_camera.xml

## you can just copy them 

and add necessary permissions into menifest.

```
<!-- 调用相机权限 -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus" />
<!-- 读写SD卡权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

## or use gradle as below:

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







## Then



## regist the activity in menifest:

```
<activity android:name="com.hss01248.hiddencamera.CameraActivity"  android:theme="@android:style/Theme.Translucent"/>
```

### start by simple intent

```
MainActivity.this.startActivity(new Intent(MainActivity.this, CameraActivity.class));
```



