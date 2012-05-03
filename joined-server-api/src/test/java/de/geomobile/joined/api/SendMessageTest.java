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
import de.geomobile.joined.api.data.Message;
import de.geomobile.joined.api.data.User;
import de.geomobile.joined.api.exception.FriendFinderHTTPException;
import de.geomobile.joined.api.exception.FriendFinderLoginException;
import de.geomobile.joined.api.exception.FriendFinderServerException;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * This class contains a test case that simulates the process of sending messages to friends.
 */
public class SendMessageTest extends TestCase
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
			List<Friend> friends = client.getFriends(user1);

			assertTrue(friends.size() == 1);

			/* USER 1 SENDS A MESSAGE TO USER 2 */
			client.sendMessage(user1, friends.get(0), "Hello");

			/* USER 2 CHECKS FOR MESSAGES */
			List<Message> messages = client.getMessages(user2);

			assertTrue(messages.size() >= 1);

			/* USER 2 DELETES MESSAGES */
			client.deleteMessages(user2);
			messages = client.getMessages(user2);

			assertTrue(messages.size() == 0);

			client.sendMessage(user1, friends.get(0), "Hello");
			
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
	}
}
