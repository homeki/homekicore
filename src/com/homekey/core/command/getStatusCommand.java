package com.homekey.core.command;

import com.homekey.core.device.Queryable;

public class getStatusCommand extends Command<String> {
	
	private Queryable queryable;

	public getStatusCommand(Queryable d) {
		this.queryable = d;
	}

	@Override
	public void internalRun() {
		result = queryable.getStatus();
	}	
}
