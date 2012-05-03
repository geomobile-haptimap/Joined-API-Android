package de.geomobile.joined.api.bearing.example;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.geomobile.joined.api.bearing.BPOI;
import de.geomobile.joined.api.bearing.config.Config;

public class JoinedMapActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		MapView mapView = (MapView) findViewById(R.id.map);

		Intent prevIntent = getIntent();
		Bundle bundle = prevIntent.getExtras();
		BPOI poi = (BPOI) bundle.getSerializable(Config.DESTINATION);

		GeoPoint point = new GeoPoint((int) (poi.getLat() * 10E5),
				(int) (poi.getLon() * 10E5));

		Drawable drawable = this.getResources().getDrawable(
				R.drawable.ic_launcher);
		HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(
				drawable, this);

		OverlayItem overlayitem = new OverlayItem(point, "", "");
		itemizedoverlay.addOverlay(overlayitem);
		mapView.getOverlays().add(itemizedoverlay);

		mapView.getController().animateTo(point);
		mapView.getController().setZoom(18);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
