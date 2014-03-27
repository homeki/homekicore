package com.homeki.core.main;

import com.homeki.core.device.mock.MockModule;
import com.homeki.core.device.onewire.OneWireModule;
import com.homeki.core.device.tellstick.TellStickModule;
import com.homeki.core.events.ChannelValueChangedEvent;
import com.homeki.core.events.EventHandlerModule;
import com.homeki.core.generators.GeneratorModule;
import com.homeki.core.http.RestApiModule;
import com.homeki.core.logging.L;
import com.homeki.core.report.ReportModule;
import com.homeki.core.storage.DatabaseManager;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Homeki {
	private List<Module> modules;
	
	public Homeki() {
		modules = new ArrayList<>();
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
		
		upgradeDatabase();
		initHibernate();
		ensureServerUuid();
		constructModules();
		
		// generate channel value changed event once, for init
		ChannelValueChangedEvent.generateOnce();
	}

	private void constructModules() {
		modules.add(new ReportModule());
		modules.add(new EventHandlerModule());
		modules.add(new MockModule());
		modules.add(new TellStickModule());
		modules.add(new OneWireModule());
		modules.add(new RestApiModule());
		modules.add(new BroadcastModule());
		modules.add(new GeneratorModule());

		for (Module module : modules) {
			try {
				L.i("Constructing module " + module.getClass().getSimpleName() + ".");
				module.construct();
			} catch (Exception e) {
				L.e("Failed to construct " + module.getClass().getSimpleName() + ".", e);
			}
		}
		L.i("Modules constructed.");
	}

	private void initHibernate() {
		try {
			Hibernate.init();
		} catch (Exception e) {
			L.e("Something went wrong when verifying access to database through Hibernate, killing Homeki.", e);
			L.e(e.getStackTrace().toString());
			System.exit(-1);
		}
		L.i("Database access through Hibernate verified.");
	}

	private void upgradeDatabase() {
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
	}

	private void ensureServerUuid() {
		Session session = Hibernate.openSession();
		String uuid = Setting.getString(session, Setting.SERVER_UUID);

		if (!uuid.isEmpty()) {
			L.i("Server UUID is " + uuid + ".");
			return;
		}

		uuid = UUID.randomUUID().toString();
		Setting.putString(session, Setting.SERVER_UUID, uuid);
		Hibernate.closeSession(session);
		L.i("New server UUID " + uuid + " generated and stored.");
	}

	public void shutdown() {
		for (Module m : modules) {
			L.i("Destructing module " + m.getClass().getSimpleName() + ".");
			m.destruct();
		}
		L.i("Modules destructed");
	}
}
