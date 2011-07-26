package com.homekey.core.main;

import com.homekey.core.device.Queryable;

public class getStatusCommand extends Command<String> {
	
	private Queryable queryable;

	public getStatusCommand(Queryable d) {
		this.queryable = d;
	}
	
	@Override
	public void run() {
		result = queryable.getStatus();
		finish();
	}	
}
