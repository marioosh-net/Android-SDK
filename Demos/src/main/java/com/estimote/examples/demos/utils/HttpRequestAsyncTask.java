package com.estimote.examples.demos.utils;

import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

public class HttpRequestAsyncTask extends AsyncTask<String, Void, String> {

	private static final String SERVER_URL = "http://localhost:8001/data";
	private List<Beacon> beacons;
	private MacAddressBeaconIdentifier macNameDecoder = new MacAddressBeaconIdentifier();
	
	public HttpRequestAsyncTask(List<Beacon> beacons) {
		this.beacons = beacons;
	}
	
	protected void onPostExecute(String result) {
		// Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		Log.d("HTTPREQ", result);
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost(SERVER_URL);
			JSONObject t = new JSONObject();
			
			/**
			 * distance from beacons in JSON
			 * {"ibeacon name":"2.34", "ibeacon2 name":"1.3", ....}
			 */
			for(Beacon b: beacons) {
				t.put(macNameDecoder.getNameByIdentifier(b.getMacAddress()), Utils.computeAccuracy(b));
			}
			StringEntity se = new StringEntity(t.toString());
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
		    httpost.setHeader("Content-type", "application/json");
		    
		    ResponseHandler responseHandler = new BasicResponseHandler();
		    return httpclient.execute(httpost, responseHandler);
		    
		} catch (Exception e) {
			Log.e("ERROR", e.toString());
		}
		return null;

	}

}
