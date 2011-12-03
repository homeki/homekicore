package com.homeki.core.device.tellstick;

public class TellStickNative {
	public static native void open();
	public static native void close();
    public static native int[] getDeviceIds();
}
