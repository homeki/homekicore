package com.homeki.core.main;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.onewire.OneWireModule;
import com.homeki.core.device.tellstick.TellStickModule;
import com.homeki.core.http.HttpListenerThread;
import com.homeki.core.storage.DatabaseUpgrader;
import com.homeki.core.storage.Hibernate;

public class ThreadMaster {
	private ControlledThread httpThread;
	private ControlledThread timerThread;
	private ControlledThread broadcastThread;
	private List<Module> modules;
	
	public ThreadMaster() {
		modules = new ArrayList<Module>();
		addShutdownHook();
	}
	
	private void addShutdownHook() {
		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new Thread() {
			public void run() {
				Thread.currentThread().setName("Main");
				shutdown();
				L.i("Homeki version " + Util.getVersion() + " exited.");
			};
		});
	}
	
	public void launch() {
		Thread.currentThread().setName("Main");
		
		L.i("Homeki version " + Util.getVersion() + " started.");
		
		// perform, if necessary, database upgrades
		try {
			new DatabaseUpgrader().upgrade();
		} catch (ClassNotFoundException e) {
			L.e("Failed to load HSQLDB JDBC driver, killing Homeki.", e);
		} catch (Exception e) {
			L.e("Database upgrade failed, killing Homeki.", e);
			System.exit(-1);
		}
		L.i("Database version up to date.");
		
		// init Hibernate functionality
		try {
			Hibernate.init();
		} catch (Exception e) {
			L.e("Something went wrong when verifying access to database through Hibernate, killing Homeki.", e);
			System.exit(-1);
		}
		L.i("Database access through Hibernate verified.");
		
		// load native JNI library
		try {
			System.loadLibrary("homekijni");
		} catch (UnsatisfiedLinkError e) {
			L.e("Failed to load Homeki JNI library, killing Homeki.", e);
			System.exit(-1);
		}
		L.i("Native JNI library loaded.");
		
		// setup and construct modules
		setupModules();
		
		for (Module module : modules) {
			try {
				module.construct();
			} catch (Exception e) {
				L.e("Failed to construct " + module.getClass().getSimpleName() + ".", e);
			}
		}
		L.i("Modules constructed.");
		
		// start HTTP listener socket
		try {
			httpThread = new HttpListenerThread();
			httpThread.start();
		} catch (Exception e) {
			L.e("Could not bind socket for HttpListenerThread, killing Homeki.");
			System.exit(-1);
		}
		
		// start broadcast listener thread
		try {
			broadcastThread = new BroadcastListenerThread();
			broadcastThread.start();
		} catch (Exception e) {
			L.e("Could not start BroadcastListenerThread.", e);
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
		if (broadcastThread != null)
			broadcastThread.shutdown();
		
		L.i("Destructing modules...");
		for (Module m : modules)
			m.destruct();
		L.i("All modules destructed.");
	}
}
