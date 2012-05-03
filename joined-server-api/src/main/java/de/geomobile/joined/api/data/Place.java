/*
 * #%L
 * Joined API for Android (Server)
 * %%
 * Copyright (C) 2012 GeoMobile GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.geomobile.joined.api.data;

/**
 * @author bastian
 *
 */
public class Place
{
	private int id;
	private String title;
	private double latitude;
	private double longitude;

	/**
	 * @param id
	 * @param title
	 * @param latitude
	 * @param longitude
	 */
	public Place(int id, String title, double latitude, double longitude)
	{
		this.id = id;
		this.title = title;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * 
	 */
	public Place()
	{

	}

	/**
	 * @return
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return
	 */
	public double getLatitude()
	{
		return latitude;
	}

	/**
	 * @param latitude
	 */
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * @return
	 */
	public double getLongitude()
	{
		return longitude;
	}

	/**
	 * @param longitude
	 */
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
}
