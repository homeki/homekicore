package com.homekey.core.command;

import com.homekey.core.device.Queryable;

public class GetStatusCommand extends Command<String> {
	
	private Queryable queryable;

	public GetStatusCommand(Queryable d) {
		this.queryable = d;
	}
	
	@Override
	public void run() {
		result = queryable.getStatus();
		finish();
	}	
}
