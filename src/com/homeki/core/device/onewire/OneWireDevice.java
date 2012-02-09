package com.homeki.core.device.onewire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.persistence.Entity;

import com.homeki.core.device.Device;
import com.homeki.core.main.Configuration;

@Entity
public abstract class OneWireDevice extends Device {
	public OneWireDevice() {
		
	}
	
	protected static String getStringVar(String deviceDirPath, String var) {
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
		return getStringVar(Configuration.ONEWIRE_PATH + "/" + internalId, var);
	}
	
	protected double getDoubleVar(String var) {
		return Double.parseDouble(getStringVar(var));
	}
}
