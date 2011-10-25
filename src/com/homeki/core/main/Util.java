package com.homeki.core.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import com.homeki.core.log.L;

public class Util {
	public static String fromFile(String filePath) {
		FileInputStream fis = null;
		String result = "";
		
		try {
			fis = new FileInputStream(filePath);
			FileChannel fc = fis.getChannel();
			
			ByteBuffer bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
			CharBuffer cbuf = Charset.forName("8859_1").newDecoder().decode(bbuf);
			result = cbuf.toString();
		} catch (Exception ex) {
			L.e("Problem reading file.", ex);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
					L.e("Problem closing file.", ex);
				}
			}
		}
		
		return result;
	}
}
