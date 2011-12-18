package com.homeki.core.device.tellstick;


public class TellStickFakeDimmer extends TellStickSwitch {
	public TellStickFakeDimmer(String internalId) {
		super(internalId);
	}

	@Override
	public String getType() {
		return "fakedimmer";
	}
}
