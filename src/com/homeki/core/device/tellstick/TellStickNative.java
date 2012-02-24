package com.homeki.core.device.tellstick;

public class TellStickNative {
	// http://developer.telldus.se/browser/telldus-core/client/telldus-core.cpp
	// http://developer.telldus.se/wiki/TellStick_conf
	//
	// protocol we use is arctech (haven't bought any device yet which does NOT use arctech)
	//
	// house is 1-67108863
	// unit is 1-16
	
	public synchronized static native void open() throws TellStickNativeException;
	public synchronized static native void close();
    public synchronized static native int[] getDeviceIds() throws TellStickNativeException;
    public synchronized static native String getDeviceType(int id);
    public synchronized static native int addSwitch(int house, int unit) throws TellStickNativeException;
    public synchronized static native int addDimmer(int house, int unit) throws TellStickNativeException;
    public synchronized static native void learn(int id) throws TellStickNativeException;
    public synchronized static native void removeDevice(int id) throws TellStickNativeException;
    public synchronized static native void turnOn(int id) throws TellStickNativeException;
    public synchronized static native void turnOff(int id) throws TellStickNativeException;
    public synchronized static native void dim(int id, int level) throws TellStickNativeException;
    public static native String getEvent();
}
