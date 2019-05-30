package com.xugter.networktoollib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateTool {

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetworkTool.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static NetType getNetType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetworkTool.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return NetType.NONE;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) return NetType.NONE;
        int type = networkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }
}
