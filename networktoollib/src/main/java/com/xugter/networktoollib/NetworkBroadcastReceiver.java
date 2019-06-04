package com.xugter.networktoollib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

class NetworkBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Logger.e(NetworkTool.TAG, "====receiver error");
            return;
        }
        if (intent.getAction().equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Logger.d(NetworkTool.TAG, "====receiver update");
            NetworkTool.getDefault().updateNetworkState();
        }
    }
}