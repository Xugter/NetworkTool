package com.xugter.networktoollib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodManager {

    /**
     * 目标过滤网络类型
     */
    private NetType targetNetType;
    /**
     * 目标方法
     */
    private Method method;

    MethodManager(NetType netType, Method method) {
        this.targetNetType = netType;
        this.method = method;
    }

    NetType getTargetNetType() {
        return targetNetType;
    }

    void setTargetNetType(NetType targetNetType) {
        this.targetNetType = targetNetType;
    }

    Method getMethod() {
        return method;
    }

    void setMethod(Method method) {
        this.method = method;
    }

    void invoke(Object object, NetType netType) {
        try {
            method.invoke(object, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
