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

import de.geomobile.joined.api.config.JOConfig;
import de.geomobile.joined.api.exception.JOFriendFinderConflictException;
import de.geomobile.joined.api.exception.JOFriendFinderHTTPException;
import de.geomobile.joined.api.exception.JOFriendFinderLoginException;
import de.geomobile.joined.api.exception.JOFriendFinderServerException;
import de.geomobile.joined.api.exception.JOFriendFinderSourceException;
import de.geomobile.joined.api.exception.JOFriendFinderUnexpectedException;

/**
 * 
 */
public class JOWebService
{
	private String joinedServerUrl;
	private String joinedApiKey;

	/**
	 * @param joinedServerUrl
	 * @param joinedSecretSalt
	 * @return
	 */
	static public JOWebService createJoinedService(String joinedServerUrl, String joinedApiKey)
	{
		JOWebService service = new JOWebService();

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
	 * @throws JOFriendFinderHTTPException
	 *             when another exception runs
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderConflictException
	 *             when nickname already exists
	 */
	public String ffRegister(String name, String password) throws JOFriendFinderHTTPException, JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderConflictException
	{
		try
		{
			// String hash = getSHA1Hash(name + getJoinedSecretSalt());
			// HttpPost httppost = new HttpPost(getJoinedServerUrl() + Config.FF_REGISTER + Config.HASH + hash);
			HttpPost httppost = new HttpPost(getJoinedServerUrl() + JOConfig.FF_REGISTER + "/" + getJoinedApiKey());
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(JOConfig.NICKNAME, new StringBody(name, Charset.forName(JOConfig.UTF_8)));
			entity.addPart(JOConfig.PASSWORD, new StringBody(getSHA1Hash(password + name), Charset.forName(JOConfig.UTF_8)));
			// entity.addPart(Config.IMAGE_KEY, new FileBody(new File(Config.MEDIA_PATH + Config.IMAGE_PNG)));
			// entity.addPart(Config.IMAGE_HASH, new StringBody(calculateHash(Config.MEDIA_PATH + Config.IMAGE_PNG), Charset.forName(Config.UTF_8)));
			httppost.setEntity(entity);

			HttpClient httpClient = getNewHttpClient();
			HttpResponse httpResponse = httpClient.execute(httppost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CONFLICT)
			{
				throw new JOFriendFinderConflictException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (GeneralSecurityException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (Exception e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
	}

	/**
	 * @param name
	 * @param username
	 * @return
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	public String ffLogin(String username, String password) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		try
		{
			JSONObject jsonUserObject = new JSONObject();

			jsonUserObject.put(JOConfig.NICKNAME, username);
			jsonUserObject.put(JOConfig.PASSWORD, getSHA1Hash(password + username));

			HttpPost httppost = new HttpPost(getJoinedServerUrl() + JOConfig.FF_LOGIN + "/" + getJoinedApiKey());

			httppost.setHeader("Content-type", "application/json");
			httppost.setEntity(new StringEntity(jsonUserObject.toString(), "UTF-8"));

			HttpClient httpClient = getNewHttpClient();

			HttpResponse httpResponse = httpClient.execute(httppost);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN)
			{
				throw new JOFriendFinderLoginException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (JSONException e)
		{
			throw new JOFriendFinderUnexpectedException("JSONException: " + e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new JOFriendFinderUnexpectedException("NoSuchAlgorithmException: " + e);
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

			SSLSocketFactory sf = new JOSSLSocketFactory(trustStore);
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
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderLoginException
	 */
	public String getFriends(String userId, String secureToken) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderLoginException
	{
		try
		{
			HttpGet httpGet = new HttpGet(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId + JOConfig.FRIENDS);

			System.out.println(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId + JOConfig.FRIENDS);
			
			httpGet.addHeader(getAuthenticationHeader(userId, secureToken));

			HttpClient httpClient = getNewHttpClient();

			HttpResponse httpResponse = httpClient.execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED)
			{
				throw new JOFriendFinderLoginException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}

		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderSourceException
	 */
	public void addFriend(String userId, String secureToken, String friendId) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderSourceException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPost httpPost = new HttpPost(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId + JOConfig.FRIEND + friendId);
			httpPost.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST)
			{
				throw new JOFriendFinderSourceException(String.valueOf(HttpStatus.SC_BAD_REQUEST));
			}
			else if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND)
			{
				throw new JOFriendFinderSourceException(String.valueOf(HttpStatus.SC_NOT_FOUND));
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 */
	public String acceptFriend(String userId, String secureToken, String friendId) throws JOFriendFinderHTTPException, JOFriendFinderUnexpectedException, JOFriendFinderServerException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpPut = new HttpPut(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId + JOConfig.FRIEND + friendId);
			httpPut.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpPut);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 */
	public boolean deleteFriend(String userId, String secureToken, String friendId) throws JOFriendFinderHTTPException, JOFriendFinderUnexpectedException, JOFriendFinderServerException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpDelete httpDelete = new HttpDelete(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId + JOConfig.FRIEND + friendId);
			httpDelete.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			return true;
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderHTTPException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 */
	public String searchFriends(String userId, String secureToken, String nickname) throws JOFriendFinderHTTPException, JOFriendFinderServerException, JOFriendFinderUnexpectedException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpGet httpGet = new HttpGet(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId + JOConfig.SEARCH + nickname);
			httpGet.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderHTTPException
	 */
	public void updateUser(String userId, String secureToken, double latitude, double longitude) throws JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpput = new HttpPut(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId);
			httpput.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(JOConfig.LATITUDE, new StringBody(String.valueOf(latitude), Charset.forName(JOConfig.UTF_8)));
			entity.addPart(JOConfig.LONGITUDE, new StringBody(String.valueOf(longitude), Charset.forName(JOConfig.UTF_8)));

			httpput.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpput);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderHTTPException
	 */
	public void activateUser(String userId, String secureToken, boolean active) throws JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpput = new HttpPut(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId);
			httpput.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(JOConfig.IS_ACTIVE, new StringBody(String.valueOf(active), Charset.forName(JOConfig.UTF_8)));

			httpput.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpput);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderHTTPException
	 */
	public void logoutUser(String userId, String secureToken) throws JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPut httpput = new HttpPut(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId);
			httpput.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(JOConfig.REGISTRATION_ID, new StringBody("", Charset.forName(JOConfig.UTF_8)));
			entity.addPart(JOConfig.IS_ACTIVE, new StringBody(String.valueOf(false), Charset.forName(JOConfig.UTF_8)));
			httpput.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpput);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderHTTPException
	 */
	public void deleteUser(String userId, String secureToken) throws JOFriendFinderUnexpectedException, JOFriendFinderServerException, JOFriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpDelete httpDelete = new HttpDelete(getJoinedServerUrl() + JOConfig.FF_FRIENDS + "/" + userId);

			httpDelete.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderHTTPException
	 */
	public String sendMessage(String userId, String secureToken, String friendsId, String msg) throws JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpPost httpPost = new HttpPost(getJoinedServerUrl() + JOConfig.FF_MESSAGE + "/" + userId + "/" + friendsId);
			httpPost.addHeader(getAuthenticationHeader(userId, secureToken));
			MultipartEntity entity = new MultipartEntity();
			entity.addPart(JOConfig.TEXT, new StringBody(msg, JOConfig.TEXT_PLAIN, Charset.forName(JOConfig.UTF_8)));
			// entity.addPart(Config.ATTACHMENT, new StringBody(placeJson, Config.TEXT_PLAIN, Charset.forName(Config.UTF_8)));
			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderHTTPException
	 */
	public String getMessages(String userId, String secureToken) throws JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpGet httpGet = new HttpGet(getJoinedServerUrl() + JOConfig.FF_MESSAGE + "/" + userId);
			httpGet.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
			else
			{
				return getJsonStringFromResponse(httpResponse);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
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
	 * @throws JOFriendFinderServerException
	 * @throws JOFriendFinderUnexpectedException
	 * @throws JOFriendFinderHTTPException
	 */
	public void deleteMessages(String userId, String secureToken) throws JOFriendFinderServerException, JOFriendFinderUnexpectedException, JOFriendFinderHTTPException
	{
		try
		{
			HttpClient httpClient = getNewHttpClient();
			HttpDelete httpDelete = new HttpDelete(getJoinedServerUrl() + JOConfig.FF_MESSAGE + "/" + userId);
			httpDelete.addHeader(getAuthenticationHeader(userId, secureToken));
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			{
				throw new JOFriendFinderServerException();
			}
			else if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT)
			{
				throw new JOFriendFinderUnexpectedException("HTTP Error Code " + httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (UnsupportedEncodingException e)
		{
			throw new JOFriendFinderUnexpectedException(e);
		}
		catch (ClientProtocolException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
		catch (IOException e)
		{
			throw new JOFriendFinderHTTPException(e);
		}
	}

	/**
	 * @param userId
	 * @param secureToken
	 * @return
	 */
	private Header getAuthenticationHeader(String userId, String secureToken)
	{
		return BasicScheme.authenticate(new UsernamePasswordCredentials(userId, secureToken), JOConfig.UTF_8, false);
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
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), JOConfig.UTF_8));
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
		md = MessageDigest.getInstance(JOConfig.SHA_1);
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes(JOConfig.ISO_8859), 0, text.length());
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
