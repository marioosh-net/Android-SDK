package com.estimote.examples.demos.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import localization.LocalizationException;
import localization.MultiplePointsLocalization;
import localization.Point;
import localization.PointId;
import localization.PointIdDistance;

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

	private static final String SERVER_URL = "http://marioosh.net:8081/data";
	private List<Beacon> beacons;
	private MacAddressBeaconIdentifier macNameDecoder = new MacAddressBeaconIdentifier();
	
	public HttpRequestAsyncTask(List<Beacon> beacons) {
		this.beacons = beacons;
	}
	
	protected void onPostExecute(String result) {
		// Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		//Log.d("HTTPREQ", result);
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
			Map<String, Double> m = new HashMap<String, Double>();
			for(Beacon b: beacons) {
				String bName = macNameDecoder.getNameByIdentifier(b.getMacAddress());
				double metry = Utils.computeAccuracy(b);
				m.put(bName, metry);
				
				t.put(bName, metry);
			}
			
			Uklad1 u = new Uklad1();
			Point p = u.licz(m);
			Log.d("POINT",p.toString());
			
			JSONObject p1 = new JSONObject();
			p1.put("x", p.x);
			p1.put("y", p.y);
			t.put("point", p1);
			
			Log.d("JSON", t.toString());
			
			StringEntity se = new StringEntity(t.toString());
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
		    httpost.setHeader("Content-type", "application/json");
		    
		    ResponseHandler responseHandler = new BasicResponseHandler();
		    return httpclient.execute(httpost, responseHandler);
		    
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ERROR", e.toString());
		}
		return null;

	}

}

class Uklad1 {
	
	private static MultiplePointsLocalization loc;
	
	public Uklad1() {
		List<PointId> pointIds = new ArrayList<PointId>();
		pointIds.add(new PointId(new Point(0,5), BeaconNameDecoder.MINT_COCTAIL));
		pointIds.add(new PointId(new Point(0,0), BeaconNameDecoder.BLUEBERRY_PIE));
		pointIds.add(new PointId(new Point(5,5), BeaconNameDecoder.ICY_MARSHMALLOW));
		pointIds.add(new PointId(new Point(5,0), BeaconNameDecoder.BLUEBERRY_PIE_2));		
		loc = new MultiplePointsLocalization(pointIds);		
	}
		
	public static Point licz(Map<String, Double> m) throws LocalizationException {
		List<PointIdDistance> pointsIdDistance = new ArrayList<PointIdDistance>();
		for(String bName: m.keySet()) {
			pointsIdDistance.add(new PointIdDistance(m.get(bName), bName));
		}
		try {
			Point p = loc.getLocation(pointsIdDistance);
			Log.d("LOC", "x : " + p.x + "    y : " + p.y);		
			return p;
		} catch (LocalizationException e) {
			e.printStackTrace();
			throw e;
		}		
		
	}
}
