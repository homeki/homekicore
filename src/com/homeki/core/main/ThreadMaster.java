package com.homeki.core.main;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.device.TimerThread;
import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.onewire.OneWireModule;
import com.homeki.core.device.tellstick.TellStickModule;
import com.homeki.core.http.HttpListenerThread;
import com.homeki.core.storage.DatabaseUpgrader;
import com.homeki.core.storage.Hibernate;

public class ThreadMaster {
	private ControlledThread httpThread;
	private ControlledThread timerThread;
	private List<Module> modules;
	
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
		Thread.currentThread().setName("Main");
		
		L.i("Homeki Core version " + getVersion() + " started.");

		// instantiate
		modules = new ArrayList<Module>();
		
		// perform, if necessary, database upgrades
		try {
			new DatabaseUpgrader().upgrade();
		}
		catch (ClassNotFoundException ex) {
			L.e("Failed to load HSQLDB JDBC driver, killing Homeki.", ex);
		}
		catch (Exception ex) {
			L.e("Database upgrade failed, killing Homeki.", ex);
			return;
		}
		L.i("Database version up to date.");
		
		// load native JNI library
		try {
			System.loadLibrary("homekijni");
		} catch (UnsatisfiedLinkError ex) {
			L.e("Failed to load Homeki JNI library, killing Homeki.");
			return;
		}
		L.i("Native JNI library loaded.");
		
		// init Hibernate functionality
		try {
			Hibernate.init();
		}
		catch (Exception ex) {
			L.e("Something went wrong when verifying access to database through Hibernate, killing Homeki.", ex);
			return;
		}
		L.i("Database access through Hibernate verified.");
		
		setupModules();
		
		for (Module module : modules)
			module.construct();
		
		try {
			httpThread = new HttpListenerThread();
			httpThread.start();
		} catch (Exception e) {
			L.e("Could not bind socket for HttpListenerThread, killing Homeki.");
			return;
		}
		
		try {
			timerThread = new TimerThread();
			timerThread.start();
		} catch (Exception e) {
			L.e("Could not start TimerThread, killing Homeki.");
			return;
		}
	}
	
	private void setupModules() {
		if (Configuration.MOCK_ENABLED) {
			L.i("Mock module enabled.");
			modules.add(new MockModule());
		}
			
		modules.add(new TellStickModule());
		modules.add(new OneWireModule());
	}
	
	public void shutdown() {
		L.i("Shutting down threads...");
		if (httpThread != null)
			httpThread.shutdown();
		if (timerThread != null)	
			timerThread.shutdown();
		
		L.i("Destructing modules...");
		for (Module m : modules)
			m.destruct();
		L.i("All modules destructed.");
	}
}
