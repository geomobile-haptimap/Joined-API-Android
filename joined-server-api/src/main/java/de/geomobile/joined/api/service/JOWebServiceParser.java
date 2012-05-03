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

import de.geomobile.joined.api.config.JOConfig;
import de.geomobile.joined.api.data.JOFriend;
import de.geomobile.joined.api.data.JOMessage;
import de.geomobile.joined.api.data.JOPlace;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

/**
 * 
 */
public class JOWebServiceParser
{
	private static JOWebServiceParser instance = null;

	/**
	 * @return
	 */
	public static JOWebServiceParser getInstance()
	{
		if (instance == null)
		{
			instance = new JOWebServiceParser();
		}
		return instance;
	}

	/**
	 * @param jsonString
	 * @return
	 * @throws JOFriendFinderUnexpectedException
	 */
	public List<JOFriend> getFriendList(String jsonString) throws JOFriendFinderUnexpectedException
	{
		List<JOFriend> friendsList = new ArrayList<JOFriend>();
		try
		{
			JSONArray jsonArray = new JSONArray(jsonString);
			int lenght = jsonArray.length();
			for (int i = 0; i < lenght; i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				JOFriend friend = new JOFriend();
				if (jsonObject.has(JOConfig.NICKNAME))
					friend.setNickname(jsonObject.getString(JOConfig.NICKNAME));
				if (jsonObject.has(JOConfig.IMAGE_HASH))
					friend.setImageHash(jsonObject.getString(JOConfig.IMAGE_HASH));
				if (jsonObject.has(JOConfig.ID))
					friend.setId(jsonObject.getString(JOConfig.ID));
				if (jsonObject.has(JOConfig.ACTIVE))
					friend.setActive(jsonObject.getBoolean(JOConfig.ACTIVE));
				if (jsonObject.has(JOConfig.STATUS))
					friend.setFriendshipStatus(jsonObject.getInt(JOConfig.STATUS));
				if (jsonObject.has(JOConfig.LAST_POS_UPDATE))
					friend.setLastPosUpdate(jsonObject.getLong(JOConfig.LAST_POS_UPDATE));
				if (jsonObject.has(JOConfig.LATITUDE))
					friend.setLatitude(jsonObject.getDouble(JOConfig.LATITUDE));
				if (jsonObject.has(JOConfig.LONGITUDE))
					friend.setLongitude(jsonObject.getDouble(JOConfig.LONGITUDE));
				friendsList.add(friend);
			}
		}
		catch (JSONException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		return friendsList;
	}

	/**
	 * @param jsonString
	 * @return
	 * @throws JOFriendFinderUnexpectedException
	 */
	public List<JOMessage> getMessages(String jsonString) throws JOFriendFinderUnexpectedException
	{
		try
		{
			List<JOMessage> list = new ArrayList<JOMessage>();
			JSONArray jsonArray = new JSONArray(jsonString);
			int length = jsonArray.length();
			for (int i = 0; i < length; i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String attachment = jsonObject.getString(JOConfig.ATTACHMENT);
				// int placeId = -1;
				JOPlace place = new JOPlace();

				if (attachment.length() > 0)
				{
				}

				list.add(new JOMessage(jsonObject.getString(JOConfig.SENDER_ID), jsonObject.getString(JOConfig.ID), JOConfig.MESSAGE_FROM, jsonObject.getString(JOConfig.TEXT), jsonObject.getLong(JOConfig.CREATION_DATE), place));
			}
			return list;
		}
		catch (JSONException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
	}

}
