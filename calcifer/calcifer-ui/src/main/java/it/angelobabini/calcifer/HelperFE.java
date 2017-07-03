package it.angelobabini.calcifer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperFE {
	private static final SimpleDateFormat sdfYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	
	public static String dateYYYYMMDD(Date date) {
		if(date != null)
			return sdfYYYMMDD.format(date);
		else
			return "";
	}
	
	public static String dateYYYYMMDD() {
		return dateYYYYMMDD(new Date());
	}
}
