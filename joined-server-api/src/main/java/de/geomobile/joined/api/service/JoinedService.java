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
package de.geomobile.joined.api.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import de.geomobile.joined.api.config.Config;
import de.geomobile.joined.api.exception.FriendFinderConflictException;
import de.geomobile.joined.api.exception.FriendFinderHTTPException;
import de.geomobile.joined.api.exception.FriendFinderLoginException;
import de.geomobile.joined.api.exception.FriendFinderServerException;
import de.geomobile.joined.api.exception.FriendFinderSourceException;
import de.geomobile.joined.api.exception.FriendFinderUnexpectedException;

/**
 * 
 */
public class JoinedService
{
	private String joinedServerUrl;
	private String joinedApiKey;

	/**
	 * @param joinedServerUrl
	 * @param joinedSecretSalt
	 * @return
	 */
	static public JoinedService createJoinedService(String joinedServerUrl, String joinedApiKey)
	{
		JoinedService service = new JoinedService();

		service.setJoinedServerUrl(joinedServerUrl);
		service.setJoinedApiKey(joinedApiKey);

		return service;
	}

	/**
	 * @return
	 */
	public String getJoinedServerUrl()
	{
		return joinedServerUrl;
	}

	/**
	 * @param joinedServerUrl
	 */
	public void setJoinedServerUrl(String joinedServerUrl)
	{
		this.joinedServerUrl = joinedServerUrl;
	}

	/**
	 * @return
	 */
	public String getJoinedApiKey()
	{
		return joinedApiKey;
	}

	/**
	 * @param joinedSecretSalt
	 */
	public void setJoinedApiKey(String joinedApiKey)
	{
		this.joinedApiKey = joinedApiKey;
	}

	/**
	 * Register for FriendFinder users. The nickname and password are mandatory.
	 * 
	 * @param name
	 *            The users nickname.
	 * @param password
	 *            The users password.
	 * @param profilImage
	 *            The users profile image exists.
	 * @return the generated attributes userid and secureToken as json object.
	 * @throws FriendFinderHTTPException
	 *             when another exception runs
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderConflictException
	 *             when nickname already exists
	 */
	public String ffRegister(String name, String password) throws FriendFinderHTTPException, FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderConflictException
	{
		try
		{
			// String hash = getSHA1Hash(name + getJoinedSecretSalt());
			// HttpPost httppost = new HttpPost(getJoinedServerUrl() + Config.FF_REGISTER + Config.HASH + hash);
			HttpPost httppost = new HttpPost(getJoinedServerUrl() + Config.FF_REGISTER + "/" + getJoinedApiKey());
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(Config.NICKNAME, new StringBody(name, Charset.forName(Config.UTF_8)));
			entity.addPart(Config.PASSWORD, new StringBody(getSHA1Hash(password + name), Charset.forName(Config.UTF_8)));
			// entity.addPart(Config.IMAGE_KEY, new FileBody(new File(Config.MEDIA_PATH + Config.IMAGE_PNG)));
			// entity.addPart(Config.IMAGE_HASH, new StringBody(calculateHash(Config.MEDIA_PATH + Config.IMAGE_PNG), Charset.forName(Config.UTF_8)));
			httppost.setEntity(entity);

			HttpClient httpClient = getNewHttpClient();
			HttpResponse httpResponse = httpClient.execute(httppost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CONFLICT)
			{
				throw new FriendFinderConflictException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (GeneralSecurityException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (Exception e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
	}

	/**
	 * @param name
	 * @param username
	 * @return
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	public String ffLogin(String username, String password) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		try
		{
			JSONObject jsonUserObject = new JSONObject();

			jsonUserObject.put(Config.NICKNAME, username);
			jsonUserObject.put(Config.PASSWORD, getSHA1Hash(password + username));

			HttpPost httppost = new HttpPost(getJoinedServerUrl() + Config.FF_LOGIN + "/" + getJoinedApiKey());

			httppost.setHeader("Content-type", "application/json");
			httppost.setEntity(new StringEntity(jsonUserObject.toString(), "UTF-8"));

			HttpClient httpClient = getNewHttpClient();

			HttpResponse httpResponse = httpClient.execute(httppost);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN)
			{
				throw new FriendFinderLoginException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (JSONException e)
		{
			throw new FriendFinderUnexpectedException("JSONException: " + e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new FriendFinderUnexpectedException("NoSuchAlgorithmException: " + e);
		}
	}

	/**
	 * @return
	 */
	private HttpClient getNewHttpClient()
	{
		try
		{
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 10000);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		}
		catch (Exception e)
		{
			return new DefaultHttpClient();
		}
	}

	/**
	 * @param userId
	 * @param secureToken
	 * @return
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderLoginException
	 */
	public String getFriends(String userId, String secureToken) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderLoginException
	{
		try
		{
			// HttpGet httpGet = new HttpGet(Config.FF_FRIENDS_URL + "/" + userId + Config.FRIENDS);
			HttpGet httpGet = new HttpGet(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId + Config.FRIENDS);

			httpGet.addHeader(getAuthenticationHeader(userId, secureToken));

			HttpClient httpClient = getNewHttpClient();

			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED)
			{
				throw new FriendFinderLoginException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}

		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Add a friend. The id, secureToken of User and friend's are mandatory.
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @param friendId
	 *            The friends id to add.
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderSourceException
	 */
	public void addFriend(String userId, String secureToken, String friendId) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderSourceException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPost httpPost = new HttpPost(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId + Config.FRIEND + friendId);
			httpPost.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST)
			{
				throw new FriendFinderSourceException(String.valueOf(HttpStatus.SC_BAD_REQUEST));
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND)
			{
				throw new FriendFinderSourceException(String.valueOf(HttpStatus.SC_NOT_FOUND));
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Accept a friend invitation. The id, secureToken of User and friend's are mandatory.
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @param friendId
	 *            The friends id to add.
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 */
	public String acceptFriend(String userId, String secureToken, String friendId) throws FriendFinderHTTPException, FriendFinderUnexpectedException, FriendFinderServerException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpPut = new HttpPut(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId + Config.FRIEND + friendId);
			httpPut.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpPut);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Delete a friend. The id, secureToken of User and friend's are mandatory.
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @param friendId
	 *            The friend's id to delete.
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 */
	public boolean deleteFriend(String userId, String secureToken, String friendId) throws FriendFinderHTTPException, FriendFinderUnexpectedException, FriendFinderServerException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpDelete httpDelete = new HttpDelete(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId + Config.FRIEND + friendId);
			httpDelete.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			return true;
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Search for users by nickname
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @param nickname
	 *            The friend's nickname to search.
	 * @return a JSON string
	 * @throws FriendFinderHTTPException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 */
	public String searchFriends(String userId, String secureToken, String nickname) throws FriendFinderHTTPException, FriendFinderServerException, FriendFinderUnexpectedException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpGet httpGet = new HttpGet(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId + Config.SEARCH + nickname);
			httpGet.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Update users current location
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @param latitude
	 * @param longitude
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderHTTPException
	 */
	public void updateUser(String userId, String secureToken, double latitude, double longitude) throws FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpput = new HttpPut(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId);
			httpput.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(Config.LATITUDE, new StringBody(String.valueOf(latitude), Charset.forName(Config.UTF_8)));
			entity.addPart(Config.LONGITUDE, new StringBody(String.valueOf(longitude), Charset.forName(Config.UTF_8)));

			httpput.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpput);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Activate or deactivate user
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @param active
	 *            active or deactive
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderHTTPException
	 */
	public void activateUser(String userId, String secureToken, boolean active) throws FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpput = new HttpPut(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId);
			httpput.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(Config.IS_ACTIVE, new StringBody(String.valueOf(active), Charset.forName(Config.UTF_8)));

			httpput.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpput);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * User logout.
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderHTTPException
	 */
	public void logoutUser(String userId, String secureToken) throws FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpput = new HttpPut(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId);
			httpput.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(Config.REGISTRATION_ID, new StringBody("", Charset.forName(Config.UTF_8)));
			entity.addPart(Config.IS_ACTIVE, new StringBody(String.valueOf(false), Charset.forName(Config.UTF_8)));
			httpput.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpput);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Delete current user
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderServerException
	 * @throws FriendFinderHTTPException
	 */
	public void deleteUser(String userId, String secureToken) throws FriendFinderUnexpectedException, FriendFinderServerException, FriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpDelete httpDelete = new HttpDelete(getJoinedServerUrl() + Config.FF_FRIENDS + "/" + userId);

