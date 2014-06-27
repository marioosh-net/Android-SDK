package com.estimote.examples.demos.utils;

public interface BeaconNameDecoder<T> {
	public static final String UNKNOWN = "unknown";
	
	String getNameByIdentifier(T beaconIdentifier);
}
