/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JPage
 *    Andrew Hurle
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.dummyserver.DummyServer;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class TestRequest {
	class MockObserver implements RequestObserver {

		public MockObserver() {
			super();
		}

		@Override
		public void responseSuccess(IRequest iReq) {
			synchronized (this) {
				notifyAll();
			}
		}

		@Override
		public void responseError(IRequest iReq) {
			// TODO Auto-generated method stub

		}

		@Override
		public void fail(IRequest iReq, Exception exception) {
			// TODO Auto-generated method stub

		}
	}

	private static int port = 38512;

	private NetworkConfiguration config = new NetworkConfiguration(
			"http://localhost:" + port);
	private NetworkConfiguration configMalformed = new NetworkConfiguration(
			"Not Correct");

	/**
	 * Test that a NullPointerException is thrown when a null
	 * networkConfiguration is passed to the Request constructor.
	 * 
	 * @throws MalformedURLException
	 */
	@Test(expected = NullPointerException.class)
	public void testRequestConstructorNullPointerException()
			throws MalformedURLException {
		new Request(null, null, null);
	}

	/**
	 * Test that a NullPointerException is thrown when a null requestMethod is
	 * passed to the Request#setRequestMethod.
	 * 
	 * @throws MalformedURLException
	 */
	@Test(expected = NullPointerException.class)
	public void testRequestSetRequestMethodNullPointerException()
			throws MalformedURLException {
		new Request(config, null, null);
	}

	/**
	 * Test that a RuntimeException is thrown when a malformed URL is provided
	 */
	@Test(expected = RuntimeException.class)
	public void testMalformedURLExceptionOnConstructor()
			throws MalformedURLException {
		Request r = new Request(configMalformed, null, HttpMethod.POST);
	}

	/**
	 * Test that a NullPointerException is thrown when a null body is passed to
	 * the Request#setRequestBody.
	 */
	@Test(expected = NullPointerException.class)
	public void testRequestSetRequestBodyNullPointerException() {
		Request r = new Request(config, null, HttpMethod.POST);
		r.setBody(null);
	}

	/**
	 * Test adding Header to the Request
	 */
	@Test
	public void testAddHeader() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = false;
		assertEquals(r.getHeaders().size(), 0);
		r.addHeader("key", "value");
		assertEquals(r.getHeaders().size(), 1);
	}

	/**
	 * Test the addHeader method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testAddHeaderIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.addHeader("key", "value");
	}

	/**
	 * Test adding query data to the Request
	 */
	@Test
	public void testAddQueryData() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = false;
		assertEquals(r.getHeaders().size(), 0);
		r.addQueryData("key", "value");
		assertEquals(r.getHeaders().size(), 1);
	}

	/**
	 * Test the addQueryData method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testAddQueryDataIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.addQueryData("key", "value");
	}

	/**
	 * Test the setAsynchronous and clearAsynchronous methods for an
	 * IllegalStateException.
	 */
	@Test
	public void testSetClearAsynchronous() {
		Request r = new Request(config, null, HttpMethod.GET);
		assertTrue(r.isAsynchronous()); // should be asynchronous by default
		r.clearAsynchronous();
		assertFalse(r.isAsynchronous());
		r.setAsynchronous();
		assertTrue(r.isAsynchronous());
	}

	/**
	 * Test the clearAsynchronous method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testClearAsynchronousIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.clearAsynchronous();
	}

	/**
	 * Test the setAsynchronous method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testSetAsynchronousIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.setAsynchronous();
	}

	/**
	 * Test setting connectTimeout when running is false
	 */
	@Test
	public void testSetConnectTimeout() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = false;
		r.setConnectTimeout(1000);
		assertEquals(1000, r.connectTimeout);
	}

	/**
	 * Test the setConnectTimeout method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testSetConnectTimeoutIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.setConnectTimeout(1000);
	}

	/**
	 * Test setting readTimeout when running is false
	 */
	@Test
	public void testSetReadTimeout() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = false;
		r.setReadTimeout(1000);
		assertEquals(1000, r.getReadTimeout());
	}

	/**
	 * Test the setReadTimeout method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testSetReadTimeoutIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.setReadTimeout(1000);
	}

	/**
	 * Test the setBody method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testSetBodyIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.setBody("");
	}

	/**
	 * Test the setHttpMethod method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testSetHttpMethodIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.setHttpMethod(HttpMethod.GET);
	}

	/**
	 * Test the setResponse method for an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void testSetResponseIllegalStateException() {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.setResponse(new ResponseModel());
	}

	/**
	 * Test that communication works.
	 */
	@Test
	public void testRequestCommunication() {
		// Make a new instance of the MyRequestObserver class.
		final MockObserver requestObserver = new MockObserver();

		// The request body
		String body = "This is the request body!";

		// Create the URL
		try {
			DummyServer server = new DummyServer(port);
			server.start();

			// Make a new POST Request.
			Request manualRequest = new Request(config, null, HttpMethod.POST); // construct
																				// the
																				// Request

			// Configure the request
			manualRequest.setBody(body); // set the request body to send to the
											// server
			manualRequest.addObserver(requestObserver); // Add the
														// requestObserver to
														// the request's set of
														// Observers

			// Send the request!
			manualRequest.send();
			synchronized (requestObserver) {
				requestObserver.wait(2000);
			}

			// assertEquals(true,
			// (body+"\n").equals(manualRequest.getResponse().getBody()));
			assertEquals(200, manualRequest.getResponse().getStatusCode());
			assertEquals(true, "OK".equalsIgnoreCase(manualRequest
					.getResponse().getStatusMessage()));

			assertTrue(body.equals(server.getLastReceived().getBody()
					.substring(2)));
			assertEquals(manualRequest.getHttpMethod(), server
					.getLastReceived().getHttpMethod());

			server.stop();
		} // TODO switch to https
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test to ensure that the response body can be read when a status code of
	 * 400 or above is received.
	 */
	@Test
	public void testRequestResponseCode400Plus() {
		// The response body
		String body = "Some error";

		DummyServer server = new DummyServer(port);

		// Construct and configure a canned response for the server to return
		ResponseModel cannedResponse = new ResponseModel();
		cannedResponse.setStatusCode(400); // status code: 400
		cannedResponse.setBody(body); // body: "Some error"

		// Make a new POST Request.
		Request manualRequest = new Request(config, null, HttpMethod.POST); // construct
																			// the
																			// Request
		manualRequest.setBody(body); // set the request body to send to the
										// server
		manualRequest.clearAsynchronous(); // make this request synchronously so
											// that the test is blocked

		// set the canned response and start the server
		server.setResponse(cannedResponse);
		server.start();

		// Send the request!
		manualRequest.send();

		assertTrue(manualRequest.getResponse().getBody().contains(body)); // check
																			// that
																			// the
																			// message
																			// in
																			// the
																			// body
																			// was
																			// read
		assertEquals(400, manualRequest.getResponse().getStatusCode());

		assertTrue(body.equals(server.getLastReceived().getBody().substring(2)));
		assertEquals(manualRequest.getHttpMethod(), server.getLastReceived()
				.getHttpMethod());

		server.stop();
	}

	/**
	 * Test the notifyObserversResponseError function for IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void testNotifyObserversResponseErrorIllegalStateException()
			throws IllegalStateException {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.notifyObserversResponseError();
	}

	/**
	 * Test the notifyObserversFail function for IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void testNotifyObserversFailIllegalStateException()
			throws IllegalStateException {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		Exception e = new Exception();
		r.notifyObserversFail(e);
	}

	/**
	 * Test the notifyObserversResponseSuccess function for
	 * IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void testNotifyObserversResponseSuccessIllegalStateException()
			throws IllegalStateException {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.notifyObserversResponseSuccess();
	}

	/**
	 * Test the addObserver function for IllegalStateException
	 */
	@Test(expected = IllegalStateException.class)
	public void testaddObserverIllegalStateException()
			throws IllegalStateException {
		Request r = new Request(config, null, HttpMethod.GET);
		r.running = true;
		r.addObserver(new MockObserver());
	}

	/**
	 * Test observer count in countObservers
	 */
	@Test
	public void testCountObservers() {
		Request r = new Request(config, null, HttpMethod.GET);
		MockObserver obs1 = new MockObserver();
		MockObserver obs2 = new MockObserver();
		r.addObserver(obs1);
		r.addObserver(obs2);
		assertEquals(2, r.countObservers());

	}

}