package com.estimote.examples.demos.utils;

import java.util.HashMap;
import java.util.Map;

public class MacAddressBeaconIdentifier implements BeaconNameDecoder<String> {

	/**
	 * specific iBeacon's
	 */
	@SuppressWarnings("serial")
	private Map<String, String> iBeaconsMap = new HashMap<String, String>(){{
		put("F5:3E:34:8E:63:E6", "mint coctail");
		put("F2:EC:58:FB:A3:24", "blueberry pie");
		put("ED:6E:56:B9:93:E7", "icy marshmallow");
		put("F8:D9:6C:91:F9:92", "blueberry pie 2");		
	}};
	
	@Override
	public String getNameByIdentifier(String mac) {
		String name = iBeaconsMap.get(mac);
		return name != null ? name : UNKNOWN;
	}

}
