package com.kdh.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chobocho.tetrisgame.ChoboTetrisActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSystemService(Context.CONNECTIVITY_SERVICE);

        setMyPermission();

        MyNetWork.checkNetwork();
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
    }


    public void onClink(View v){
        switch (v.getId()){
            case R.id.btn1:
                Intent intent = new Intent(this, ChoboTetrisActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void setMyPermission() {
        String[] requestPerm = new String[15];
        int len = 0;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED)
            requestPerm[len++] = Manifest.permission.CALL_PHONE;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
            requestPerm[len++] = Manifest.permission.ACCESS_FINE_LOCATION;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)
            requestPerm[len++] = Manifest.permission.ACCESS_COARSE_LOCATION;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            requestPerm[len++] = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
            requestPerm[len++] = Manifest.permission.READ_CONTACTS;

        if (len > 0)
            ActivityCompat.requestPermissions(this, requestPerm, 1000);
    }

}
