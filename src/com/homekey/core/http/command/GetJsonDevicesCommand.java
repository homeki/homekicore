package com.homekey.core.http.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homekey.core.command.Command;
import com.homekey.core.http.json.JsonDevice;
import com.homekey.core.main.InternalData;

public class GetJsonDevicesCommand extends Command<String> {
	
	@Override
	public void internalRun(InternalData data) {
		Gson builder = new GsonBuilder().setPrettyPrinting().create();
		setResult(builder.toJson(JsonDevice.makeArray(data.getDevices())));
	}
}
