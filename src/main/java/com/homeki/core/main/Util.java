package com.homeki.core.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homeki.core.logging.L;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;


public class Util {
  private static boolean testVersion;

  public static String fromFile(String filePath) {
		FileInputStream fis = null;
		String result = "";
		
		try {
			fis = new FileInputStream(filePath);
			FileChannel fc = fis.getChannel();
			
			ByteBuffer bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
			CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
			result = cbuf.toString();
		} catch (Exception e) {
			L.e("Problem reading file.", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					L.e("Problem closing file.", e);
				}
			}
		}
		
		return result;
	}
	
	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public static SimpleDateFormat getDateTimeFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static String getVersion() {
		Package p = new Util().getClass().getPackage();
		String version = p.getImplementationVersion();

		if (version == null)
			version = "SNAPSHOT";

		return version;
	}

	public static boolean isDevVersion() {
		return getVersion().endsWith("SNAPSHOT");
	}

	public static Gson constructGson() {
		return new GsonBuilder()
		.setPrettyPrinting()
		.setDateFormat(Util.getDateTimeFormat().toPattern())
		.create();
	}
	
	public static void sleep(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			L.e("Util.sleep aborted due to InterruptedException.");
		}
	}
	
	public static String byteArrayToHex(byte[] a) {
	   StringBuilder sb = new StringBuilder();
	   for(byte b : a)
	      sb.append(String.format("%02x", b&0xff));
	   return sb.toString();
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}
	
	public static boolean isNotNullAndEmpty(String s) {
		return s != null && s.length() == 0;
	}

  public static boolean isTestVersion() {
    return System.getProperty("test") != null;
  }
}
