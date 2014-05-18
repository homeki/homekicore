package com.homeki.core.device.zwave;

import com.homeki.core.logging.L;
import org.zwave4j.*;

import java.io.File;

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

	public String getValueLabel(ValueId vid) {
		return manager.getValueLabel(vid);
	}

	private String findController() {
		if (new File("/dev/zstick").exists()) return "/dev/zstick";
		if (new File("/dev/ttyAMA0").exists()) return "/dev/ttyAMA0";
		L.w("No Z-Wave controller found, Z-Wave module not initialized.");
		return null;
	}
}
