package com.homekey.core.device.onewire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.homekey.core.device.Device;

public abstract class OneWireDevice extends Device {
	private String deviceDirPath;
	
	public OneWireDevice(String internalId, String deviceDirPath) {
		super(internalId);
		this.deviceDirPath = deviceDirPath;
	}
	
	public static String getStringVar(String deviceDirPath, String var) {
		String varFilePath = deviceDirPath + "/" + var;
		File varFile = new File(varFilePath);
		Scanner varScanner;
		
		try {
			varScanner = new Scanner(varFile);
		} catch (FileNotFoundException ex) {
			System.err.println("OneWireDevice should have found var file, didn't.");
			return null;
		}
		
		return varScanner.next();
	}
	
	protected String getStringVar(String var) {
		return getStringVar(deviceDirPath, var);
	}
	
	protected float getFloatVar(String var) {
		return Float.parseFloat(getStringVar(var));
	}
}
