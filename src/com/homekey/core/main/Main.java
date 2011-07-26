package com.homekey.core.main;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CommandsThread ct = new CommandsThread();
		ct.start();
		ExampleCommand ec = new ExampleCommand();
		ct.post(ec);
		
		System.out.println(ec.getResult());
		
		
		
	}

}
