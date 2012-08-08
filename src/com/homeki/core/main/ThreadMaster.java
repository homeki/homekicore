package com.homeki.core.main;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.Component;
import org.restlet.data.Protocol;

import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.onewire.OneWireModule;
import com.homeki.core.device.tellstick.TellStickModule;
import com.homeki.core.events.EventHandlerThread;
import com.homeki.core.generators.ClockGeneratorThread;
import com.homeki.core.http.HttpListenerThread;
import com.homeki.core.http.RestletApplication;
import com.homeki.core.storage.DatabaseManager;
import com.homeki.core.storage.Hibernate;

public class ThreadMaster {
	private ControlledThread httpThread;
	private ControlledThread broadcastThread;
	private ControlledThread eventHandlerThread;
	private ControlledThread clockGeneratorThread;
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
	
	private void configureLogging() {
		Logger log = Logger.getLogger("");
		Handler[] handlers = log.getHandlers();
		for (Handler h : handlers)
			if (h instanceof ConsoleHandler)
				h.setFormatter(new CustomLogFormatter());
	}
	
	public void launch() {
		Thread.currentThread().setName("Main");
		configureLogging();
		
		L.i("Homeki version " + Util.getVersion() + " started.");
		
		// perform, if necessary, database upgrades
		try {
			new DatabaseManager().upgrade();
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
			L.e(e.getStackTrace().toString());
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
		
		// start Restlet listener thread
		try {
			Component comp = new Component();
			comp.getServers().add(Protocol.HTTP, 5001);
			comp.getDefaultHost().attach(new RestletApplication());
			comp.getLogger().setLevel(Level.SEVERE);
			comp.start();
		} catch (Exception e) {
			L.e("Unknown exception starting RestletListenerThread.", e);
			System.exit(-1);
		}
		
		// start broadcast listener thread
		try {
			broadcastThread = new BroadcastListenerThread();
			broadcastThread.start();
		} catch (Exception e) {
			L.e("Could not start BroadcastListenerThread.", e);
		}
		
		// start event handler thread
		try {
			eventHandlerThread = new EventHandlerThread();
			eventHandlerThread.start();
		} catch (Exception e) {
			L.e("Could not start EventHandlerThread.", e);
		}
		
		// start clock generator thread
		try {
			clockGeneratorThread = new ClockGeneratorThread();
			clockGeneratorThread.start();
		} catch (Exception e) {
			L.e("Could not start ClockGeneratorThread.", e);
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
		if (broadcastThread != null)
			broadcastThread.shutdown();
		
		L.i("Destructing modules...");
		for (Module m : modules)
			m.destruct();
		L.i("All modules destructed.");
	}
}
