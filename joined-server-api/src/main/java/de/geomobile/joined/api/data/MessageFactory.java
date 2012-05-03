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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * @author bastian
 *
 */
public class MessageFactory
{
	static private MessageFactory _instance = null;

	/**
	 * 
	 */
	private MessageFactory()
	{

	}

	/**
	 * @return
	 */
	static public MessageFactory instance()
	{
		if (null == _instance)
		{
			_instance = new MessageFactory();
		}
		return _instance;
	}

	/**
	 * @param json
	 * @return
	 * @throws FriendFinderUnexpectedException
	 */
	public List<Friend> createFriends(String json) throws FriendFinderUnexpectedException
	{
		List<Friend> friendsList = new ArrayList<Friend>();

		try
		{
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				Friend friend = new Friend();

				if (jsonObject.has(Config.NICKNAME))
				{
					friend.setNickname(jsonObject.getString(Config.NICKNAME));
				}

				if (jsonObject.has(Config.IMAGE_HASH))
				{
					friend.setImageHash(jsonObject.getString(Config.IMAGE_HASH));
				}

				if (jsonObject.has(Config.ID))
				{
					friend.setId(jsonObject.getString(Config.ID));
				}

				if (jsonObject.has(Config.ACTIVE))
				{
					friend.setActive(jsonObject.getBoolean(Config.ACTIVE));
				}

				if (jsonObject.has(Config.STATUS))
				{
					friend.setFriendshipStatus(jsonObject.getInt(Config.STATUS));
				}

				if (jsonObject.has(Config.LAST_POS_UPDATE))
				{
					friend.setLastPosUpdate(jsonObject.getLong(Config.LAST_POS_UPDATE));
				}

				if (jsonObject.has(Config.LATITUDE))
				{
					friend.setLatitude(jsonObject.getDouble(Config.LATITUDE));
				}

				if (jsonObject.has(Config.LONGITUDE))
				{
					friend.setLongitude(jsonObject.getDouble(Config.LONGITUDE));
				}

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
	 * @param friends
	 * @return
	 */
	public List<Friend> getAvailableFriends(List<Friend> friends)
	{
		List<Friend> availableFriends = new ArrayList<Friend>();
		for (Friend friend : friends)
		{
			if (friend.getFriendshipStatus() == Config.FRIEND_STATUS_YES && friend.isActive())
			{
				availableFriends.add(friend);
			}
		}
		return availableFriends;
	}

	/**
	 * @param friends
	 * @return
	 */
	public List<Friend> getDeactivatedFriends(List<Friend> friends)
	{
		List<Friend> availableFriends = new ArrayList<Friend>();
		for (Friend friend : friends)
		{
			if (friend.getFriendshipStatus() == Config.FRIEND_STATUS_YES && friend.isActive() == false)
			{
				availableFriends.add(friend);
			}
		}
		return availableFriends;
	}

	/**
	 * @param friends
	 * @return
	 */
	public List<Friend> getFriendsToAccept(List<Friend> friends)
	{
		List<Friend> availableFriends = new ArrayList<Friend>();
		for (Friend friend : friends)
		{
			if (friend.getFriendshipStatus() == Config.FRIEND_STATUS_NO)
			{
				availableFriends.add(friend);
			}
		}
		return availableFriends;
	}

	/**
	 * @param friends
	 * @return
	 */
	public List<Friend> getPendingFriends(List<Friend> friends)
	{
		List<Friend> availableFriends = new ArrayList<Friend>();
		for (Friend friend : friends)
		{
			if (friend.getFriendshipStatus() == Config.FRIEND_STATUS_PENDING)
			{
				availableFriends.add(friend);
			}
		}
		return availableFriends;
	}
}
