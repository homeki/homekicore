package com.homeki.core.device.zwave;

import com.homeki.core.device.DataType;
import com.homeki.core.logging.L;
import org.zwave4j.*;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public enum ZWaveApi {
	INSTANCE;

	private Manager manager;
	private ZWaveWatcher watcher;
	private String controllerPort;
	private long homeId;

	private ZWaveApi() {
		NativeLibraryLoader.loadLibrary(ZWave4j.LIBRARY_NAME, ZWave4j.class);
	}

	public void open() {
		controllerPort = findController();
		if (controllerPort == null) return;

		L.i("Connecting to Z-Wave controller " + controllerPort + ".");

		Options options = Options.create("./", "", "");
		options.addOptionBool("ConsoleOutput", false);
		options.lock();

		watcher = new ZWaveWatcher();
		manager = Manager.create();
		manager.addWatcher(watcher, null);
		manager.addDriver(controllerPort);
	}

	public void close() {
		if (manager == null) return;
		manager.removeDriver(controllerPort);
		Manager.destroy();
		Options.destroy();
	}

	public void ready(long homeId) {
		this.homeId = homeId;
	}

	public int getIntValue(ValueId vid) {
		switch (vid.getType()) {
			case BYTE:
				AtomicReference<Short> bb = new AtomicReference<>();
				Manager.get().getValueAsByte(vid, bb);
				return bb.get();
			case INT:
				AtomicReference<Integer> i = new AtomicReference<>();
				Manager.get().getValueAsInt(vid, i);
				return i.get();
			case SHORT:
				AtomicReference<Short> s = new AtomicReference<>();
				Manager.get().getValueAsShort(vid, s);
				return s.get();
			default:
				L.e("Wrong value type, will return 0.");
				return 0;
		}
	}

	public boolean getBoolValue(ValueId vid) {
		switch (vid.getType()) {
			case BOOL:
				AtomicReference<Boolean> b = new AtomicReference<>();
				Manager.get().getValueAsBool(vid, b);
				return b.get();
			default:
				L.e("Wrong value type, will return false.");
				return false;
		}
	}

	public double getDoubleValue(ValueId vid) {
		switch (vid.getType()) {
			case DECIMAL:
				AtomicReference<Float> b = new AtomicReference<>();
				Manager.get().getValueAsFloat(vid, b);
				return b.get();
			default:
				L.e("Wrong value type, will return 0.0.");
				return 0.0;
		}
	}

	public String getValueLabel(ValueId vid) {
		return manager.getValueLabel(vid);
	}

	public static DataType convertValueType(ValueType valueType) {
		switch (valueType) {
			case BOOL:
				return DataType.BOOL;
			case BYTE:
				return DataType.BYTE;
			case DECIMAL:
				return DataType.DOUBLE;
			case INT:
			case SHORT:
				return DataType.INT;
			case STRING:
				return DataType.STRING;
			default:
				return null;
		}
	}

	private String findController() {
		if (new File("/dev/zstick").exists()) return "/dev/zstick";
		if (new File("/dev/ttyAMA0").exists()) return "/dev/ttyAMA0";
		L.w("No Z-Wave controller found, Z-Wave module not initialized.");
		return null;
	}
}
