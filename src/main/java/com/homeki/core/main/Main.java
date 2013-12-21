package com.homeki.core.main;

public class Main {
	public static void main(String[] args) {
    if (Util.isTestVersion())
      Configuration.transformForTest();
		else if (Util.isDevVersion())
			Configuration.transformForDev();
		
		new Homeki().launch();
	}
}
