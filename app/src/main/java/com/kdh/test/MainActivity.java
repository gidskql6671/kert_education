package com.kdh.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private TextView txtResult;
    private Socket socket;  //소켓 생성
    BufferedReader in;      //서버로부터 온 데이터를 읽는다.
    PrintWriter out;        //서버에 데이터를 전송한다.
    EditText input;         //화면구성
    Button btn1;          //화면구성
    TextView output;        //화면구성
    String data;
    TextView ttmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMyPermission();

        input = findViewById(R.id.input);
        output = findViewById(R.id.output);
        txtResult = findViewById(R.id.tv1);

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("155.230.52.63", 5678); // 소켓 생성
                    // 데이터 전송 시 stream 형태로 변환하여 전송
                    out = new PrintWriter(socket.getOutputStream(), true);

                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    while (true) {
                        data = in.readLine(); // in으로 받은 데이터를 String 형태로 읽어 data에 저장
                        output.post(new Runnable() {
                            @Override
                            public void run() {
                                output.setText(data); // 글자 출력칸에 서버가 보낸 메시지를 받음.
                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
        });

        worker.start();

        /*
        getLocation();

        if (!isConnected()){
            Toast.makeText(this, "네트워크 연결 안됨", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (getNetWorkType()){
            case ConnectivityManager.TYPE_WIFI:
                Toast.makeText(this, "Wifi에 연결", Toast.LENGTH_SHORT).show();
                break;
            case ConnectivityManager.TYPE_MOBILE:
                Toast.makeText(this, "모바일에 연결", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    protected void onStop() {  //앱 종료시
        super.onStop();
        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNetWorkType(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork.getType();
    }
    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetWork != null && activeNetWork.isConnectedOrConnecting());

        return isConnected;
    }

    public void getLocation() {
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }

    public void onClink(View v){
        switch (v.getId()){
            case R.id.btn1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data = input.getText().toString();
                        Log.w("NETWORK", " " + data);
                        if (data != null){
                            out.println(data); // data를 stream 형태로 변환하여 전송. 변환 내용은 쓰레드에 담겨있음
                        }
                    }
                }).start();
                break;
            case R.id.btn2:

        }
    }
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            txtResult.setText("위치정보 : "+ provider +"\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

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

        if (len > 0)
            ActivityCompat.requestPermissions(this, requestPerm, 1000);
    }

}
