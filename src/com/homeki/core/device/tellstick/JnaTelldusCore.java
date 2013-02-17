package com.homeki.core.device.tellstick;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;


public interface JnaTelldusCore extends Library {
	// Device methods
	public static int TELLSTICK_TURNON = 1;
	public static int TELLSTICK_TURNOFF = 2;
	public static int TELLSTICK_DIM = 16;
	
	// Error codes
	public static int TELLSTICK_SUCCESS = 0;
	public static int TELLSTICK_ERROR_NOT_FOUND = 1;
	public static int TELLSTICK_ERROR_PERMISSION_DENIED = 2;
	public static int TELLSTICK_ERROR_DEVICE_NOT_FOUND = 3;
	public static int TELLSTICK_ERROR_METHOD_NOT_SUPPORTED = 4;
	public static int TELLSTICK_ERROR_COMMUNICATION = 5;
	public static int TELLSTICK_ERROR_CONNECTING_SERVICE = 6;
	public static int TELLSTICK_ERROR_UNKNOWN_RESPONSE = 7;
	public static int TELLSTICK_ERROR_SYNTAX = 8;
	public static int TELLSTICK_ERROR_BROKEN_PIPE = 9;
	public static int TELLSTICK_ERROR_COMMUNICATING_SERVICE = 10;
	public static int TELLSTICK_ERROR_CONFIG_SYNTAX = 11;
	public static int TELLSTICK_ERROR_UNKNOWN = 99;
	
	interface TDDeviceEvent extends Callback {
		void invoke(int deviceId, int method, String data, int callbackId, Pointer context);
	}
	
	void tdInit();
	void tdClose();
	int tdRegisterDeviceEvent(TDDeviceEvent eventFunction, Pointer context);
	int tdGetNumberOfDevices();
	int tdGetDeviceId(int index);
	int tdMethods(int id, int methodsSupported);
	int tdUnregisterCallback(int callbackId);
	int tdAddDevice();
	int tdLearn(int id);
	int tdTurnOn(int id);
	int tdTurnOff(int id);
	int tdDim(int id, byte level);
	boolean tdSetModel(int id, String model);
	boolean tdSetProtocol(int id, String protocol);
	boolean tdSetDeviceParameter(int id, String name, String value);
	boolean tdRemoveDevice(int id);
}
