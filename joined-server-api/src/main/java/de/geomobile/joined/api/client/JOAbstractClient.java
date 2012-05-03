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

import de.geomobile.joined.api.data.JOFriend;
import de.geomobile.joined.api.data.JOMessage;
import de.geomobile.joined.api.data.JOUser;
import de.geomobile.joined.api.exception.JOFriendFinderConflictException;
import de.geomobile.joined.api.exception.JOFriendFinderHTTPException;
import de.geomobile.joined.api.exception.JOFriendFinderLoginException;
import de.geomobile.joined.api.exception.JOFriendFinderServerException;
import de.geomobile.joined.api.exception.JOFriendFinderSourceException;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

/**
 * This class is the base class for the {@link JOClient} implementation.
 *
 */
public abstract class JOAbstractClient
{
	/**
	 * This methods creates a new user at the Joined server.
	 * 
	 * @param username The name for the new user.
	 * @param password The password for the new user.
	 * 
	 * @return The {@link JOUser} object that represents the logged in user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderConflictException
	 */
	abstract public JOUser register(String username, String password) throws JOFriendFinderHTTPException, JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderConflictException;
	
	/**
	 * This methods deletes an existing user at the Joined server.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user. 
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderConflictException
	 */
	abstract public void delete(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderConflictException;
	
	/**
	 * This methods enables users to login at the Joined server.
	 * 
	 * @param username The name of the user.
	 * @param password The password of the user.
	 * 
	 * @return The {@link JOUser} object that represents the logged in user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public JOUser login(String username, String password) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
		
	/**
	 * This methods enables users to logout at the Joined server.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void logout(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This methods updates the position of the user at the Joined server.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * @param latitude The position of the user (latitude coordinate).
	 * @param longitude The position of the user (longitude coordinate).
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void updatePosition(JOUser user, double latitude, double longitude) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This method updates the status of the user at the Joined server.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * @param active The status of the user (<code>true</code> for active and visible, <code>false</code> for inactive and invisible).
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void updateStatus(JOUser user, boolean active) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This method get the friends of an user from the Joined server.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * 
	 * @return A list of {@link JOFriend} objects, each representing one friend of the user.  
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public List<JOFriend> getFriends(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;

	/**
	 * This method searches other users at the Joined server.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * @param nickname The nickname other users.
	 * 
	 * @return A list of {@link JOFriend} objects.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public List<JOFriend> searchFriends(JOUser user, String nickname) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This methods sends a friends invitation to another user. If the other user accepts the invitation, the user appears at the list of friends. 
	 *  
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * @param friend The {@link JOFriend} object that represents the other user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void addFriend(JOUser user, JOFriend friend) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException, JOFriendFinderSourceException;
	
	/**
	 * This method accepts the friend invitation of another user.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * @param friend The {@link JOFriend} object that represents the other user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void acceptFriend(JOUser user, JOFriend friend) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This method deletes a friend from the list of friends.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * @param friend The {@link JOUser} object that represents the other user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void deleteFriend(JOUser user, JOFriend friend) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This methods sends a text message to a friend.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * @param friend The {@link JOFriend} object that represents the friend.
	 * @param message The text message.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void sendMessage(JOUser user, JOFriend friend, String message) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This method returns a list of all available message from the inbox of the logged in user.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * 
	 * @return A list of {@link JOMessage} objects, each representing a message from the inbox of the logged in user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public List<JOMessage> getMessages(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
	
	/**
	 * This methods deletes all messages in the inbox of the logged in user.
	 * 
	 * @param user The {@link JOUser} object that represents the logged in user.
	 * 
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	abstract public void deleteMessages(JOUser user) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException;
}
