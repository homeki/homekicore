package com.homeki.core.main;

import com.homeki.core.Logs;
import com.homeki.core.log.L;

public class Main {
	public static void main(String[] args) {
		L l = L.setStandard(Logs.CORE);
		l.setMinimumLevel(L.LEVEL_DEBUG);
		l.addOutput(System.err, L.LEVEL_WARN, true, true);
		
		new ThreadMaster().launch();
	}
}
