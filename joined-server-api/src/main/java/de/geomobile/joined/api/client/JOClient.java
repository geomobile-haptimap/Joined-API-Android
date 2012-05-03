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
package de.geomobile.joined.api.client;

import java.util.List;
import java.util.logging.Logger;

import de.geomobile.joined.api.data.JOFriend;
import de.geomobile.joined.api.data.JOMessage;
import de.geomobile.joined.api.data.JOUser;
import de.geomobile.joined.api.data.JOUserFactory;
import de.geomobile.joined.api.exception.JOFriendFinderConflictException;
import de.geomobile.joined.api.exception.JOFriendFinderHTTPException;
import de.geomobile.joined.api.exception.JOFriendFinderLoginException;
import de.geomobile.joined.api.exception.JOFriendFinderServerException;
import de.geomobile.joined.api.exception.JOFriendFinderSourceException;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;
import de.geomobile.joined.api.service.JOWebServiceParser;
import de.geomobile.joined.api.service.JOWebService;

/**
 * This class contains various methods for accessing the Joined server. 
 */
public class JOClient extends JOAbstractClient
{
	private static Logger LOGGER = Logger.getLogger(JOClient.class.getName());

	private JOWebService joinedService;

	/**
	 * This private method is used internally to create a client for the Joined server.
	 * 
	 * @param joinedServerUrl The URL of the Joined server.
	 * @param joinedSecretSalt The secret salt for accessing the Joined server.
	 */
	private JOClient(String joinedServerUrl, String joinedApiKey)
	{
		this.joinedService = JOWebService.createJoinedService(joinedServerUrl, joinedApiKey);
	}

	/**
	 * This method enables users to create an easy to use client for the Joined server.
	 * 
	 * @param pJoinedServerUrl The URL of the Joined server.
	 * @param joinedSecretSalt The secret salt for accessing the Joined server.
	 * @return
	 */
	// static public JoinedClient createJoinedClient(String pJoinedServerUrl, String joinedSecretSalt)
	static public JOClient createJoinedClient(String pJoinedServerUrl, String joinedApiKey)
	{
		JOClient client = new JOClient(pJoinedServerUrl, joinedApiKey);
		LOGGER.info(client.toString());
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#register(java.lang.String, java.lang.String)
	 */
	public JOUser register(String username, String password) throws JOFriendFinderHTTPException, JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderConflictException
	{
		String json = joinedService.ffRegister(username, password);
		JOUser user = JOUserFactory.instance().createUser(json);
		LOGGER.info(user.toString());
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#delete(de.geomobile.joined.api.object.User)
	 */
	public void delete(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderConflictException
	{
		joinedService.deleteUser(user.getId(), user.getSecureToken());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#logout(de.geomobile.joined.api.object.User)
	 */
	public void logout(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		joinedService.logoutUser(user.getId(), user.getSecureToken());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#joinedLogin(java.lang.String, java.lang.String)
	 */
	public JOUser login(String username, String password) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		String json = joinedService.ffLogin(username, password);
		JOUser user = JOUserFactory.instance().createUser(json);
		LOGGER.info(user.toString());
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#updatePosition(de.geomobile.joined.api.object.User, double, double)
	 */
	public void updatePosition(JOUser user, double latitude, double longitude) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		joinedService.updateUser(user.getId(), user.getSecureToken(), latitude, longitude);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#setStatus(de.geomobile.joined.api.object.User, boolean)
	 */
	public void updateStatus(JOUser user, boolean active) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		joinedService.activateUser(user.getId(), user.getSecureToken(), active);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#getFriends(de.geomobile.joined.api.object.User)
	 */
	public List<JOFriend> getFriends(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		String json = joinedService.getFriends(user.getId(), user.getSecureToken());
		// List<Friend> friends = FriendFactory.instance().createFriends(json);
		List<JOFriend> friends = JOWebServiceParser.getInstance().getFriendList(json);
		for (JOFriend friend : friends)
		{
			LOGGER.info(friend.toString());
		}
		return friends;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#searchFriends(de.geomobile.joined.api.object.User, java.lang.String)
	 */
	public List<JOFriend> searchFriends(JOUser user, String nickname) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		String json = joinedService.searchFriends(user.getId(), user.getSecureToken(), nickname);
		// List<Friend> friends = FriendFactory.instance().createFriends(json);
		List<JOFriend> friends = JOWebServiceParser.getInstance().getFriendList(json);
		for (JOFriend friend : friends)
		{
			LOGGER.info(friend.toString());
		}
		return friends;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#accept(de.geomobile.joined.api.object.User, de.geomobile.joined.api.object.Friend)
	 */
	public void addFriend(JOUser user, JOFriend friend) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException, JOFriendFinderSourceException
	{
		joinedService.addFriend(user.getId(), user.getSecureToken(), friend.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#accept(de.geomobile.joined.api.object.User, de.geomobile.joined.api.object.Friend)
	 */
	public void acceptFriend(JOUser user, JOFriend friend) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		joinedService.acceptFriend(user.getId(), user.getSecureToken(), friend.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#accept(de.geomobile.joined.api.object.User, de.geomobile.joined.api.object.Friend)
	 */
	public void deleteFriend(JOUser user, JOFriend friend) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		joinedService.deleteFriend(user.getId(), user.getSecureToken(), friend.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.client.AbstractJoinedClient#sendMessage(de.geomobile.joined.api.data.User, de.geomobile.joined.api.data.Friend, java.lang.String)
	 */
	public void sendMessage(JOUser user, JOFriend friend, String message) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		joinedService.sendMessage(user.getId(), user.getSecureToken(), friend.getId(), message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.client.AbstractJoinedClient#getMessages(de.geomobile.joined.api.data.User)
	 */
	public List<JOMessage> getMessages(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		String json = joinedService.getMessages(user.getId(), user.getSecureToken());
		List<JOMessage> messages = JOWebServiceParser.getInstance().getMessages(json);
		for (JOMessage message : messages)
		{
			LOGGER.info(message.toString());
		}
		return messages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.client.AbstractJoinedClient#deleteMessages(de.geomobile.joined.api.data.User)
	 */
	public void deleteMessages(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		joinedService.deleteMessages(user.getId(), user.getSecureToken());
	}

}
