package com.homeki.core.device.tellstick;

import com.homeki.core.storage.ITableFactory;

public class TellStickFakeDimmer extends TellStickSwitch {
	public TellStickFakeDimmer(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}

	@Override
	public String getType() {
		return "fakedimmer";
	}
}
