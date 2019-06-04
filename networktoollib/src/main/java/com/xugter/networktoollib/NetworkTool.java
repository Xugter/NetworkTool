package com.xugter.networktoollib;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.*;
import android.net.Network;
import android.os.Build;

import java.lang.reflect.Method;
import java.util.*;

public class NetworkTool {

    static String TAG = "NetworkTool";
    static boolean DEBUG = false;
    private static volatile NetworkTool defaultInstance;
    private Application application;
    private NetworkBroadcastReceiver networkBroadcastReceiver;
    private Map<Object, List<MethodManager>> subscriberList;
    private ConnectivityManager connectivityManager;
    private boolean init = false;

    private NetworkTool() {
        subscriberList = new HashMap<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            networkBroadcastReceiver = new NetworkBroadcastReceiver();
        }
    }

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

    public void init(Application application) {
        if (init) return;
        init = true;
        this.application = application;
        connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Logger.i(TAG, "====register network callback");
            ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        Logger.i(TAG, "====onAvailable " + network.toString());
                        updateNetworkState();
                    }

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        Logger.i(TAG, "====onLost" + network.toString());
                        updateNetworkState();
                    }
                });
            }
        } else {
            Logger.i(TAG, "====register network broadcast receiver");
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            application.registerReceiver(networkBroadcastReceiver, filter);
        }
    }

    public void setDebug(boolean debug) {
        DEBUG = debug;
    }

    public void register(Object subscriber) {
        if (!subscriberList.containsKey(subscriber)) {
            List<MethodManager> methodManagerList = findAnnotationMethod(subscriber);
            Logger.i(TAG, "====add " + methodManagerList.size() + " method for " + subscriber.toString());
            subscriberList.put(subscriber, methodManagerList);
            postNetTypeForMethodList(subscriber, methodManagerList, NetworkStateTool.getNetType());
        }
    }

    public void unregister(Object subscriber) {
        if (!subscriberList.isEmpty()) {
            Logger.i(TAG, "====unregister" + " for " + subscriber.toString());
            subscriberList.remove(subscriber);
        }
    }

    Application getApplication() {
        return application;
    }

    ConnectivityManager getConnectivityManager () {
        return connectivityManager;
    }

    void updateNetworkState() {
        Logger.i(TAG, "====updateNetworkState");
        post(NetworkStateTool.getNetType());
    }

    private void post(NetType netType) {
        Logger.i(TAG, "====post net type=" + netType);
        Set<Object> set = subscriberList.keySet();
        for (Object keyObject : set) {
            List<MethodManager> methodManagerList = subscriberList.get(keyObject);
            postNetTypeForMethodList(keyObject, methodManagerList, netType);
        }
    }

    private void postNetTypeForMethodList(Object keyObject, List<MethodManager> methodManagerList, NetType netType) {
        if (methodManagerList != null) {
            for (MethodManager methodManager : methodManagerList) {
                switch (methodManager.getTargetNetType()) {
                    case AUTO:
                        methodManager.invoke(keyObject, netType);
                        break;
                    case NONE:
                        break;
                    case WIFI:
                        break;
                    case CMNET:
                        break;
                    case CMWAP:
                        break;
                }
            }
        }
    }

    private List<MethodManager> findAnnotationMethod(Object subscriber) {
        List<MethodManager> methodManagerList = new ArrayList<>();
        Class<?> clazz = subscriber.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            com.xugter.networktoollib.Network network = method.getAnnotation(com.xugter.networktoollib.Network.class);
            if (network == null) continue;
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + " wrong parameter");
            }
            if (parameterTypes[0].isAssignableFrom(NetType.class)) {
                MethodManager methodManager = new MethodManager(network.targetNetType(), method);
                methodManagerList.add(methodManager);
            } else {
                throw new RuntimeException(method.getName() + " wrong parameter");
            }
        }
        return methodManagerList;
    }
}