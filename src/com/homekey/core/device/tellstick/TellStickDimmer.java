package com.homekey.core.device.tellstick;

import com.homekey.core.device.Dimmable;

public class TellStickDimmer extends TellStickSwitch implements Dimmable{
	
	public TellStickDimmer(String internalId) {
		super(internalId);
	}

	@Override
	public void dim(int level) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
