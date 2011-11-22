package com.homeki.core.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.homeki.core.log.L;

/*
 * Configuration file format (shows all possible settings):
 * 
 * [detectors]
 * detector.mock.use = true
 * detector.tellstick.use = true
 * detector.tellstick.path = /etc/tellstick.conf
 * detector.onewire.use = true
 * detector.onewire.path = /mnt/1wire/uncached
 * detector.camera.use = true
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
			
			String key;
			String value;
			
			try {
				key = line.next().trim();
				
				if (line.hasNext()) {
					value = line.next().trim();
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
