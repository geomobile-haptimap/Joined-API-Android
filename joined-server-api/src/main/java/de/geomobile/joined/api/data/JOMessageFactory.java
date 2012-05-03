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

import de.geomobile.joined.api.config.JOConfig;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

/**
 * @author bastian
 *
 */
public class JOMessageFactory
{
	static private JOMessageFactory _instance = null;

	/**
	 * 
	 */
	private JOMessageFactory()
	{

	}

	/**
	 * @return
	 */
	static public JOMessageFactory instance()
	{
		if (null == _instance)
		{
			_instance = new JOMessageFactory();
		}
		return _instance;
	}

	/**
	 * @param json
	 * @return
	 * @throws JOFriendFinderUnexpectedException
	 */
	public List<JOFriend> createFriends(String json) throws JOFriendFinderUnexpectedException
	{
		List<JOFriend> friendsList = new ArrayList<JOFriend>();

		try
		{
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				JOFriend friend = new JOFriend();

				if (jsonObject.has(JOConfig.NICKNAME))
				{
					friend.setNickname(jsonObject.getString(JOConfig.NICKNAME));
				}

				if (jsonObject.has(JOConfig.IMAGE_HASH))
				{
					friend.setImageHash(jsonObject.getString(JOConfig.IMAGE_HASH));
				}

				if (jsonObject.has(JOConfig.ID))
				{
					friend.setId(jsonObject.getString(JOConfig.ID));
				}

				if (jsonObject.has(JOConfig.ACTIVE))
				{
					friend.setActive(jsonObject.getBoolean(JOConfig.ACTIVE));
				}

				if (jsonObject.has(JOConfig.STATUS))
				{
					friend.setFriendshipStatus(jsonObject.getInt(JOConfig.STATUS));
				}

				if (jsonObject.has(JOConfig.LAST_POS_UPDATE))
				{
					friend.setLastPosUpdate(jsonObject.getLong(JOConfig.LAST_POS_UPDATE));
				}

				if (jsonObject.has(JOConfig.LATITUDE))
				{
					friend.setLatitude(jsonObject.getDouble(JOConfig.LATITUDE));
				}

				if (jsonObject.has(JOConfig.LONGITUDE))
				{
					friend.setLongitude(jsonObject.getDouble(JOConfig.LONGITUDE));
				}

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
	 * @param friends
	 * @return
	 */
	public List<JOFriend> getAvailableFriends(List<JOFriend> friends)
	{
		List<JOFriend> availableFriends = new ArrayList<JOFriend>();
		for (JOFriend friend : friends)
		{
			if (friend.getFriendshipStatus() == JOConfig.FRIEND_STATUS_YES && friend.isActive())
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
	public List<JOFriend> getDeactivatedFriends(List<JOFriend> friends)
	{
		List<JOFriend> availableFriends = new ArrayList<JOFriend>();
		for (JOFriend friend : friends)
		{
			if (friend.getFriendshipStatus() == JOConfig.FRIEND_STATUS_YES && friend.isActive() == false)
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
	public List<JOFriend> getFriendsToAccept(List<JOFriend> friends)
	{
		List<JOFriend> availableFriends = new ArrayList<JOFriend>();
		for (JOFriend friend : friends)
		{
			if (friend.getFriendshipStatus() == JOConfig.FRIEND_STATUS_NO)
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
	public List<JOFriend> getPendingFriends(List<JOFriend> friends)
	{
		List<JOFriend> availableFriends = new ArrayList<JOFriend>();
		for (JOFriend friend : friends)
		{
			if (friend.getFriendshipStatus() == JOConfig.FRIEND_STATUS_PENDING)
			{
				availableFriends.add(friend);
			}
		}
		return availableFriends;
	}
}
