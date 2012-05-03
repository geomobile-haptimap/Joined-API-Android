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

import de.geomobile.joined.api.data.Friend;
import de.geomobile.joined.api.data.Message;
import de.geomobile.joined.api.data.User;
import de.geomobile.joined.api.data.UserFactory;
import de.geomobile.joined.api.exception.FriendFinderConflictException;
import de.geomobile.joined.api.exception.FriendFinderHTTPException;
import de.geomobile.joined.api.exception.FriendFinderLoginException;
import de.geomobile.joined.api.exception.FriendFinderServerException;
import de.geomobile.joined.api.exception.FriendFinderSourceException;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;
import de.geomobile.joined.api.service.JSONParser;
import de.geomobile.joined.api.service.JoinedService;

/**
 * This class contains various methods for accessing the Joined server. 
 */
public class JoinedClient extends AbstractJoinedClient
{
	private static Logger LOGGER = Logger.getLogger(JoinedClient.class.getName());

	private JoinedService joinedService;

	/**
	 * This private methods is used internally to create a client for the Joined server.
	 * 
	 * @param joinedServerUrl The URL of the Joined server.
	 * @param joinedSecretSalt The secret salt for accessing the Joined server.
	 */
	private JoinedClient(String joinedServerUrl, String joinedApiKey)
	{
		this.joinedService = JoinedService.createJoinedService(joinedServerUrl, joinedApiKey);
	}

	/**
	 * This methods enables users to create an easy to use client for the Joined server.
	 * 
	 * @param pJoinedServerUrl The URL of the Joined server.
	 * @param joinedSecretSalt The secret salt for accessing the Joined server.
	 * @return
	 */
	// static public JoinedClient createJoinedClient(String pJoinedServerUrl, String joinedSecretSalt)
	static public JoinedClient createJoinedClient(String pJoinedServerUrl, String joinedApiKey)
	{
		JoinedClient client = new JoinedClient(pJoinedServerUrl, joinedApiKey);
		LOGGER.info(client.toString());
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#register(java.lang.String, java.lang.String)
	 */
	public User register(String username, String password) throws FriendFinderHTTPException, FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderConflictException
	{
		String json = joinedService.ffRegister(username, password);
		User user = UserFactory.instance().createUser(json);
		LOGGER.info(user.toString());
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#delete(de.geomobile.joined.api.object.User)
	 */
	public void delete(User user) throws FriendFinderHTTPException, FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderConflictException
	{
		joinedService.deleteUser(user.getId(), user.getSecureToken());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#logout(de.geomobile.joined.api.object.User)
	 */
	public void logout(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		joinedService.logoutUser(user.getId(), user.getSecureToken());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#joinedLogin(java.lang.String, java.lang.String)
	 */
	public User login(String username, String password) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		String json = joinedService.ffLogin(username, password);
		User user = UserFactory.instance().createUser(json);
		LOGGER.info(user.toString());
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#updatePosition(de.geomobile.joined.api.object.User, double, double)
	 */
	public void updatePosition(User user, double latitude, double longitude) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		joinedService.updateUser(user.getId(), user.getSecureToken(), latitude, longitude);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#setStatus(de.geomobile.joined.api.object.User, boolean)
	 */
	public void updateStatus(User user, boolean active) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		joinedService.activateUser(user.getId(), user.getSecureToken(), active);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#getFriends(de.geomobile.joined.api.object.User)
	 */
	public List<Friend> getFriends(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		String json = joinedService.getFriends(user.getId(), user.getSecureToken());
		// List<Friend> friends = FriendFactory.instance().createFriends(json);
		List<Friend> friends = JSONParser.getInstance().getFriendList(json);
		for (Friend friend : friends)
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
	public List<Friend> searchFriends(User user, String nickname) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		String json = joinedService.searchFriends(user.getId(), user.getSecureToken(), nickname);
		// List<Friend> friends = FriendFactory.instance().createFriends(json);
		List<Friend> friends = JSONParser.getInstance().getFriendList(json);
		for (Friend friend : friends)
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
	public void addFriend(User user, Friend friend) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException, FriendFinderSourceException
	{
		joinedService.addFriend(user.getId(), user.getSecureToken(), friend.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#accept(de.geomobile.joined.api.object.User, de.geomobile.joined.api.object.Friend)
	 */
	public void acceptFriend(User user, Friend friend) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		joinedService.acceptFriend(user.getId(), user.getSecureToken(), friend.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.AbstractJoinedClient#accept(de.geomobile.joined.api.object.User, de.geomobile.joined.api.object.Friend)
	 */
	public void deleteFriend(User user, Friend friend) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		joinedService.deleteFriend(user.getId(), user.getSecureToken(), friend.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.client.AbstractJoinedClient#sendMessage(de.geomobile.joined.api.data.User, de.geomobile.joined.api.data.Friend, java.lang.String)
	 */
	public void sendMessage(User user, Friend friend, String message) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		joinedService.sendMessage(user.getId(), user.getSecureToken(), friend.getId(), message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.geomobile.joined.api.client.AbstractJoinedClient#getMessages(de.geomobile.joined.api.data.User)
	 */
	public List<Message> getMessages(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		String json = joinedService.getMessages(user.getId(), user.getSecureToken());
		List<Message> messages = JSONParser.getInstance().getMessages(json);
		for (Message message : messages)
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
	public void deleteMessages(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		joinedService.deleteMessages(user.getId(), user.getSecureToken());
	}

}
