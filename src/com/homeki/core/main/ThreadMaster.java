package com.homeki.core.main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.homeki.core.device.Detector;
import com.homeki.core.device.camera.CameraModule;
import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.onewire.OneWireModule;
import com.homeki.core.device.tellstick.TellStickListener;
import com.homeki.core.device.tellstick.TellStickModule;
import com.homeki.core.http.HttpApi;
import com.homeki.core.http.HttpListenerThread;
import com.homeki.core.threads.CollectorThread;
import com.homeki.core.threads.ControlledThread;
import com.homeki.core.threads.DetectorThread;

public class ThreadMaster {
	private Monitor monitor;
	private List<ControlledThread> threads;
	private List<Module> modules;
	private HttpApi api;
	private ConfigurationFile file;
	
	public ThreadMaster() {
		addShutdownHook();
	}
	
	private void addShutdownHook() {
		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new Thread() { 
			public void run() { 
				shutdown(); 
				L.i("Homekey version " + getVersion() + " exited.");
			}; 
		});
	}
	
	private String getVersion() {
		Package p = getClass().getPackage();
		String version = p.getImplementationVersion();
		
		if (version == null)
			version = "(DEV)";
		
		return version;
	}
	
	public void launch() {
		String version = getVersion();
		
		L.i("Homeki Core version " + version + " started.");
		
		file = new ConfigurationFile();
		threads = new ArrayList<ControlledThread>();
		modules = new ArrayList<Module>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		
		try {
			file.load();
		} catch (FileNotFoundException ex) {
			L.e("Could not find configuration file, killing Homeki.");
			return;
		} catch (Exception ex) {
			L.e("Exception when parsing configuration file.", ex);
			return;
		}
		
		try {
			System.loadLibrary("homekijni");
		} catch (UnsatisfiedLinkError ex) {
			L.e("Failed to load Homeki JNI library, killing Homeki.");
			return;
		}
		
		//dbf.upgrade(version);
		
		setupModules(file);
		
		List<Detector> detectors = new ArrayList<Detector>();
		
		for (Module module : modules)
			module.construct(detectors);
		
		try {
			threads.add(new HttpListenerThread(api));
		} catch (Exception e) {
			L.e("Could not bind socket for HttpListenerThread, killing Homeki.");
			return;
		}
		
		threads.add(new DetectorThread(detectors, monitor));
		threads.add(new CollectorThread(monitor));
		
		threads.add(new TellStickListener(monitor));
		
		
		for (Thread t : threads)
			t.start();
	}
	
	private void setupModules(ConfigurationFile file) {
		if (file.getBool("module.mock.use"))
			modules.add(new MockModule());
		if (file.getBool("module.tellstick.use"))
			modules.add(new TellStickModule());
		if (file.getBool("module.onewire.use"))
			modules.add(new OneWireModule(file));
		if (file.getBool("module.camera.use"))
			modules.add(new CameraModule());
		
		if (modules.size() == 0)
			L.w("No modules enabled, nothing will happen.");
	}
	
	public void shutdown() {
		L.i("Shutting down threads...");
		for (ControlledThread t : threads)
			t.shutdown();
		L.i("All threads shutdown.");
		
		L.i("Destructing modules...");
		for (Module m : modules)
			m.destruct();
		L.i("All modules destructed.");
	}
}
