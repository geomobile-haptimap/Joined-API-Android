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
import de.geomobile.joined.api.data.JOUser;
import de.geomobile.joined.api.exception.JOFriendFinderHTTPException;
import de.geomobile.joined.api.exception.JOFriendFinderLoginException;
import de.geomobile.joined.api.exception.JOFriendFinderServerException;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

/**
 * This class contains a test case that simulates the process of searching other users.
 */
public class JOSearchFriendsTest extends TestCase
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
			JOUser user = client.login(JOConfig.TEST_USER_1, JOConfig.TEST_USER_1);

			/* SEARCH FRIENDS */
			List<JOFriend> friends = client.searchFriends(user, JOConfig.TEST_USER_2);

			assertTrue(friends.size() > 0);
			
			/* LOGOUT USER */
			client.logout(user);			
		}
		catch (JOFriendFinderHTTPException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (JOFriendFinderServerException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (JOFriendFinderUnexpectedException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (JOFriendFinderLoginException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
