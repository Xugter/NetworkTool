package com.xugter.networktoollib;

public class NetworkTool {

    public static String TAG = "NetworkTool";
    static volatile NetworkTool defaultInstance;

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
}