			httpDelete.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Send a text message to friend
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @param friendsId
	 *            The friends id to send this message
	 * @param msg
	 *            The text message
	 * @return The HashMap String with id and time
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderHTTPException
	 */
	public String sendMessage(String userId, String secureToken, String friendsId, String msg) throws FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPost httpPost = new HttpPost(getJoinedServerUrl() + Config.FF_MESSAGE + "/" + userId + "/" + friendsId);
			httpPost.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(Config.TEXT, new StringBody(msg, Config.TEXT_PLAIN, Charset.forName(Config.UTF_8)));
			// entity.addPart(Config.ATTACHMENT, new StringBody(placeJson, Config.TEXT_PLAIN, Charset.forName(Config.UTF_8)));
			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Gets all unread Messages of the User.
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @return The Json String with Messages info.
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderHTTPException
	 */
	public String getMessages(String userId, String secureToken) throws FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpGet httpGet = new HttpGet(getJoinedServerUrl() + Config.FF_MESSAGE + "/" + userId);
			httpGet.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * Deletes all Messages from Server of the User as recipient in the DB.
	 * 
	 * @param userId
	 *            The users id.
	 * 
	 * @param secureToken
	 *            The users secureToken.
	 * @throws FriendFinderServerException
	 * @throws FriendFinderUnexpectedException
	 * @throws FriendFinderHTTPException
	 */
	public void deleteMessages(String userId, String secureToken) throws FriendFinderServerException, FriendFinderUnexpectedException, FriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpDelete httpDelete = new HttpDelete(getJoinedServerUrl() + Config.FF_MESSAGE + "/" + userId);
			httpDelete.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new FriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new FriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new FriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new FriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new FriendFinderHTTPException(e);
		}
	}

	/**
	 * @param userId
	 * @param secureToken
	 * @return
	 */
	private Header getAuthenticationHeader(String userId, String secureToken)
	{
		return BasicScheme.authenticate(new UsernamePasswordCredentials(userId, secureToken), Config.UTF_8, false);
	}

	/**
	 * @param httpResponse
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private String getJsonStringFromResponse(HttpResponse httpResponse) throws UnsupportedEncodingException, IllegalStateException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), Config.UTF_8));
		return reader.readLine();
	}

	/**
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private String getSHA1Hash(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md;
		StringBuffer buf = new StringBuffer();
		md = MessageDigest.getInstance(Config.SHA_1);
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes(Config.ISO_8859), 0, text.length());
		sha1hash = md.digest();
		for (int i = 0; i < sha1hash.length; i++)
		{
			int halfbyte = (sha1hash[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do
			{
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = sha1hash[i] & 0x0F;
			}
			while (two_halfs++ < 1);
		}

		return buf.toString();
	}
}
