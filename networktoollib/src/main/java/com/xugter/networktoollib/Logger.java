package com.xugter.networktoollib;

import android.util.Log;

class Logger {
    static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    static void d(String tag, String msg) {
        if (NetworkTool.DEBUG) {
            Log.d(tag, msg);
        }
    }

    static void e(String tag, String msg) {
        Log.e(tag, msg);
    }
}
