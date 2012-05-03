package de.geomobile.joined.api.bearing;

import android.location.Location;

public class GeoHelper {
	
	
	public GeoHelper(){
	}
	
	// berechnet den winkel, handyausrichtung und ziel
	public static int calculateAngle(Location start, Location destination){
		boolean lon = false;
		boolean lat = false;
		
		if((start.getLongitude() - destination.getLongitude()) <= 0) lon = true;
		if((start.getLatitude() - destination.getLatitude()) <= 0) lat = true;
		
//		Log.v(GEOHELPER, "Lat : " + lat +"  Lon : " + lon);		
		double AK = calculateGoogleNS(start, destination);
		double GK = calculateGoogleWO(start, destination);
		double NSOW = GK / AK;
		double angle = Math.atan(NSOW);
		double angleDegree = Math.toDegrees(angle);
//		Log.v(GEOHELPER, "NS   :  "+ AK );
//		Log.v(GEOHELPER, "WO   :  "+ GK );
		
		if(lon && lat) return (int) angleDegree;
		else if(lon && !lat) return (int) (180 - angleDegree);
		else if(!lon && lat) return (int) (360 - angleDegree);
		else return (int) (180 + angleDegree);
		
	}
	
	public static double calculateGoogleDistance(Location start, Location destination){
		double distance = start.distanceTo(destination);
		return distance;
	}
	
	public static double calculateGoogleNS(Location start, Location destination){
		Location loc = new Location("loc");
		loc.setLatitude(destination.getLatitude());
		loc.setLongitude(start.getLongitude());
		return start.distanceTo(loc);
	}
	
	public static double calculateGoogleWO(Location start, Location destination){
		Location loc = new Location("loc");
		loc.setLatitude(start.getLatitude());
		loc.setLongitude(destination.getLongitude());
		return start.distanceTo(loc);
	}
}
