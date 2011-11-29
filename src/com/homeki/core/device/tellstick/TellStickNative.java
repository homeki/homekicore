package com.homeki.core.device.tellstick;

public class TellStickNative {
    public native void print();

    static {
        System.loadLibrary("telldusjni");
    }
}
