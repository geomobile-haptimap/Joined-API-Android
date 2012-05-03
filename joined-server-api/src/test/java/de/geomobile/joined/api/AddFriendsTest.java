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
package de.geomobile.joined.api;

import java.util.List;

import junit.framework.TestCase;
import de.geomobile.joined.api.client.JoinedClient;
import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.data.Friend;
import de.geomobile.joined.api.data.FriendFactory;
import de.geomobile.joined.api.data.User;
import de.geomobile.joined.api.exception.FriendFinderHTTPException;
import de.geomobile.joined.api.exception.FriendFinderLoginException;
import de.geomobile.joined.api.exception.FriendFinderServerException;
import de.geomobile.joined.api.exception.FriendFinderSourceException;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * This class contains a test case that simulates the process of adding other users to a friend list.
 */
public class AddFriendsTest extends TestCase
{
	/**
	 * This method performs the actual test case.
	 */
	public void test()
	{
		try
		{
			/* CREATE CLIENT */
			JoinedClient client = JoinedClient.createJoinedClient(Config.JOINED_SERVER, Config.OPEN_API_KEY);

			/* LOGIN USER */
			User user1 = client.login(Config.TEST_USER_1, Config.TEST_USER_1);
			User user2 = client.login(Config.TEST_USER_2, Config.TEST_USER_2);

			/* USER 1 SEARCHES FOR USER 2 */
			List<Friend> friends = client.searchFriends(user1, Config.TEST_USER_2);

			assertTrue(friends.size() == 1);

			/* USER 1 INVITES USER 2 */
			client.addFriend(user1, friends.get(0));

			/* USER 2 GET FRIENDS TO ACCEPT LIST */
			friends = client.getFriends(user2);
			List<Friend> friendsToAccept = FriendFactory.instance().getFriendsToAccept(friends);

			assertTrue(friendsToAccept.size() == 1);

			/* USER 2 ACCEPTS USER 1 */
			client.acceptFriend(user2, friendsToAccept.get(0));

			/* USER 1 GET FRIEND LIST */
			friends = client.getFriends(user1);

			/* USER 2 GET FRIEND LIST */
			friends = client.getFriends(user2);

			/* USER 2 DELETE USER 1 AS FRIEND */
			client.deleteFriend(user2, friends.get(0));

			/* USER 2 GET FRIEND LIST */
			friends = client.getFriends(user2);

			assertTrue(friends.size() == 0);

			/* USER 2 SEARCHES FOR USER 1 */
			friends = client.searchFriends(user2, Config.TEST_USER_1);

			assertTrue(friends.size() == 1);

			/* USER 2 INVITES USER 1 */
			client.addFriend(user2, friends.get(0));

			/* USER 2 GET FRIEND LIST */
			friends = client.getFriends(user2);

			assertTrue(friends.size() == 1);

			/* USER 2 GET FRIENDS PENDING LIST */
			friends = client.getFriends(user2);
			List<Friend> friendsPending = FriendFactory.instance().getPendingFriends(friends);

			assertTrue(friendsPending.size() == 1);

			/* USER 1 GET FRIEND LIST */
			friends = client.getFriends(user1);

			assertTrue(friends.size() == 1);

			/* USER 1 GET FRIENDS TO ACCEPT LIST */
			friends = client.getFriends(user1);
			friendsToAccept = FriendFactory.instance().getFriendsToAccept(friends);

			assertTrue(friendsToAccept.size() == 1);
			
			/* USER 1 ACCEPTS USER 2 */
			client.acceptFriend(user1, friendsToAccept.get(0));

			/* LOGOUT USER */
			client.logout(user1);
			client.logout(user2);

			assertTrue(true);
		}
		catch (FriendFinderHTTPException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (FriendFinderUnexpectedException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (FriendFinderServerException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (FriendFinderLoginException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (FriendFinderSourceException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
