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
 * This class contains various methods for receiving information about a friend.
 */
public class JOFriend
{
	private String nickname = "";
	private long lastPosUpdate = 0L;
	private int friendshipStatus = 0;
	private String image = "";
	private String imageHash = "";
	private Double latitude = 0D;
	private Double longitude = 0D;
	private String id = "";
	private boolean isActive = false;
	private String facebookID = "";
	
	/**
	 * @return
	 */
	public String getNickname()
	{
		return nickname;
	}

	/**
	 * @param nickname
	 */
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	/**
	 * @return
	 */
	public long getLastPosUpdate()
	{
		return lastPosUpdate;
	}

	/**
	 * @param lastPosUpdate
	 */
	public void setLastPosUpdate(long lastPosUpdate)
	{
		this.lastPosUpdate = lastPosUpdate;
	}

	/**
	 * @return
	 */
	public int getFriendshipStatus()
	{
		return friendshipStatus;
	}

	/**
	 * @param friendshipStatus
	 */
	public void setFriendshipStatus(int friendshipStatus)
	{
		this.friendshipStatus = friendshipStatus;
	}

	/**
	 * @return
	 */
	public String getImage()
	{
		return image;
	}

	/**
	 * @param image
	 */
	public void setImage(String image)
	{
		this.image = image;
	}

	/**
	 * @return
	 */
	public String getImageHash()
	{
		return imageHash;
	}

	/**
	 * @param imageHash
	 */
	public void setImageHash(String imageHash)
	{
		this.imageHash = imageHash;
	}

	/**
	 * @return
	 */
	public Double getLatitude()
	{
		return latitude;
	}

	/**
	 * @param latitude
	 */
	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	/**
	 * @return
	 */
	public Double getLongitude()
	{
		return longitude;
	}

	/**
	 * @param longitude
	 */
	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return
	 */
	public boolean isActive()
	{
		return isActive;
	}

	/**
	 * @param isActive
	 */
	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	/**
	 * @return
	 */
	public String getFacebookID()
	{
		return facebookID;
	}

	/**
	 * @param facebookID
	 */
	public void setFacebookID(String facebookID)
	{
		this.facebookID = facebookID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "FRIEND = { nickname: '" + getNickname() + "', lastPosUpdate: '" + getLastPosUpdate() + "', friendshipStatus: '" + getFriendshipStatus() + "', image: '" + getImage() + "', imageHash: '" + getImageHash() + "', latitude: '"
				+ getLatitude() + "', longitude: '" + getLongitude() + "', id: '" + getId() + "', isActive: '" + isActive() + "', facebookID: '" + getFacebookID() + "' }";
	}
}
