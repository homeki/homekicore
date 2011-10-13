package com.homeki.core.main;

import com.homeki.core.Logs;
import com.homeki.core.log.L;

public class Main {
	public static void main(String[] args) {
		L.setStandard(Logs.CORE);
		
		new ThreadMaster().launch();
	}
}
