package com.xugter.networktool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.xugter.networktoollib.NetType;
import com.xugter.networktoollib.Network;
import com.xugter.networktoollib.NetworkTool;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkTool.getDefault().register(this);
    }

    @Network(targetNetType = NetType.AUTO)
    public void test(NetType netType) {
        Log.i("bbbb", "===============" + netType);
    }

    @Override
    protected void onDestroy() {
        NetworkTool.getDefault().unregister(this);
        super.onDestroy();
    }
}
