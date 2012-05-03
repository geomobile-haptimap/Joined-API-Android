package de.geomobile.joined.api.bearing.example;

import java.util.List;

import de.geomobile.joined.api.client.JOClient;
import de.geomobile.joined.api.config.JOConfig;
import de.geomobile.joined.api.data.JOFriend;
import de.geomobile.joined.api.data.JOUser;
import de.geomobile.joined.api.exception.JOFriendFinderHTTPException;
import de.geomobile.joined.api.exception.JOFriendFinderLoginException;
import de.geomobile.joined.api.exception.JOFriendFinderServerException;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

public class JoinedManager
{

	private static JoinedManager instance;
	private JOClient client;
	private JOUser user;

	public static JoinedManager getInstance()
	{
		if (instance == null)
		{
			instance = new JoinedManager();
		}
		return instance;
	}

	/* LOGIN USER */
	public boolean login(String nickname, String password)
	{
		client = JOClient.createJoinedClient(JOConfig.JOINED_SERVER, JOConfig.OPEN_API_KEY);
		try
		{
			user = client.login(nickname, password);
			return true;
		}
		catch (JOFriendFinderHTTPException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (JOFriendFinderServerException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (JOFriendFinderUnexpectedException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (JOFriendFinderLoginException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/* GET LIST OF FRIENDS */
	public List<JOFriend> getFriends()
	{
		try
		{
			return client.getFriends(user);
		}
		catch (JOFriendFinderHTTPException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (JOFriendFinderServerException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (JOFriendFinderUnexpectedException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (JOFriendFinderLoginException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
