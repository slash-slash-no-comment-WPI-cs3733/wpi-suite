/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.codec.binary.Base64;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Allows the tests to login
 *
 * @author Jon Sorrells
 */
public class TestLogin {

	private static boolean loggedIn = false;

	private static final String username = "admin";
	private static final String password = "password";
	private static final String url = "http://localhost:8080/WPISuite/API";
	private static final int maxLoginWait = 10; // in seconds

	/**
	 * login as admin on localhost, timeout of 5 seconds
	 *
	 */
	public static void login() {
		login(username, password, url, maxLoginWait);
	}

	/**
	 * login as admin on localhost
	 * 
	 * @param maxLoginWait
	 *            the timeout
	 */
	public static void login(int maxLoginWait) {
		login(username, password, url, maxLoginWait);
	}

	/**
	 * login on localhost, timeout of 5 seconds
	 * 
	 * @param username
	 *            the user to login as
	 * @param password
	 *            that user's password
	 */
	public static void login(String username, String password) {
		login(username, password, url, maxLoginWait);
	}

	/**
	 * timeout of 5 seconds
	 * 
	 * @param username
	 *            the user to login as
	 * @param password
	 *            that user's password
	 * @param url
	 *            the server to login to
	 */
	public static void login(String username, String password, String url) {
		login(username, password, url, maxLoginWait);
	}

	/**
	 * 
	 * @param username
	 *            the user to login as
	 * @param password
	 *            that user's password
	 * @param url
	 *            the server to login to
	 * @param maxLoginWait
	 *            the timeout
	 */
	public static void login(String username, String password, String url,
			int maxLoginWait) {
		sendLoginRequest(username, password, url);
		// wait to login
		for (int i = 0; i < maxLoginWait && !loggedIn; i++) {
			try {
				java.lang.Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!loggedIn) {
			fail("Unable to login.  Is your server running?");
		}
	}

	/**
	 * mostly copied from Janeway
	 *
	 */
	public static void sendLoginRequest(String username, String password,
			String url) {
		// Form the basic auth string
		String basicAuth = "Basic ";
		String credentials = username + ":" + password;
		basicAuth += Base64.encodeBase64String(credentials.getBytes());
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration(url));

		// Create and send the login request
		Request request = Network.getInstance().makeRequest("login",
				HttpMethod.POST);
		System.out.println(basicAuth);
		request.addHeader("Authorization", basicAuth);
		request.addObserver(new RequestObserver() {

			@Override
			public void responseSuccess(IRequest iReq) {
				loginSuccessful(iReq.getResponse());
			}

			@Override
			public void responseError(IRequest iReq) {
				org.junit.Assert.fail("failed to login");
			}

			@Override
			public void fail(IRequest iReq, Exception exception) {
				org.junit.Assert.fail("failed to login");
			}
		});
		request.send();
	}

	/**
	 * mostly copied from Janeway
	 *
	 */
	public static void loginSuccessful(ResponseModel response) {
		// Save the cookies
		List<String> cookieList = response.getHeaders().get("Set-Cookie");
		String cookieParts[];
		String cookieNameVal[];
		if (cookieList != null) { // if the server returned cookies
			for (String cookie : cookieList) { // for each returned cookie
				cookieParts = cookie.split(";"); // split the cookie
				if (cookieParts.length >= 1) { // if there is at least one part
												// to the cookie
					cookieNameVal = cookieParts[0].split("="); // split the
																// cookie into
																// its name and
																// value
					if (cookieNameVal.length == 2) { // if the split worked, add
														// the cookie to the
														// default
														// NetworkConfiguration
						NetworkConfiguration config = Network.getInstance()
								.getDefaultNetworkConfiguration();
						config.addCookie(cookieNameVal[0], cookieNameVal[1]);
						Network.getInstance().setDefaultNetworkConfiguration(
								config);
					} else {
						System.err.println("Received unparsable cookie: "
								+ cookie);
					}
				} else {
					System.err.println("Received unparsable cookie: " + cookie);
				}
			}

			System.out.println(Network.getInstance()
					.getDefaultNetworkConfiguration().getRequestHeaders()
					.get("cookie").get(0));

		} else {
			fail("failed to login");
		}
		loggedIn = true;
	}
}
