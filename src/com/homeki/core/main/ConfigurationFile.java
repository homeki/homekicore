package com.homeki.core.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigurationFile {
	private Map<String, String> values;
	
	public ConfigurationFile() {
		values = new HashMap<String, String>();
	}
	
	public void load() throws FileNotFoundException {
		File f = new File("homeki.conf");
		Scanner sc = new Scanner(f);
		
		while (sc.hasNextLine()) {
			Scanner line = new Scanner(sc.nextLine());
			line.useDelimiter("=");
			
			String key = line.next().trim();
			String value;
			
			if (!key.startsWith("[")) {
				value = line.next().trim();
			
				if (!key.startsWith("#")) {
					values.put(key, value);
				}
			}
		}
	}
	
	public int getInt(String key) {
		int id;
		
		try {
			id = Integer.parseInt(values.get(key));
		} catch (Exception ex) {
			id = -1;
		}
		
		return id;
	}
	
	public String getString(String key) {
		String value;
		
		try {
			value = values.get(key);
		} catch (Exception ex) {
			value = "";
		}
		
		return value;
	}
	
	public boolean getBool(String key) {
		boolean bool;
		
		try {
			bool = Boolean.parseBoolean(values.get(key));
		} catch (Exception ex) {
			bool = false;
		}
		
		return bool;
	}
	
	public boolean contains(String key) {
		return values.containsKey(key);
	}
}
