package de.geomobile.joined.api.bearing;

import java.io.Serializable;

/**
 * This class contains the Geoinformation for the HaptiMapToolKit.<br>
 * You have to save the Longitude, Latitude and the name of the PointOfInterest.
 * @author Evgenij Renke  GeoMobile GmbH
 *
 */
public class BPOI implements Serializable {
	
	private static final long serialVersionUID = 4759185882642148767L;
	private double lat;
	private double lon;
	private String description;
	
	/**
	 * 
	 * @param lat	value of Latitude (12.23233)	
	 * @param lon	value of Longitude (24.87883)
	 * @param description	name of the PointOfInterest
	 */
	public BPOI(double lat, double lon, String description) {
		this.lat = lat;
		this.lon = lon;
		this.description = description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	/**
	 * Set longitude.
	 * @param lon the longitude as a double.
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	/**
	 *  Get Longitude.
	 * @return
	 */
	public double getLon() {
		return lon;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLat() {
		return lat;
	}
}
