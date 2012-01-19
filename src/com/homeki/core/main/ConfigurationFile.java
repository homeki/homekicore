package com.homeki.core.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/*
 * Configuration file format (shows all possible settings):
 * 
 * [modules]
 * module.mock.use = true
 * module.tellstick.use = true
 * module.tellstick.allowedsensors = 12,145,543
 * module.onewire.use = true
 * module.onewire.path = /mnt/1wire/uncached
 * 
 */

public class ConfigurationFile {
	private Map<String, String> values;
	
	public ConfigurationFile() {
		values = new HashMap<String, String>();
	}
	
	public void load() throws FileNotFoundException {
		File f = new File("homeki.conf");
		Scanner sc = new Scanner(f);
		
		while (sc.hasNextLine()) {
			String text = sc.nextLine().trim();
			
			if (text.isEmpty() || text.startsWith("[") || text.startsWith("#"))
				continue;
			
			Scanner line = new Scanner(text);
			line.useDelimiter("=");
			
			try {
				String key = line.next().trim();
				
				if (line.hasNext()) {
					String value = line.next().trim();
					values.put(key, value);	
				}
			} catch (Exception ex) {
				L.e("Failed parsing one of the keys in the configuration file.");
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
		String value = null;
		
		try {
			value = values.get(key);
		} catch (Exception ex) { }
		
		// check and set to empty here as values.get() can return null
		if (value == null)
			value = "";
		
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
