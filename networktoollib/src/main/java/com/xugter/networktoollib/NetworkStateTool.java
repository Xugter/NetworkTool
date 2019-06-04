package com.xugter.networktoollib;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class NetworkStateTool {

    static boolean isNetworkAvailable() {
        if (NetworkTool.getDefault().getConnectivityManager() == null) {
            Logger.e(NetworkTool.TAG, "====Get ConnectivityManager Failed");
            return false;
        }
        NetworkInfo[] info = NetworkTool.getDefault().getConnectivityManager().getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    Logger.d(NetworkTool.TAG, "====" + networkInfo.getTypeName() + " connected");
                    return true;
                }
            }
        }
        return false;
    }

    static NetType getNetType() {
        if (NetworkTool.getDefault().getConnectivityManager() == null) {
            Logger.e(NetworkTool.TAG, "====Get ConnectivityManager Failed");
            return NetType.NONE;
        }
        NetworkInfo networkInfo = NetworkTool.getDefault().getConnectivityManager().getActiveNetworkInfo();
        if (networkInfo == null) {
            Logger.e(NetworkTool.TAG, "====Get NetworkInfo Failed");
            return NetType.NONE;
        }
        int type = networkInfo.getType();
        Logger.d(NetworkTool.TAG, "====networkInfo type=" + type);
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
