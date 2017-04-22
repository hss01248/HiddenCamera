package com.hss01248.hidedemo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hss01248.hiddencamera.CameraUtil;
import com.hss01248.hiddencamera.PhotoCallback;
import com.hss01248.permission.FloatWindowManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"hahahh",Toast.LENGTH_SHORT).show();
            }
        });



        getPermission();
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.this.startActivity(new Intent(MainActivity.this, CameraActivity.class));
                CameraUtil.takePhotoQuitely(getApplicationContext(),false, 1500,2500,new PhotoCallback() {
                    @Override
                    public void onFail() {
                        Log.e("onFail","onFail----------------");
                    }

                    @Override
                    public void onSuccess(String path) {
                        Log.e("onSuccess",path);
                    }
                });
            }
        });
    }

    private void getPermission() {
        if(Build.VERSION.SDK_INT < 23){
            FloatWindowManager.getInstance().askPermission(this);
        }else {
            new RxPermissions(this)
                    .request(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.SYSTEM_ALERT_WINDOW)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if(aBoolean){

                            }
                        }
                    });
        }
    }


}
