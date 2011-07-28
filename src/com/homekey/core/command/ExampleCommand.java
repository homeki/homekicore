package com.homekey.core.command;

import com.homekey.core.main.InternalData;

public class ExampleCommand extends Command<String> {
	@Override
	public void internalRun(InternalData data) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setResult("OMG IT WORKS!");
	}	
}
