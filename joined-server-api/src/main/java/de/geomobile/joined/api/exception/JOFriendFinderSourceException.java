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
package de.geomobile.joined.api.exception;

/**
 * This exception is thrown to indicate an internal error at the Joined server.
 */
public class JOFriendFinderSourceException extends Exception
{
	private static final long serialVersionUID = -2490317988463167773L;

	/**
	 * Constructs an exception with no detail message.
	 */
	public JOFriendFinderSourceException()
	{

	}

	/**
	 * Constructs an exception with a specified detail message.
	 * 
	 * @param detailMessage The detail message.
	 */
	public JOFriendFinderSourceException(String detailMessage)
	{
		super(detailMessage);
	}

	/**
	 * Constructs an exception based on an exception.
	 * 
	 * @param throwable The exception basis.
	 */
	public JOFriendFinderSourceException(Throwable throwable)
	{
		super(throwable);
	}

	/**
	 * Constructs an exception based on an exception and with a specified detail message.
	 * 
	 * @param detailMessage The detail message.
	 * @param throwable The exception basis.
	 */
	public JOFriendFinderSourceException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}
}
