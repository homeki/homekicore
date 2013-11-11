package com.homeki.core.device.tellstick;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public enum TellStickApi {
	// http://developer.telldus.se/browser/telldus-core/client/telldus-core.cpp
	// http://developer.telldus.se/wiki/TellStick_conf
	//
	// protocol we use is arctech (haven't bought any device yet which does NOT use arctech)
	//
	// house is 1-67108863
	// unit is 1-16
	
	INSTANCE;
	
	class DeviceEventHandler implements JnaTelldusCore.TDDeviceEvent {
		@Override
		public void invoke(int deviceId, int method, String data, int callbackId, Pointer context) {
			String value = "device " + deviceId + " "; 
			
			switch (method) {
			case JnaTelldusCore.TELLSTICK_TURNON:
				value += "true";
				break;
			case JnaTelldusCore.TELLSTICK_TURNOFF:
				value += "false";
				break;
			case JnaTelldusCore.TELLSTICK_DIM:
				value += data;
				break;
			default:
				return;
			}

			events.add(value);
		}
	}
	
	enum Type {
		SWITCH,
		DIMMER
	}
	
	private JnaTelldusCore telldus;
	private BlockingQueue<String> events;
	private DeviceEventHandler deviceEventHandler;
	private int deviceEventHandlerId;
	
	public synchronized void open() throws TellStickNativeException {
		if (events != null)
			throw new RuntimeException("May only call TellstickNative.open() once.");
		
		events = new LinkedBlockingQueue<String>();
		deviceEventHandler = new DeviceEventHandler();
		telldus = (JnaTelldusCore)Native.loadLibrary("telldus-core", JnaTelldusCore.class);
		deviceEventHandlerId = telldus.tdRegisterDeviceEvent(deviceEventHandler, Pointer.NULL);
		checkIfFailed(deviceEventHandlerId);
	}
	
	public synchronized void close() {
		telldus.tdUnregisterCallback(deviceEventHandlerId);
		telldus.tdClose();
	}
	
    public synchronized int[] getDeviceIds() throws TellStickNativeException {
    	int count = telldus.tdGetNumberOfDevices();
    	
    	checkIfFailed(count);
    	
    	int[] ids = new int[count];
    	
    	for (int i = 0; i < count; i++) {
    		ids[i] = telldus.tdGetDeviceId(i);
    		checkIfFailed(ids[i]);
    	}
    	
    	return ids;
    }
    
    public synchronized String getDeviceType(int id) {
    	int features = telldus.tdMethods(id, JnaTelldusCore.TELLSTICK_DIM | JnaTelldusCore.TELLSTICK_TURNON);
    	String type = "";

    	if ((features & JnaTelldusCore.TELLSTICK_DIM) != 0)
    		type = "dimmer";
    	else if ((features & JnaTelldusCore.TELLSTICK_TURNON) != 0)
    		type = "switch";

    	return type;
    }
    
    public synchronized int addSwitch(int house, int unit) throws TellStickNativeException {
    	return addDevice(Type.SWITCH, house, unit);
    }
    
    public synchronized int addDimmer(int house, int unit) throws TellStickNativeException {
    	return addDevice(Type.DIMMER, house, unit);
    }
    
    public synchronized void learn(int id) throws TellStickNativeException {
    	int ret = telldus.tdLearn(id);
    	checkIfFailed(ret);
    }
    
    public synchronized void removeDevice(int id) throws TellStickNativeException {
    	if (!telldus.tdRemoveDevice(id))
    		throw new TellStickNativeException("Failed to remove device from tellstick.conf.");
    }
    
    public synchronized void turnOn(int id) throws TellStickNativeException {
    	int ret = telldus.tdTurnOn(id);
    	checkIfFailed(ret);
    }
    
    public synchronized void turnOff(int id) throws TellStickNativeException {
    	int ret = telldus.tdTurnOff(id);
    	checkIfFailed(ret);
    }
    
    public synchronized void dim(int id, int level) throws TellStickNativeException {
    	int ret = telldus.tdDim(id, (byte)level);
    	checkIfFailed(ret);
    }
    
    public String getEvent() throws InterruptedException {
    	return events.take();
    }
    
    private int addDevice(Type type, int house, int unit) {
    	int id = telldus.tdAddDevice();

    	checkIfFailed(id);

    	if (type == Type.SWITCH) {
    		if (!telldus.tdSetModel(id, "selflearning-switch"))
    			throw new TellStickNativeException("Failed to set model 'selflearning-switch'.");
    	}
    	else if (type == Type.DIMMER) {
    		if (!telldus.tdSetModel(id, "selflearning-dimmer"))
    			throw new TellStickNativeException("Failed to set model 'selflearning-dimmer'.");
    	}

    	if (!telldus.tdSetProtocol(id, "arctech"))
    		throw new TellStickNativeException("Failed to set protocol 'arctech'.");

    	if (!telldus.tdSetDeviceParameter(id, "house", String.valueOf(house)))
    		throw new TellStickNativeException("Failed to set device parameter 'house'.");

    	if (!telldus.tdSetDeviceParameter(id, "unit", String.valueOf(unit)))
    		throw new TellStickNativeException("Failed to set device parameter 'unit	'.");

    	return id;
    }
    
    private void checkIfFailed(int result) {
    	if (result == JnaTelldusCore.TELLSTICK_SUCCESS || result > 0)
    		return;
    	
    	switch (result) {
    	case JnaTelldusCore.TELLSTICK_ERROR_NOT_FOUND:
    		throw new TellStickNativeException("TellStick not found.");
    	case JnaTelldusCore.TELLSTICK_ERROR_PERMISSION_DENIED:
    		throw new TellStickNativeException("Permission denied accessing TellStick.");
    	case JnaTelldusCore.TELLSTICK_ERROR_DEVICE_NOT_FOUND:
    		throw new TellStickNativeException("Device not found.");
    	case JnaTelldusCore.TELLSTICK_ERROR_METHOD_NOT_SUPPORTED:
    		throw new TellStickNativeException("The method you tried to use is not supported by the device.");
    	case JnaTelldusCore.TELLSTICK_ERROR_COMMUNICATION:
    		throw new TellStickNativeException("An error occurred while communicating with TellStick.");
    	case JnaTelldusCore.TELLSTICK_ERROR_CONNECTING_SERVICE:
    		throw new TellStickNativeException("Could not connect to the Telldus Service.");
    	case JnaTelldusCore.TELLSTICK_ERROR_UNKNOWN_RESPONSE:
    		throw new TellStickNativeException("Received an unknown response.");
    	case JnaTelldusCore.TELLSTICK_ERROR_SYNTAX:
    		throw new TellStickNativeException("Syntax error.");
    	case JnaTelldusCore.TELLSTICK_ERROR_BROKEN_PIPE:
    		throw new TellStickNativeException("Broken pipe.");
    	case JnaTelldusCore.TELLSTICK_ERROR_COMMUNICATING_SERVICE:
    		throw new TellStickNativeException("An error occurred while communicating with the Telldus Service.");
    	case JnaTelldusCore.TELLSTICK_ERROR_CONFIG_SYNTAX:
    		throw new TellStickNativeException("Syntax error in the configuration file.");
    	default:
    		throw new TellStickNativeException();
    	}
    	
    }
}
