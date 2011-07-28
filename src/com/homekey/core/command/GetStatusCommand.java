package com.homekey.core.command;

import com.homekey.core.device.Queryable;
import com.homekey.core.http.json.JsonStatus;
import com.homekey.core.main.InternalData;

public class GetStatusCommand extends Command<Object> {
	private int id;

	public GetStatusCommand(int id) {
		this.id = id;
	}

	@Override
	public void internalRun(InternalData data) {
		Queryable<?> q = data.getQueryable(id);
		setResult(new JsonStatus<Object>( q.getValue() ));
	}
}
