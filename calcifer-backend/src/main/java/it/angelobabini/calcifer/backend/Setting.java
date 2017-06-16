package it.angelobabini.calcifer.backend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Setting implements Serializable {
	private static final long serialVersionUID = 9106782590772796666L;
	
	private static final Map<String,Object> map = new HashMap<String,Object>();
	static {
		map.put("PHOTO_MAX_SIZE", 2000);
		map.put("PHOTO_BASE_URL", "http://www.angelobabini.it/calcifer/capisaldo/");
		map.put("PHOTO_STORAGE_URL", "ftp://www.angelobabini.it:21/www.angelobabini.it/calcifer/capisaldo/");
		map.put("PHOTO_STORAGE_USERNAME", "1384980@aruba.it");
		map.put("PHOTO_STORAGE_PASSWORD", "b3e85acb27");
		map.put("PHOTO_THUMB_WIDTH", "200px");
		map.put("PHOTO_THUMB_HEIGHT", "150px");
		
		map.put("TEST_INT", 10);
		map.put("TEST_STRING", "2000");
		map.put("TEST_DOUBLE", 2.5);
	}
	
	public static Object getRaw(String key) {
		if(!map.containsKey(key))
			return null;
		else
			return map.get(key);
	}

	public static String getAsString(String key) {
		Object raw = getRaw(key);
		if(raw instanceof String)
			return (String) raw;
		else
			return String.valueOf(raw);
	}

	public static Integer getAsInt(String key) {
		Object raw = getRaw(key);
		if(raw instanceof Integer)
			return (Integer) raw;
		else {
			try {
				return Integer.parseInt(String.valueOf(raw));
			} catch(Exception e) {
				return null;
			}
		}
	}

	public static Double getAsDouble(String key) {
		Object raw = getRaw(key);
		if(raw instanceof Double)
			return (Double) raw;
		else {
			try {
				return Double.parseDouble(String.valueOf(raw));
			} catch(Exception e) {
				return null;
			}
		}
	}
}
