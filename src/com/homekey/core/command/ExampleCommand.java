package com.homekey.core.command;

public class ExampleCommand extends Command<String> {
	
	@Override
	public void internalRun() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		result = "OMG IT WORKS!";
	}	
}
