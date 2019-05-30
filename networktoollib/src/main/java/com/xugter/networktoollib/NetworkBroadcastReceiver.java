package com.xugter.networktoollib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkTool.getDefault().updateNetworkState();
    }
}