package de.geomobile.joined.api.bearing.example;

import java.util.List;

import de.geomobile.joined.api.client.JoinedClient;
import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.data.Friend;
import de.geomobile.joined.api.data.User;
import de.geomobile.joined.api.exception.FriendFinderHTTPException;
import de.geomobile.joined.api.exception.FriendFinderLoginException;
import de.geomobile.joined.api.exception.FriendFinderServerException;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

public class JoinedManager {

	private static JoinedManager instance;
	private JoinedClient client;
	private User user;

	public static JoinedManager getInstance() {
		if (instance == null) {
			instance = new JoinedManager();
		}
		return instance;
	}
	
	/* LOGIN USER */
	public boolean login(String nickname, String password) {
		client = JoinedClient.createJoinedClient(Config.JOINED_SERVER,
				Config.JOINED_SECRET_SALT);
		try {
			
			user = client.login(nickname, password);
			return true;
		} catch (FriendFinderHTTPException e) {
			e.printStackTrace();
			return false;
		} catch (FriendFinderServerException e) {
			e.printStackTrace();
			return false;
		} catch (FriendFinderUnexpectedException e) {
			e.printStackTrace();
			return false;
		} catch (FriendFinderLoginException e) {
			e.printStackTrace();
			return false;
		}
	}

	/* GET LIST OF FRIENDS */
	public List<Friend> getFriends() {
		try {
			return client.getFriends(user);
		} catch (FriendFinderHTTPException e) {
			e.printStackTrace();
			return null;
		} catch (FriendFinderServerException e) {
			e.printStackTrace();
			return null;
		} catch (FriendFinderUnexpectedException e) {
			e.printStackTrace();
			return null;
		} catch (FriendFinderLoginException e) {
			e.printStackTrace();
			return null;
		}
	}
}
