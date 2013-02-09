package com.homeki.core.main;

import com.homeki.core.logging.L;


public class MainMock {
	public static void main(String[] args) {
		Configuration.MOCK_ENABLED = true;
		L.i("MainMock: Enabled mock devices.");
		
		new ThreadMaster().launch();
	}
}
