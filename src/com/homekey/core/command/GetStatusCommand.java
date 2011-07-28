package com.homekey.core.command;

import com.homekey.core.device.Queryable;
import com.homekey.core.main.InternalData;

public class GetStatusCommand<T> extends Command<T> {
	
	private Queryable<T> queryable;

	public GetStatusCommand(Queryable<T> d) {
		if(d == null)
			throw new RuntimeException("Queryable can not be null!");
		this.queryable = d;
	}

	@Override
	public void internalRun(InternalData data) {
		System.out.println(queryable.toString());
		setResult(queryable.getValue());
	}	
}
