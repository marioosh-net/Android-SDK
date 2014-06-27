package com.estimote.examples.demos.utils;

public interface BeaconNameDecoder<T> {
	public static final String UNKNOWN = "unknown";
	public static final String MINT_COCTAIL = "mint coctail";
	public static final String BLUEBERRY_PIE = "blueberry pie";
	public static final String ICY_MARSHMALLOW = "icy marshmallow";
	public static final String BLUEBERRY_PIE_2 = "blueberry pie 2";
	
	String getNameByIdentifier(T beaconIdentifier);
}
