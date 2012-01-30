package com.homeki.core.main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.homeki.core.device.mock.MockDimmer;
import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.onewire.OneWireModule;
import com.homeki.core.device.tellstick.TellStickModule;
import com.homeki.core.http.HttpApi;
import com.homeki.core.http.HttpListenerThread;
import com.homeki.core.storage.DatabaseUpgrader;
import com.homeki.core.storage.Hibernate;
import com.homeki.core.storage.entities.DimmerHistoryPoint;

public class ThreadMaster {
	private Monitor monitor;
	private ControlledThread httpThread;
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
		Thread.currentThread().setName("Main");
		
		L.i("Homeki Core version " + getVersion() + " started.");

		// instantiate
		file = new ConfigurationFile();
		modules = new ArrayList<Module>();
		monitor = new Monitor();
		api = new HttpApi(monitor);
		
		// load and check configuration file
		try {
			file.load();
		} catch (FileNotFoundException ex) {
			L.e("Could not find configuration file, killing Homeki.");
			return;
		} catch (Exception ex) {
			L.e("Exception when parsing configuration file.", ex);
			return;
		}
		L.i("Configuration file read.");
		
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
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Session session = Hibernate.openSession();
		
		//Hibernate.closeSession(session);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		setupModules();
		
		for (Module module : modules)
			module.construct(monitor, file);
		
		try {
			httpThread = new HttpListenerThread(api);
			httpThread.start();
		} catch (Exception e) {
			L.e("Could not bind socket for HttpListenerThread, killing Homeki.");
			return;
		}
	}
	
	private void setupModules() {
		if (file.getBool("module.mock.use"))
			modules.add(new MockModule());
		if (file.getBool("module.tellstick.use"))
			modules.add(new TellStickModule());
		if (file.getBool("module.onewire.use"))
			modules.add(new OneWireModule());
		
		if (modules.size() == 0)
			L.w("No modules enabled, nothing will happen.");
	}
	
	public void shutdown() {
		L.i("Destructing modules...");
		for (Module m : modules)
			m.destruct();
		L.i("All modules destructed.");
	}
}
