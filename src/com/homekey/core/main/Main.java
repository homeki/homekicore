package com.homekey.core.main;

import com.homekey.core.Logs;
import com.homekey.core.log.L;

public class Main {
	public static void main(String[] args) {
		L.setStandard(Logs.CORE);
		
		new ThreadMaster().launch();
	}
}
