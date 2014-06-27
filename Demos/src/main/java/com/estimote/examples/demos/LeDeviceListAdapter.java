package com.estimote.examples.demos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.examples.demos.utils.BeaconNameDecoder;
import com.estimote.examples.demos.utils.MacAddressBeaconIdentifier;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


/**
 * Displays basic information about beacon.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class LeDeviceListAdapter extends BaseAdapter {

  private ArrayList<Beacon> beacons;
  private LayoutInflater inflater;
  private MacAddressBeaconIdentifier macNameDecoder = new MacAddressBeaconIdentifier();

  public LeDeviceListAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
    this.beacons = new ArrayList<Beacon>();
  }

  public void replaceWith(Collection<Beacon> newBeacons) {
    this.beacons.clear();
    this.beacons.addAll(newBeacons);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return beacons.size();
  }

  @Override
  public Beacon getItem(int position) {
    return beacons.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    view = inflateIfRequired(view, position, parent);
    bind(getItem(position), view);
    return view;
  }

  private void bind(Beacon beacon, View view) {
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.macTextView.setText(String.format("MAC: %s (%.2fm)", beacon.getMacAddress(), Utils.computeAccuracy(beacon)));
    holder.majorTextView.setText("Major: " + beacon.getMajor());
    holder.minorTextView.setText("Minor: " + beacon.getMinor());
    holder.measuredPowerTextView.setText("MPower: " + beacon.getMeasuredPower());
    holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
    String name = macNameDecoder.getNameByIdentifier(beacon.getMacAddress());
    holder.name.setText("Name: "+ (!name.equals(BeaconNameDecoder.UNKNOWN) ? name : beacon.getName()));
    
    int bg = R.drawable.beacon_gray;
    bg = name.equals(BeaconNameDecoder.BLUEBERRY_PIE) ? R.drawable.beacon_blueberry :
    	name.equals(BeaconNameDecoder.BLUEBERRY_PIE_2) ? R.drawable.beacon_blueberry2 :
    		name.equals(BeaconNameDecoder.ICY_MARSHMALLOW) ? R.drawable.beacon_icy :
    			name.equals(BeaconNameDecoder.MINT_COCTAIL) ? R.drawable.beacon_mint : 
    				R.drawable.beacon_gray;    	
    holder.image.setImageResource(bg);
    
  }

  private View inflateIfRequired(View view, int position, ViewGroup parent) {
    if (view == null) {
      view = inflater.inflate(R.layout.device_item, null);
      view.setTag(new ViewHolder(view));
    }
    return view;
  }

  static class ViewHolder {
    final TextView macTextView;
    final TextView majorTextView;
    final TextView minorTextView;
    final TextView measuredPowerTextView;
    final TextView rssiTextView;
    final TextView name;
    final ImageView image;

    ViewHolder(View view) {
      macTextView = (TextView) view.findViewWithTag("mac");
      majorTextView = (TextView) view.findViewWithTag("major");
      minorTextView = (TextView) view.findViewWithTag("minor");
      measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
      rssiTextView = (TextView) view.findViewWithTag("rssi");
      name = (TextView) view.findViewWithTag("name");
      image = (ImageView) view.findViewWithTag("image");
    }
  }
}
