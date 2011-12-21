package com.homeki.core.device.tellstick;

public class TellStickNative {
	public static native void open();
	public static native void close();
    public static native int[] getDeviceIds();
    public static native String getDeviceType(int id);
    public static native void turnOn(int id);
    public static native void turnOff(int id);
    public static native void dim(int id, int level);
    public static native String getEvent();
    // "id(internalid) värde"
    // värde kan vara numeriskt (om dimmer) eller true/false (om switch)
}
