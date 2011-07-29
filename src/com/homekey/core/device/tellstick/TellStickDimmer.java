package com.homekey.core.device.tellstick;

import com.homekey.core.device.Dimmable;
import com.homekey.core.storage.Database;

public class TellStickDimmer extends TellStickSwitch implements Dimmable {
	public TellStickDimmer(String internalId, Database db) {
		super(internalId, db);
	}

	@Override
	public void dim(int level) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
