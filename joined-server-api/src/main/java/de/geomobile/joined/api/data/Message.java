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

import de.geomobile.joined.api.config.Config;

/**
 * This class contains various methods for receiving information about a text message.
 */
public class Message
{
	private String recipient;
	private String id;
	private int direction;
	private String content;
	private String mode;
	private long time;
	private Place place;
	
	/**
	 * 
	 * @param recipient The sender of the message.
	 * @param id The internal identifier for the message.
	 * @param direction 
	 * @param content
	 * @param time
	 * @param place
	 */
	public Message(String recipient, String id, int direction, String content, long time, Place place)
	{
		this.recipient = recipient;
		this.id = id;
		this.direction = direction;
		this.content = content;
		this.mode = Config.TEXT_MSG;
		this.time = time;
		this.place = place;
	}
	
	/**
	 * @return
	 */
	public String getSender()
	{
		return recipient;
	}

	/**
	 * @param recipient
	 */
	public void setRecipient(String recipient)
	{
		this.recipient = recipient;
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
	public int getDirection()
	{
		return direction;
	}

	/**
	 * @param direction
	 */
	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	/**
	 * @return
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

	/**
	 * @return
	 */
	public String getMode()
	{
		return mode;
	}

	/**
	 * @param mode
	 */
	public void setMode(String mode)
	{
		this.mode = mode;
	}

	/**
	 * @return
	 */
	public long getTime()
	{
		return time;
	}

	/**
	 * @param time
	 */
	public void setTime(long time)
	{
		this.time = time;
	}

	/**
	 * @return
	 */
	public Place getPlace()
	{
		return place;
	}

	/**
	 * @param place
	 */
	public void setPlace(Place place)
	{
		this.place = place;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Message from '" + getSender() + "' : '" + getContent() + "'";
	}
}
