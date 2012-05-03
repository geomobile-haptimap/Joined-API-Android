package de.geomobile.joined.api.bearing;

import java.util.List;

import de.geomobile.joined.api.bearing.config.Config;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

public class RotationListener implements LocationListener {

	private Location destination;
	private Location start;
	private final RotatorImage view;
	private boolean sensorIsEnabled = false;
	private SensorManager mySensorManager;
	private Context context;
	private SensorEventListener sensorEventListener;
	private List<Sensor> mySensors;
	private boolean sensorIsSleeping = false;
	private final TextView descTextView;
	private boolean reached = false;

	public RotationListener(Location destination, RotatorImage view,
			Context context, TextView descTextView) {
		this.destination = destination;
		this.view = view;
		this.context = context;
		this.descTextView = descTextView;
	}

	public void onLocationChanged(Location location) {
		start = location;
		if (!sensorIsEnabled) {
			sensorIsEnabled = true;
			startSensor();
		}
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	private void startSensor() {
		sensorEventListener = new SensorEventListener() {
			float filteredAngle = 0;

			public void onSensorChanged(SensorEvent event) {
				if (start != null && destination != null) {
					// view.setVisibility(View.VISIBLE);
					int angle = GeoHelper.calculateAngle(start, destination);

					float influenceOfNewAngle = .1f;
					float heading = event.values[0];
					float diff1 = heading - filteredAngle;
					if (Math.abs(diff1) <= 180) {
						filteredAngle = filteredAngle + influenceOfNewAngle
								* diff1;
					} else {
						filteredAngle = filteredAngle - Math.signum(diff1)
								* influenceOfNewAngle
								* (360 - Math.signum(diff1) * diff1);
					}

					if (filteredAngle < 0)
						filteredAngle = 360 + filteredAngle;
					else if (filteredAngle >= 360)
						filteredAngle = filteredAngle - 360;

					heading = filteredAngle;

					int rotation;

					if ((angle - heading) <= 180 && (angle - heading) >= -180) {
						rotation = (int) (angle - heading);
					} else if ((angle - heading) >= 180) {
						rotation = (int) -(360 - angle + heading);
					} else {
						rotation = (int) (360 - heading + angle);
					}

					// Entfernung der Punkte berechnen
					double dlon = 71.5 * (start.getLongitude() - destination
							.getLongitude());
					double dlat = 111.3 * (start.getLatitude() - destination
							.getLatitude());
					double dcalculate = java.lang.Math.sqrt(dlon * dlon + dlat
							* dlat);
					dcalculate = dcalculate * 1000;
					int distance = (int) dcalculate;
					String dString;
					if (distance <= 15) {
						dString = (int) distance + " m";
						if (!reached) {
							reached = true;
							Intent intent = new Intent();
							intent.setAction(Config.DEASTINATION_REACHED);
							context.sendBroadcast(intent);
						}
					} else if (distance < 1000) {
						dString = (int) distance + " m";
					} else {
						double ddistance = (int) distance;
						dString = (int) (ddistance / 1000) + " km";
					}

					if (descTextView != null) {
						descTextView
								.setText(String.format(
										context.getString(R.string.hmtk_rotationListener_distance),
										dString)
										+ String.format(
												context.getString(R.string.hmtk_rotationListener_accuracy),
												(int) start.getAccuracy()));
					}

					view.rotate((rotation));
				}
			}

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};

		mySensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		// "rate suitable for games" Alternative: SENSOR_DELAY_FASTEST
		mySensorManager.registerListener(sensorEventListener, mySensors.get(0),
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void disableOrientation() {
		if (sensorIsEnabled && !sensorIsSleeping) {
			mySensorManager.unregisterListener(sensorEventListener);
			sensorIsSleeping = true;
		}
	}

	public void enableOrientation() {
		if (sensorIsEnabled && sensorIsSleeping) {
			mySensorManager.registerListener(sensorEventListener,
					mySensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
			sensorIsSleeping = false;
		}
	}

	public void changeDestination(Location location) {
		this.destination = location;
	}

	public Location getDestination() {
		return destination;
	}
}
