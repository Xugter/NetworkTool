package com.xugter.networktool

import android.app.Application
import com.xugter.networktoollib.NetworkTool

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkTool.getDefault().init(this)
    }
}