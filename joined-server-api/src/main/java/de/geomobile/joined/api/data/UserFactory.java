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

import org.json.JSONException;
import org.json.JSONObject;

import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * This class contains various methods for creating user information.
 */
public class UserFactory
{
	static private UserFactory _instance = null;

	/**
	 * This constructor is for internal use only.
	 */
	private UserFactory()
	{

	}

	/**
	 * This methods returns an instance of the class.
	 * 
	 * @return An instance of the class.
	 */
	static public UserFactory instance()
	{
		if (null == _instance)
		{
			_instance = new UserFactory();
		}
		return _instance;
	}

	/**
	 * This method creates an {@link User} object from a Json document.
	 * 
	 * @param json
	 *            The Json document.
	 * 
	 * @return The {@link User} object.
	 * 
	 * @throws FriendFinderUnexpectedException
	 */
	public User createUser(String json) throws FriendFinderUnexpectedException
	{
		try
		{
			JSONObject jsonObject = new JSONObject(json);

			User user = new User();

			user.setId(jsonObject.getString(Config.ID));
			user.setSecureToken(jsonObject.getString(Config.SECURE_TOKEN));

			return user;
		}
		catch (JSONException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
	}
}
