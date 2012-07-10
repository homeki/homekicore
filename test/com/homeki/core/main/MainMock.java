package com.homeki.core.main;


public class MainMock {
	public static void main(String[] args) {
		Configuration.MOCK_ENABLED = true;
		L.i("MainMock: Enabled mock devices.");
		
		new ThreadMaster().launch();
	}
}
