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

/**
 * This class contains various methods for receiving information about a user.
 */
public class JOUser
{
	private String id;
	private String secureToken;

	/**
	 * This method returns the identifier of the user.
	 * 
	 * @return The identifier of the user.
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * This method sets the identifier for the user.
	 * 
	 * @param id
	 *            The identifier for the user.
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * This method returns the secure token of the user.
	 * 
	 * @return The secure token of the user.
	 */
	public String getSecureToken()
	{
		return secureToken;
	}

	/**
	 * This method sets the secure token for the user.
	 * 
	 * @param secureToken
	 *            The secure token for the user.
	 */
	public void setSecureToken(String secureToken)
	{
		this.secureToken = secureToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "USER = { id: '" + getId() + "', secureToken: '" + getSecureToken() + "' }";
	}
}
