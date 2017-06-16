package it.angelobabini.calcifer.backend;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

	public static File tempFolder() {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("tmp_", ".tmp");
			return tempFile.getParentFile();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				tempFile.deleteOnExit();
			} catch(Exception e) {}
			try {
				tempFile.delete();
			} catch(Exception e) {}
		}
		return tempFile;
	}
	
	public static File tempFile(String filename) {
		return new File(tempFolder(), filename);
	}
	
	public static SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	public static String dateYYYYMMDD(Date date) {
		return date == null ? "" : sdfYYYYMMDD.format(date);
	}
}
