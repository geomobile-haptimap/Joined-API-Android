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

import de.geomobile.joined.api.config.JOConfig;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

/**
 * This class contains various methods for creating user information.
 */
public class JOUserFactory
{
	static private JOUserFactory _instance = null;

	/**
	 * This constructor is for internal use only.
	 */
	private JOUserFactory()
	{

	}

	/**
	 * This methods returns an instance of the class.
	 * 
	 * @return An instance of the class.
	 */
	static public JOUserFactory instance()
	{
		if (null == _instance)
		{
			_instance = new JOUserFactory();
		}
		return _instance;
	}

	/**
	 * This method creates an {@link JOUser} object from a Json document.
	 * 
	 * @param json
	 *            The Json document.
	 * 
	 * @return The {@link JOUser} object.
	 * 
	 * @throws JOFriendFinderUnexpectedException
	 */
	public JOUser createUser(String json) throws JOFriendFinderUnexpectedException
	{
		try
		{
			JSONObject jsonObject = new JSONObject(json);

			JOUser user = new JOUser();

			user.setId(jsonObject.getString(JOConfig.ID));
			user.setSecureToken(jsonObject.getString(JOConfig.SECURE_TOKEN));

			return user;
		}
		catch (JSONException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
	}
}
