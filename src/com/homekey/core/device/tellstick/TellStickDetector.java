package com.homekey.core.device.tellstick;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.homekey.core.device.Detector;
import com.homekey.core.device.Device;

public class TellStickDetector extends Detector {
	private final static String PATH = "/etc/tellstick.conf";
	
	public CharSequence fromFile(String filename) throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		FileChannel fc = fis.getChannel();
		
		// Create a read-only CharBuffer on the file
		ByteBuffer bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
		CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
		return cbuf;
	}
	
	private static final String valueFinder = "%s\\s*?=\\s*?(\\w+|\".*?\")";
	private static final String deviceFinder = "device?\\s*\\{(.*?)parameters";
	
	@Override
	public List<Device> findDevices() {
		List<Device> devices = new ArrayList<Device>();
		try {
			Pattern idFinder = Pattern.compile(String.format(valueFinder, "id"));
			Pattern modelFinder = Pattern.compile(String.format(valueFinder, "model"));
			
			Pattern p = Pattern.compile(deviceFinder, Pattern.DOTALL);
			Matcher m = p.matcher(fromFile(PATH));
			while (m.find()) {
				String match = m.group(1);
				String id = getMatch(match, idFinder);
				String model = getMatch(match, modelFinder);
			
				if (model.equals("selflearning-switch")) {
					devices.add(new TellStickSwitch(id));
				} else if (model.equals("selflearning-dimmer")){
					devices.add(new TellStickDimmer(id));
				} else {
					System.err.println("Unknown device!");
				}
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return devices;
		
	}
	
	private String getMatch(String string, Pattern p) {
		Matcher m = p.matcher(string);
		if (m.find())
			return m.group(1);
		return null;
	}
}
