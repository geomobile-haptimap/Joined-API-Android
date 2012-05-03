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

import junit.framework.TestCase;
import de.geomobile.joined.api.client.JoinedClient;
import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.data.User;
import de.geomobile.joined.api.exception.FriendFinderHTTPException;
import de.geomobile.joined.api.exception.FriendFinderLoginException;
import de.geomobile.joined.api.exception.FriendFinderServerException;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * This class contains a test case that simulates the process of updating the status of a user.
 */
public class UpdateStatusTest extends TestCase
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
			User user = client.login(Config.TEST_USER_1, Config.TEST_USER_1);

			/* DEACTIVATE USER */
			client.updateStatus(user, false);

			/* ACTIVATE USER */
			client.updateStatus(user, true);

			/* LOGOUT USER */
			client.logout(user);

			assertTrue(true);
		}
		catch (FriendFinderHTTPException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (FriendFinderServerException e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
		catch (FriendFinderUnexpectedException e)
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
