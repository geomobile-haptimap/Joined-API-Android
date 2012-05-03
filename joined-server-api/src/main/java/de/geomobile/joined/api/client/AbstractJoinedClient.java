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

import de.geomobile.joined.api.data.Friend;
import de.geomobile.joined.api.data.Message;
import de.geomobile.joined.api.data.User;
import de.geomobile.joined.api.exception.FriendFinderConflictException;
import de.geomobile.joined.api.exception.FriendFinderHTTPException;
import de.geomobile.joined.api.exception.FriendFinderLoginException;
import de.geomobile.joined.api.exception.FriendFinderServerException;
import de.geomobile.joined.api.exception.FriendFinderSourceException;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * This class is the base class for the {@link JoinedClient} implementation.
 *
 */
public abstract class AbstractJoinedClient
{
	/**
	 * This methods creates a new user at the Joined server.
	 * 
	 * @param username The name for the new user.
	 * @param password The password for the new user.
	 * 
	 * @return The {@link User} object that represents the logged in user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderConflictException
	 */
	abstract public User register(String username, String password) throws FriendFinderHTTPException, FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderConflictException;
	
	/**
	 * This methods deletes an existing user at the Joined server.
	 * 
	 * @param user The {@link User} object that represents the logged in user. 
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderConflictException
	 */
	abstract public void delete(User user) throws FriendFinderHTTPException, FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderConflictException;
	
	/**
	 * This methods enables users to login at the Joined server.
	 * 
	 * @param username The name of the user.
	 * @param password The password of the user.
	 * 
	 * @return The {@link User} object that represents the logged in user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public User login(String username, String password) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
		
	/**
	 * This methods enables users to logout at the Joined server.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void logout(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This methods updates the position of the user at the Joined server.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * @param latitude The position of the user (latitude coordinate).
	 * @param longitude The position of the user (longitude coordinate).
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void updatePosition(User user, double latitude, double longitude) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This method updates the status of the user at the Joined server.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * @param active The status of the user (<code>true</code> for active and visible, <code>false</code> for inactive and invisible).
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void updateStatus(User user, boolean active) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This method get the friends of an user from the Joined server.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * 
	 * @return A list of {@link Friend} objects, each representing one friend of the user.  
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public List<Friend> getFriends(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;

	/**
	 * This method searches other users at the Joined server.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * @param nickname The nickname other users.
	 * 
	 * @return A list of {@link Friend} objects.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public List<Friend> searchFriends(User user, String nickname) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This methods sends a friends invitation to another user. If the other user accepts the invitation, the user appears at the list of friends. 
	 *  
	 * @param user The {@link User} object that represents the logged in user.
	 * @param friend The {@link Friend} object that represents the other user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void addFriend(User user, Friend friend) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException, FriendFinderSourceException;
	
	/**
	 * This method accepts the friend invitation of another user.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * @param friend The {@link Friend} object that represents the other user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void acceptFriend(User user, Friend friend) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This method deletes a friend from the list of friends.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * @param friend The {@link User} object that represents the other user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void deleteFriend(User user, Friend friend) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This methods sends a text message to a friend.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * @param friend The {@link Friend} object that represents the friend.
	 * @param message The text message.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void sendMessage(User user, Friend friend, String message) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This method returns a list of all available message from the inbox of the logged in user.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * 
	 * @return A list of {@link Message} objects, each representing a message from the inbox of the logged in user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public List<Message> getMessages(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
	
	/**
	 * This methods deletes all messages in the inbox of the logged in user.
	 * 
	 * @param user The {@link User} object that represents the logged in user.
	 * 
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	abstract public void deleteMessages(User user) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException;
}
