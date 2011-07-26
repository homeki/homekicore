package com.homekey.core.command;

public class ExampleCommand extends Command<String> {
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		result = "OMG IT WORKS!";
		finish();
	}	
}
