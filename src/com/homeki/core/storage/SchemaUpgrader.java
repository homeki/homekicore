package com.homeki.core.storage;

import java.util.ArrayList;
import java.util.List;

import com.homeki.core.main.L;

public class SchemaUpgrader {
	private List<SchemaVersion> versions;
	private SchemaVersion fromVersion;
	
	public SchemaUpgrader(String fromVersion) {
		this.versions = new ArrayList<SchemaVersion>();
		this.fromVersion = new SchemaVersion(fromVersion);
	}
	
	public boolean execute() {
		//versions.add(new To0_0_20(databasePath));
		
		boolean upgradeFromNow = false;
		
		for (int i = 0; i < versions.size(); i++) {
			SchemaVersion v = versions.get(i);
			
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
