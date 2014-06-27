package com.estimote.examples.demos;

import java.util.Arrays;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.connection.BeaconConnection;

/**
 * Demo that shows how to connect to beacon and change its minor value.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class CharacteristicsDemoActivity extends Activity {

  private Beacon beacon;
  private BeaconConnection connection;

  private TextView statusView;
  private TextView beaconDetailsView;
  private EditText minorEditView;
  private EditText majorEditView;
  private Spinner powerSpinner;
  private View afterConnectedView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.characteristics_demo);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    statusView = (TextView) findViewById(R.id.status);
    beaconDetailsView = (TextView) findViewById(R.id.beacon_details);
    afterConnectedView = findViewById(R.id.after_connected);
    minorEditView = (EditText) findViewById(R.id.minor);
    powerSpinner = (Spinner) findViewById(R.id.power);
    majorEditView = (EditText) findViewById(R.id.major);
    
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.powerValues, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    powerSpinner.setAdapter(adapter);

    beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);
    connection = new BeaconConnection(this, beacon, createConnectionCallback());
    findViewById(R.id.update_major).setOnClickListener(createUpdateMajorButtonListener());
    findViewById(R.id.update).setOnClickListener(createUpdateButtonListener());    
    findViewById(R.id.update_power).setOnClickListener(createUpdatePowerButtonListener());
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!connection.isConnected()) {
      statusView.setText("Status: Connecting...");
      connection.authenticate();
    }
  }

  @Override
  protected void onDestroy() {
    connection.close();
    super.onDestroy();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Returns click listener on update minor button.
   * Triggers update minor value on the beacon.
   */
  private View.OnClickListener createUpdateButtonListener() {
    return new View.OnClickListener() {
      @Override public void onClick(View v) {
        int minor = parseMinorFromEditView();
        if (minor == -1) {
          showToast("Minor must be a number");
        } else {
          updateMinor(minor);
        }
      }
    };
  }
  
  /**
   * Returns click listener on update power button.
   * Triggers update power value on the beacon.
   */  
	private View.OnClickListener createUpdatePowerButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int power = parsePowerFromEditView();
				if (power == -1) {
					showToast("Power must be a number "+BeaconConnection.ALLOWED_POWER_LEVELS.toString());
				} else {
					updatePower(power);
				}
			}

		};
	}
	
  /**
   * Returns click listener on update major button.
   * Triggers update power value on the beacon.
   */  
	private View.OnClickListener createUpdateMajorButtonListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int major = parseMajorFromEditView();
				if (major == -1) {
					showToast("Major must be a number");
				} else {
					updateMajor(major);
				}
			}

		};
	}	

  /**
   * @return Parsed integer from edit text view or -1 if cannot be parsed or not a power value.
   */
	private int parsePowerFromEditView() {
		try {
			int power = Integer.parseInt(String.valueOf(powerSpinner.getSelectedItem().toString()));
			if (!BeaconConnection.ALLOWED_POWER_LEVELS.contains(power)) {
				return -1;
			}
			return power;
		} catch (NumberFormatException e) {
			return -1;
		}
	}  

  /**
   * @return Parsed integer from edit text view or -1 if cannot be parsed.
   */
  private int parseMinorFromEditView() {
    try {
      return Integer.parseInt(String.valueOf(minorEditView.getText()));
    } catch (NumberFormatException e) {
      return -1;
    }
  }
  
  /**
   * @return Parsed integer from edit text view or -1 if cannot be parsed.
   */
  private int parseMajorFromEditView() {
    try {
      return Integer.parseInt(String.valueOf(majorEditView.getText()));
    } catch (NumberFormatException e) {
      return -1;
    }
  }  

  private void updateMinor(int minor) {
    // Minor value will be normalized if it is not in the range.
    // Minor should be 16-bit unsigned integer.
    connection.writeMinor(minor, new BeaconConnection.WriteCallback() {
      @Override public void onSuccess() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            showToast("Minor value updated");
          }
        });
      }

      @Override public void onError() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            showToast("Minor not updated");
          }
        });
      }
    });
  }
  
  private void updatePower(int power) {
	    // Minor value will be normalized if it is not in the range.
	    // Minor should be 16-bit unsigned integer.
	    connection.writeBroadcastingPower(power, new BeaconConnection.WriteCallback() {
	      @Override public void onSuccess() {
	        runOnUiThread(new Runnable() {
	          @Override public void run() {
	            showToast("Power value updated");
	          }
	        });
	      }

	      @Override public void onError() {
	        runOnUiThread(new Runnable() {
	          @Override public void run() {
	            showToast("Power not updated");
	          }
	        });
	      }
	    });
	  }
  
  
  private void updateMajor(int major) {
	    // Minor value will be normalized if it is not in the range.
	    // Minor should be 16-bit unsigned integer.
	    connection.writeMajor(major, new BeaconConnection.WriteCallback() {
	      @Override public void onSuccess() {
	        runOnUiThread(new Runnable() {
	          @Override public void run() {
	            showToast("Major value updated");
	          }
	        });
	      }

	      @Override public void onError() {
	        runOnUiThread(new Runnable() {
	          @Override public void run() {
	            showToast("Major not updated");
	          }
	        });
	      }
	    });
	  }    

  private BeaconConnection.ConnectionCallback createConnectionCallback() {
    return new BeaconConnection.ConnectionCallback() {
      @Override public void onAuthenticated(final BeaconConnection.BeaconCharacteristics beaconChars) {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            statusView.setText("Status: Connected to beacon");
            StringBuilder sb = new StringBuilder()
                .append("Major: ").append(beacon.getMajor()).append("\n")
                .append("Minor: ").append(beacon.getMinor()).append("\n")
                .append("Advertising interval: ").append(beaconChars.getAdvertisingIntervalMillis()).append("ms\n")
                .append("Broadcasting power: ").append(beaconChars.getBroadcastingPower()).append(" dBm\n")
                .append("Battery: ").append(beaconChars.getBatteryPercent()).append(" %");
            beaconDetailsView.setText(sb.toString());
            minorEditView.setText(String.valueOf(beacon.getMinor()));
            majorEditView.setText(String.valueOf(beacon.getMajor()));
            ArrayAdapter<String> ad = (ArrayAdapter<String>) powerSpinner.getAdapter();
            powerSpinner.setSelection(ad.getPosition(String.valueOf(beaconChars.getBroadcastingPower())));
            // setText(String.valueOf(beaconChars.getBroadcastingPower()));
            afterConnectedView.setVisibility(View.VISIBLE);
          }
        });
      }

      @Override public void onAuthenticationError() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            statusView.setText("Status: Cannot connect to beacon. Authentication problems.");
          }
        });
      }

      @Override public void onDisconnected() {
        runOnUiThread(new Runnable() {
          @Override public void run() {
            statusView.setText("Status: Disconnected from beacon");
          }
        });
      }
    };
  }

  private void showToast(String text) {
    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
  }
}
