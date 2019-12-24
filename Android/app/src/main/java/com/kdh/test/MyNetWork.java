package com.kdh.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyNetWork {
    private static Context mc = null;

    public static int checkNetwork(){
        mc = ApplicationClass.getContext();

        if (!isConnected()){
            Toast.makeText(mc, "네트워크 연결 안됨", Toast.LENGTH_SHORT).show();
            return -1;
        }
        switch (getNetWorkType()){
            case ConnectivityManager.TYPE_WIFI:
                Toast.makeText(mc, "Wifi에 연결", Toast.LENGTH_SHORT).show();
                return 0;
            case ConnectivityManager.TYPE_MOBILE:
                Toast.makeText(mc, "모바일에 연결", Toast.LENGTH_SHORT).show();
                return 1;
        }
        return -1;
    }

    private static int getNetWorkType(){
        ConnectivityManager cm = (ConnectivityManager)mc.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork.getType();
    }
    private static boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)mc.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetWork != null && activeNetWork.isConnectedOrConnecting());

        return isConnected;
    }
}
