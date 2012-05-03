package de.geomobile.joined.api.bearing;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.geomobile.joined.api.bearing.config.Config;

public class JoinedBearingActivity extends Activity {

	private GeigerSoundImageView geigerPointer;
	private RotationListener angleListener;
	private LocationManager locManager;
	private TextView descTextView;
	private BroadcastReceiver broadCastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent prevIntent = getIntent();
		Bundle bundle = prevIntent.getExtras();
		BPOI destinationPOI = (BPOI) bundle.getSerializable(Config.DESTINATION);
		Location destination = new Location(Config.DEST);
		destination.setLatitude(destinationPOI.getLat());
		destination.setLongitude(destinationPOI.getLon());

		setContentView(R.layout.bearing);

		geigerPointer = (GeigerSoundImageView) findViewById(R.id.GeigerPointer);
		descTextView = (TextView) findViewById(R.id.descText);

		geigerPointer.isGeiger(true);
		angleListener = new RotationListener(destination, geigerPointer, this,
				descTextView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		angleListener.enableOrientation();
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
				10, angleListener);
		geigerPointer.create();

		Location lastLocation = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastLocation != null
				&& System.currentTimeMillis() - lastLocation.getTime() > 30000) {
			angleListener.onLocationChanged(lastLocation);
		}
		registerReceiver();
	}

	@Override
	protected void onPause() {
		super.onPause();
		geigerPointer.stopThread();
		angleListener.disableOrientation();
		locManager.removeUpdates(angleListener);
	}

	@Override
	protected void onStop() {
		super.onPause();
		if (geigerPointer != null && angleListener != null) {
			geigerPointer.stopThread();
			angleListener.disableOrientation();
			locManager.removeUpdates(angleListener);
		}
		unregisterBroadcastReceiver();
	}

	public void onVibrationClick(View view) {
		geigerPointer.setVibratorActive();
	}

	public void onSoundClick(View view) {
		geigerPointer.setSoundActive();
	}

	private void registerReceiver() {

		if (broadCastReceiver == null) {
			broadCastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if (Config.DEASTINATION_CHANGED_ACTION.equals(intent
							.getAction())) {
						double lat = intent.getDoubleExtra(Config.LATITUDE, 0);
						double lng = intent.getDoubleExtra(Config.LONGITUDE, 0);
						Log.i("GeigerSoundActivity",
								"Destination changed : lat " + lat + " lng "
										+ lng);
						Location location = new Location("");
						location.setLatitude(lat);
						location.setLongitude(lng);
						angleListener.changeDestination(location);
					}
				}
			};
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Config.DEASTINATION_CHANGED_ACTION);
			this.registerReceiver(broadCastReceiver, intentFilter);
		}
	}

	private void unregisterBroadcastReceiver() {
		if (broadCastReceiver != null) {
			this.unregisterReceiver(broadCastReceiver);
			broadCastReceiver = null;
		}
	}
}