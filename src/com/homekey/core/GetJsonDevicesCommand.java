package com.homekey.core;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.homekey.core.command.Command;
import com.homekey.core.device.Device;
import com.homekey.core.main.InternalData;

public class GetJsonDevicesCommand extends Command<String> {

	@Override
	public void internalRun(InternalData data) {
		JsonObject jo = new JsonObject();
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		for (Device d : data.getDevices())
			jo.add(d.getClass().getSimpleName(), g.toJsonTree(d));
		setResult(g.toJson(jo));
	}
	
}
