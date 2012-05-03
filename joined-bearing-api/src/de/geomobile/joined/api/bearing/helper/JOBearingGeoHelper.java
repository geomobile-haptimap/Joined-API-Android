package de.geomobile.joined.api.bearing.helper;

import android.location.Location;

/**
 * @author bbaranski
 *
 */
public class JOBearingGeoHelper
{
	/**
	 * 
	 */
	public JOBearingGeoHelper()
	{
	}

	/**
	 * @param start
	 * @param destination
	 * @return
	 */
	public static int calculateAngle(Location start, Location destination)
	{
		boolean lon = false;
		boolean lat = false;

		if ((start.getLongitude() - destination.getLongitude()) <= 0)
			lon = true;
		if ((start.getLatitude() - destination.getLatitude()) <= 0)
			lat = true;

		double AK = calculateGoogleNS(start, destination);
		double GK = calculateGoogleWO(start, destination);
		double NSOW = GK / AK;
		double angle = Math.atan(NSOW);
		double angleDegree = Math.toDegrees(angle);

		if (lon && lat)
			return (int) angleDegree;
		else if (lon && !lat)
			return (int) (180 - angleDegree);
		else if (!lon && lat)
			return (int) (360 - angleDegree);
		else
			return (int) (180 + angleDegree);

	}

	/**
	 * @param start
	 * @param destination
	 * @return
	 */
	public static double calculateGoogleDistance(Location start, Location destination)
	{
		double distance = start.distanceTo(destination);
		return distance;
	}

	/**
	 * @param start
	 * @param destination
	 * @return
	 */
	public static double calculateGoogleNS(Location start, Location destination)
	{
		Location loc = new Location("loc");
		loc.setLatitude(destination.getLatitude());
		loc.setLongitude(start.getLongitude());
		return start.distanceTo(loc);
	}

	/**
	 * @param start
	 * @param destination
	 * @return
	 */
	public static double calculateGoogleWO(Location start, Location destination)
	{
		Location loc = new Location("loc");
		loc.setLatitude(start.getLatitude());
		loc.setLongitude(destination.getLongitude());
		return start.distanceTo(loc);
	}
}
