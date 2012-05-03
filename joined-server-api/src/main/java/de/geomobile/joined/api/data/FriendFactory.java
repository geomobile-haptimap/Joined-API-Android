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

import de.geomobile.joined.api.config.Config;

public class FriendFactory
{
	static private FriendFactory _instance = null;

	/**
	 * 
	 */
	private FriendFactory()
	{

	}

	/**
	 * @return
	 */
	static public FriendFactory instance()
	{
		if (null == _instance)
		{
			_instance = new FriendFactory();
		}
		return _instance;
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
