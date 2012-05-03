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
import de.geomobile.joined.api.client.JOClient;
import de.geomobile.joined.api.config.JOConfig;
import de.geomobile.joined.api.data.JOUser;

/**
 * This class combines all test cases to one test suite.
 */
public class JOAllTests
{
	/**
	 * This method combines all test cases to one test suite.
	 * 
	 * @return The test case that contains the test suite.
	 */
	public static Test suite()
	{
		clean();

		TestSuite suite = new TestSuite(JOAllTests.class.getName());
		// $JUnit-BEGIN$
		suite.addTestSuite(JORegisterTest.class);
		suite.addTestSuite(JOLoginTest.class);
		suite.addTestSuite(JOSearchFriendsTest.class);		
		suite.addTestSuite(JOGetFriendsTest.class);
		suite.addTestSuite(JOUpdateStatusTest.class);
		suite.addTestSuite(JOUpdatePositionTest.class);
		suite.addTestSuite(JOAddFriendsTest.class);
		suite.addTestSuite(JOSendMessageTest.class);
		suite.addTestSuite(JODeleteTest.class);
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
			JOClient client = JOClient.createJoinedClient(JOConfig.JOINED_SERVER, JOConfig.OPEN_API_KEY);
			JOUser user1 = client.login(JOConfig.TEST_USER_1, JOConfig.TEST_USER_1);
			client.delete(user1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			JOClient client = JOClient.createJoinedClient(JOConfig.JOINED_SERVER, JOConfig.OPEN_API_KEY);
			JOUser user2 = client.login(JOConfig.TEST_USER_2, JOConfig.TEST_USER_2);
			client.delete(user2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
