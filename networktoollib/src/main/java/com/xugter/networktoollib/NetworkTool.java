package com.xugter.networktoollib;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.*;
import android.os.Build;
import android.util.Log;

public class NetworkTool {

    private static String TAG = "NetworkTool";
    private static volatile NetworkTool defaultInstance;
    private Application application;
    private NetworkBroadcastReceiver networkBroadcastReceiver;

    public static NetworkTool getDefault() {
        NetworkTool instance = defaultInstance;
        if (instance == null) {
            synchronized (NetworkTool.class) {
                instance = NetworkTool.defaultInstance;
                if (instance == null) {
                    instance = NetworkTool.defaultInstance = new NetworkTool();
                }
            }
        }
        return instance;
    }

    public Application getApplication() {
        return application;
    }

    public void init(Application application) {
        this.application = application;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onAvailable(Network network) {
                        updateNetworkState();
                    }

                    @Override
                    public void onLost(Network network) {
                        updateNetworkState();
                    }
                });
            }
        } else {
            if (networkBroadcastReceiver == null) {
                networkBroadcastReceiver = new NetworkBroadcastReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                application.registerReceiver(networkBroadcastReceiver, filter);
            }
        }
    }

    public void updateNetworkState() {
        Log.i(TAG, "====isAvailable=" + NetworkStateTool.isNetworkAvailable());
        Log.i(TAG, "====netType=" + NetworkStateTool.getNetType());
    }
}