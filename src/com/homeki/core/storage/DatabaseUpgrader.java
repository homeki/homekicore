package com.homeki.core.storage;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.log.L;
import com.homeki.core.storage.versions.To0_0_20;

public class DatabaseUpgrader {
	private List<DatabaseVersion> versions;
	private DatabaseVersion fromVersion;
	
	public DatabaseUpgrader(String databasePath, String fromVersion) {
		this.versions = new ArrayList<DatabaseVersion>();
		this.fromVersion = new DatabaseVersion(fromVersion);
	}
	
	public boolean execute() {
		//versions.add(new To0_0_20(databasePath));
		
		boolean upgradeFromNow = false;
		
		for (int i = 0; i < versions.size(); i++) {
			DatabaseVersion v = versions.get(i);
			
			if (v.compareTo(fromVersion) > 0)
				upgradeFromNow = true;
			
			if (upgradeFromNow) {
				v.run();
				L.i("Upgraded database to version " + v + ".");
			}
		}
		
		return upgradeFromNow;
	}
}
