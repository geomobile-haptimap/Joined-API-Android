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
package de.geomobile.joined.api.config;

/**
 * This class contains various static variables that provide internal management information for communicating with the Joined server. 
 */
public class JOConfig
{
	/* JOINED PARAMETER */

	/**
	 * The URL of the Joined server.
	 */
	public final static String JOINED_SERVER = "https://joinedserver.geomobile.de:7012/friendfinder";
	
	/**
	 * The unqique identifier for the public Joined API.
	 */
	public final static String JOINED_API_KEY = "5923078164";
		
	/* UNIT TEST */

	/**
	 * The name and password for the first user to be created during JUnit tests.
	 */
	public static String TEST_USER_1 = "dummy-1";
	
	/**
	 * The name and password for the second user to be created during JUnit tests.
	 */
	public static final String TEST_USER_2 = "dummy-2";

	/* STATUS CODES */

	/**
	 * This variable indicates that there is no friend relationship to another user ({@see Friend}).
	 */
	public static final int FRIEND_STATUS_NO = 0;
	
	/**
	 * This variable indicates that there is an active friend relationship to another user ({@see Friend}).
	 */
	public static final int FRIEND_STATUS_YES = 1;
	
	/**
	 * This variable indicates that there is a pending friend invitation for another user ({@see Friend}).
	 */
	public static final int FRIEND_STATUS_PENDING = 2;

	/* MESSAGES */

	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String TEXT_MSG = "text";

	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final int MESSAGE_FROM = 0;
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final int MESSAGE_TO = 1;

	/* RESOURCES */

	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String FF_REGISTER = "/ffregister";

	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String FF_LOGIN = "/fflogin";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String FF_FRIENDS = "/user";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String FF_MESSAGE = "/message";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String FRIENDS = "/friends";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String FRIEND = "/friend/";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String SEARCH = "/search/";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String IMAGE = "/image/";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String HASH = "?hash=";

	/* DOCUMENT ELEMENTS */
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String NICKNAME = "nickname";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String PASSWORD = "password";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String REGISTRATION_ID = "registrationId";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String FB_ID = "fbId";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String SECURE_TOKEN = "secureToken";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String ID = "id";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String ATTACHMENT = "attachment";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String TITLE = "title";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String IMAGE_HASH = "imageHash";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String ACTIVE = "active";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String STATUS = "status";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String LAST_POS_UPDATE = "lastPosUpdate";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String LATITUDE = "latitude";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String LONGITUDE = "longitude";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String CREATION_DATE = "creationDate";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String SENDER_ID = "senderId";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String IMAGE_KEY = "image";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String IS_ACTIVE = "isActive";

	/* CONSTANTS */

	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String UTF_8 = "UTF-8";

	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String TEXT = "text";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String TEXT_PLAIN = "text/plain";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String SHA_1 = "SHA-1";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String ISO_8859 = "iso-8859-1";
	
	/**
	 * This variable is not documented and for internal use only.
	 */
	public static final String MD5 = "MD5";
}
