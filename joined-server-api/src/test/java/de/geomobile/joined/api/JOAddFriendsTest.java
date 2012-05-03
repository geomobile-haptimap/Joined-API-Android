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
import de.geomobile.joined.api.client.JOClient;
import de.geomobile.joined.api.config.JOConfig;
import de.geomobile.joined.api.data.JOFriend;
import de.geomobile.joined.api.data.JOFriendFactory;
import de.geomobile.joined.api.data.JOUser;
import de.geomobile.joined.api.exception.JOFriendFinderHTTPException;
import de.geomobile.joined.api.exception.JOFriendFinderLoginException;
import de.geomobile.joined.api.exception.JOFriendFinderServerException;
import de.geomobile.joined.api.exception.JOFriendFinderSourceException;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

/**
 * This class contains a test case that simulates the process of adding other users to a friend list.
 */
public class JOAddFriendsTest extends TestCase
{
	/**
	 * This method performs the actual test case.
	 */
	public void test()
	{
		try
		{
			/* CREATE CLIENT */
			JOClient client = JOClient.createJoinedClient(JOConfig.JOINED_SERVER, JOConfig.JOINED_API_KEY);

			/* LOGIN USER */
			JOUser user1 = client.login(JOConfig.TEST_USER_1, JOConfig.TEST_USER_1);
			JOUser user2 = client.login(JOConfig.TEST_USER_2, JOConfig.TEST_USER_2);

			/* USER 1 SEARCHES FOR USER 2 */
			List<JOFriend> friends = client.searchFriends(user1, JOConfig.TEST_USER_2);

			assertTrue(friends.size() == 1);

			/* USER 1 INVITES USER 2 */
			client.addFriend(user1, friends.get(0));

			/* USER 2 GET FRIENDS TO ACCEPT LIST */
			friends = client.getFriends(user2);
			List<JOFriend> friendsToAccept = JOFriendFactory.instance().getFriendsToAccept(friends);

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
			friends = client.searchFriends(user2, JOConfig.TEST_USER_1);

			assertTrue(friends.size() == 1);

			/* USER 2 INVITES USER 1 */
			client.addFriend(user2, friends.get(0));

			/* USER 2 GET FRIEND LIST */
			friends = client.getFriends(user2);

			assertTrue(friends.size() == 1);

			/* USER 2 GET FRIENDS PENDING LIST */
			friends = client.getFriends(user2);
			List<JOFriend> friendsPending = JOFriendFactory.instance().getPendingFriends(friends);

			assertTrue(friendsPending.size() == 1);

			/* USER 1 GET FRIEND LIST */
			friends = client.getFriends(user1);

			assertTrue(friends.size() == 1);

			/* USER 1 GET FRIENDS TO ACCEPT LIST */
			friends = client.getFriends(user1);
			friendsToAccept = JOFriendFactory.instance().getFriendsToAccept(friends);

			assertTrue(friendsToAccept.size() == 1);
			
			/* USER 1 ACCEPTS USER 2 */
			client.acceptFriend(user1, friendsToAccept.get(0));

			/* LOGOUT USER */
			client.logout(user1);
			client.logout(user2);

			assertTrue(true);
		}
		catch (JOFriendFinderHTTPException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (JOFriendFinderUnexpectedException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (JOFriendFinderServerException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (JOFriendFinderLoginException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (JOFriendFinderSourceException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
