package com.kdh.test;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerClient extends AsyncTask<Void, Integer, Integer> {
    private Context context;
    String server;
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    String data;

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sendContact(){
        ContactUtil cu = new ContactUtil(context);
        ArrayList<Contact> myContacts = cu.getContactList();

        for(Contact value : myContacts){
            out.println("이름 : " + value.getName() + " 번호 : "+value.getPhoneNumber());
            Log.w("Contact", "이름 : " + value.getName() + " 번호 : "+value.getPhoneNumber());
        }
        out.println("end");
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
