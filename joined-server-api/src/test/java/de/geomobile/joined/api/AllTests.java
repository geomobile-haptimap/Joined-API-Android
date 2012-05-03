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

import junit.framework.Test;
import junit.framework.TestSuite;
import de.geomobile.joined.api.client.JoinedClient;
import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.data.User;

/**
 * This class combines all test cases to one test suite.
 */
public class AllTests
{
	/**
	 * This method combines all test cases to one test suite.
	 * 
	 * @return The test case that contains the test suite.
	 */
	public static Test suite()
	{
		clean();

		TestSuite suite = new TestSuite(AllTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(RegisterTest.class);
		suite.addTestSuite(LoginTest.class);
		suite.addTestSuite(SearchFriendsTest.class);		
		suite.addTestSuite(GetFriendsTest.class);
		suite.addTestSuite(UpdateStatusTest.class);
		suite.addTestSuite(UpdatePositionTest.class);
		suite.addTestSuite(AddFriendsTest.class);
		suite.addTestSuite(SendMessageTest.class);
		suite.addTestSuite(DeleteTest.class);
		// $JUnit-END$
		return suite;
	}

	/**
	 * This method cleans the user database at the Joined server (removal of test users).
	 */
	private static void clean()
	{
		try
		{
			JoinedClient client = JoinedClient.createJoinedClient(Config.JOINED_SERVER, Config.OPEN_API_KEY);
			User user1 = client.login(Config.TEST_USER_1, Config.TEST_USER_1);
			client.delete(user1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			JoinedClient client = JoinedClient.createJoinedClient(Config.JOINED_SERVER, Config.OPEN_API_KEY);
			User user2 = client.login(Config.TEST_USER_2, Config.TEST_USER_2);
			client.delete(user2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
