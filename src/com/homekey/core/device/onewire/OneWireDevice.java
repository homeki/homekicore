package com.homekey.core.device.onewire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.homekey.core.device.Device;
import com.homekey.core.storage.Database;

public abstract class OneWireDevice extends Device {
	private String deviceDirPath;
	
	public OneWireDevice(String internalId, String deviceDirPath, Database db) {
		super(internalId, db);
		this.deviceDirPath = deviceDirPath;
	}
	
	public synchronized static String getStringVar(String deviceDirPath, String var) {
		String varFilePath = deviceDirPath + "/" + var;
		File varFile = new File(varFilePath);
		Scanner varScanner = null;
		
		try {
			varScanner = new Scanner(varFile);
			return varScanner.next();
		} catch (FileNotFoundException ex) {
			System.err.println("OneWireDevice should have found var file, didn't.");
			return null;
		}
		finally {
			if (var != null) {
				varScanner.close();
			}
		}
	}
	
	protected String getStringVar(String var) {
		return getStringVar(deviceDirPath, var);
	}
	
	protected float getFloatVar(String var) {
		return Float.parseFloat(getStringVar(var));
	}
}
