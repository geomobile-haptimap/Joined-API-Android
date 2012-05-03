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
package de.geomobile.joined.api.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.data.Friend;
import de.geomobile.joined.api.data.Message;
import de.geomobile.joined.api.data.Place;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * 
 */
public class JSONParser
{
	private static JSONParser instance = null;

	/**
	 * @return
	 */
	public static JSONParser getInstance()
	{
		if (instance == null)
		{
			instance = new JSONParser();
		}
		return instance;
	}

	/**
	 * @param jsonString
	 * @return
	 * @throws FriendFinderUnexpectedException
	 */
	public List<Friend> getFriendList(String jsonString) throws FriendFinderUnexpectedException
	{
		List<Friend> friendsList = new ArrayList<Friend>();
		try
		{
			JSONArray jsonArray = new JSONArray(jsonString);
			int lenght = jsonArray.length();
			for (int i = 0; i < lenght; i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Friend friend = new Friend();
				if (jsonObject.has(Config.NICKNAME))
					friend.setNickname(jsonObject.getString(Config.NICKNAME));
				if (jsonObject.has(Config.IMAGE_HASH))
					friend.setImageHash(jsonObject.getString(Config.IMAGE_HASH));
				if (jsonObject.has(Config.ID))
					friend.setId(jsonObject.getString(Config.ID));
				if (jsonObject.has(Config.ACTIVE))
					friend.setActive(jsonObject.getBoolean(Config.ACTIVE));
				if (jsonObject.has(Config.STATUS))
					friend.setFriendshipStatus(jsonObject.getInt(Config.STATUS));
				if (jsonObject.has(Config.LAST_POS_UPDATE))
					friend.setLastPosUpdate(jsonObject.getLong(Config.LAST_POS_UPDATE));
				if (jsonObject.has(Config.LATITUDE))
					friend.setLatitude(jsonObject.getDouble(Config.LATITUDE));
				if (jsonObject.has(Config.LONGITUDE))
					friend.setLongitude(jsonObject.getDouble(Config.LONGITUDE));
				friendsList.add(friend);
			}
		}
		catch (JSONException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		return friendsList;
	}

	/**
	 * @param jsonString
	 * @return
	 * @throws FriendFinderUnexpectedException
	 */
	public List<Message> getMessages(String jsonString) throws FriendFinderUnexpectedException
	{
		try
		{
			List<Message> list = new ArrayList<Message>();
			JSONArray jsonArray = new JSONArray(jsonString);
			int length = jsonArray.length();
			for (int i = 0; i < length; i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String attachment = jsonObject.getString(Config.ATTACHMENT);
				// int placeId = -1;
				Place place = new Place();

				if (attachment.length() > 0)
				{
				}

				list.add(new Message(jsonObject.getString(Config.SENDER_ID), jsonObject.getString(Config.ID), Config.MESSAGE_FROM, jsonObject.getString(Config.TEXT), jsonObject.getLong(Config.CREATION_DATE), place));
			}
			return list;
		}
		catch (JSONException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
	}

}
