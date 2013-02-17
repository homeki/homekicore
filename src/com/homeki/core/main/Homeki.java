package com.homeki.core.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;

import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.onewire.OneWireModule;
import com.homeki.core.device.tellstick.TellStickModule;
import com.homeki.core.events.ChannelValueChangedEvent;
import com.homeki.core.events.EventHandlerThread;
import com.homeki.core.generators.ClockGeneratorThread;
import com.homeki.core.http.JsonRestletApplication;
import com.homeki.core.logging.L;
import com.homeki.core.report.ReportThread;
import com.homeki.core.storage.DatabaseManager;
import com.homeki.core.storage.Hibernate;

public class Homeki {
	private Component jsonRestletComponent;
	private Component webGuiRestletComponent;
	private ControlledThread broadcastThread;
	private ControlledThread eventHandlerThread;
	private ControlledThread clockGeneratorThread;
	private ControlledThread reportThread;
	private List<Module> modules;
	
	public Homeki() {
		modules = new ArrayList<Module>();
		addShutdownHook();
	}
	
	private void addShutdownHook() {
		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new Thread() {
			public void run() {
				Thread.currentThread().setName("ShutdownHook");
				shutdown();
				L.i("Homeki version " + Util.getVersion() + " exited.");
			};
		});
	}
	
	public void launch() {
		Thread.currentThread().setName("Homeki");
		
		L.i("Homeki version " + Util.getVersion() + " started.");
		
		// perform, if necessary, database upgrades
		try {
			new DatabaseManager().upgrade();
		} catch (ClassNotFoundException e) {
			L.e("Failed to load Postgres JDBC driver, killing Homeki.", e);
			System.exit(-1);
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
		
		// setup and start report thread reporting user statistics to GAE app
		if (Configuration.REPORTER_ENABLED) {
			try {
				reportThread = new ReportThread();
				reportThread.start();
			} catch (Exception e) {
				L.e("Could not start report thread.", e);
			}
		}
		else {
			L.w("Reporting disabled in configuration.");
		}
		
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
		
		// start JSON Restlet listener thread
		try {
			jsonRestletComponent = new Component();
			jsonRestletComponent.getServers().add(Protocol.HTTP, 5000);
			jsonRestletComponent.getDefaultHost().attach(new JsonRestletApplication());
			jsonRestletComponent.start();
		} catch (Exception e) {
			L.e("Unknown exception starting JSON restlet HTTP server.", e);
			System.exit(-1);
		}
		
		// start web GUI Restlet listener thread
		File webRoot = new File(Configuration.WEBROOT_PATH);
		if (webRoot.exists()) {
			L.i("Web root exists, starting static web server on port 8080.");
			try {
				webGuiRestletComponent = new Component();
				webGuiRestletComponent.getServers().add(Protocol.HTTP, 8080);
				webGuiRestletComponent.getClients().add(Protocol.FILE);
				webGuiRestletComponent.getDefaultHost().attach(new Directory(webGuiRestletComponent.getContext().createChildContext(), "file://" + Configuration.WEBROOT_PATH));
				webGuiRestletComponent.start();
			} catch (Exception e) {
				L.e("Unknown exception when starting static web server on port 8080.", e);
				System.exit(-1);
			}
		}
		else {
			L.i("Found no web root in " + Configuration.WEBROOT_PATH + ", skipping start of static web server.");
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
		
		// generate channel value changed event once, for init
		ChannelValueChangedEvent.generateOnce();
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
		if (jsonRestletComponent != null) {
			try {
				jsonRestletComponent.stop();
			} catch (Exception e) {
				L.e("Failed to stop JSON restlet HTTP server.", e);
			}
		}
		if (webGuiRestletComponent != null) {
			try {
				webGuiRestletComponent.stop();
			} catch (Exception e) {
				L.e("Failed to stop web GUI restlet HTTP server.", e);
			}
		}
		if (broadcastThread != null)
			broadcastThread.shutdown();
		
		L.i("Destructing modules...");
		for (Module m : modules)
			m.destruct();
		L.i("All modules destructed.");
	}
}
