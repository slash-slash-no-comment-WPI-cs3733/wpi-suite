/*******************************************************************************
 * Copyright (c) 2012 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mpdelladonna
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;

/**
 * Primary servlet for the WPISuite service
 * 
 * This class handles incoming API requests over HTTP
 * 
 * @author Mike Della Donna
 *
 */
public class WPICoreServlet extends HttpServlet {

	private static final long serialVersionUID = -7156601241025735047L;
	private ErrorResponseFormatter reponseFormatter;
	private Object updateNotifier = new Object();
	private Cookie[] lastCookies = null;

	/**
	 * Empty Constructor
	 */
	public WPICoreServlet() {
		this.reponseFormatter = new JsonErrorResponseFormatter();
	}

	/**
	 * Forwards get requests and restful parameters to the ManagerLayer
	 * singleton
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// res.setCharacterEncoding("UTF8");
		// PrintWriter out = res.getWriter();
		// ServletOutputStream out = res.getOutputStream();
		String delims = "[/]+";
		String[] path = req.getPathInfo().split(delims);

		System.arraycopy(path, 1, path, 0, path.length - 1);
		path[path.length - 1] = null;

		if (req.getHeader("long-polling") != null) {
			handlePolling(req, res, path);
			return;
		}
		// ServletOutputStream out = res.getOutputStream();
		DataOutputStream out = new DataOutputStream(res.getOutputStream());
		try {

			byte[] a = new byte[3];
			a[0] = (byte) 227;
			a[1] = (byte) 129;
			a[2] = (byte) 130;

			String s = ManagerLayer.getInstance().read(path, req.getCookies());

			out.writeUTF(s);
			out.flush();

			// char[] temp = s.toCharArray();
			// byte[] bytes = new byte[temp.length];
			// for (int i = 0; i < temp.length; i++) {
			// bytes[i] = (byte) temp[i];
			// }
			//
			// out.write(bytes);
			// out.flush();
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
		}

		out.close();
	}

	/**
	 * Handles long-polling requests. Waits until there are changes to respond
	 * to clients
	 *
	 * @param req
	 *            The request from the client
	 * @param res
	 *            The response for the client
	 * @param path
	 *            The path the client wants changes for
	 * @throws IOException
	 *             If the print writer throws an IOException
	 */
	private void handlePolling(HttpServletRequest req, HttpServletResponse res,
			String[] path) throws IOException {

		int timeout = Integer.parseInt(req.getHeader("long-polling"));
		PrintWriter out = res.getWriter();
		System.out.println("waiting on request: " + req.toString());

		synchronized (updateNotifier) {
			try {
				// wait for changes
				long start = System.currentTimeMillis();
				updateNotifier.wait(timeout);

				// keep waiting if the last changes are from us and the timeout
				// hasn't happened yet
				while (ManagerLayer.getInstance().sameSession(lastCookies,
						req.getCookies())
						&& start - System.currentTimeMillis() < timeout) {
					updateNotifier.wait(timeout
							- (start - System.currentTimeMillis()));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("sending changes to request: " + req.toString());
		}

		// TODO: send the same object we received from the client, instead of
		// doing a database read
		try {
			out.println(ManagerLayer.getInstance().read(path, req.getCookies()));
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
			out.write(this.reponseFormatter.formatContent(e));
			out.flush();
			out.close();
			return;
		}

		// send changes
		out.close();
	}

	/**
	 * Forwards put requests and restful parameters to the ManagerLayer
	 * singleton
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		BufferedReader in = req.getReader();
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
		String[] path = req.getPathInfo().split(delims);

		System.arraycopy(path, 1, path, 0, path.length - 1);
		path[path.length - 1] = null;

		try {
			out.println(ManagerLayer.getInstance().create(path, in.readLine(),
					req.getCookies()));
			// notify if something was written to the database
			synchronized (updateNotifier) {
				lastCookies = req.getCookies();
				updateNotifier.notifyAll();
			}
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
			out.write(this.reponseFormatter.formatContent(e));
			out.flush();
			out.close();
			return;
		}
		res.setStatus(HttpServletResponse.SC_CREATED);
		out.close();

	}

	/**
	 * Forwards post requests and restful parameters to the ManagerLayer
	 * singleton
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// BufferedReader in = req.getReader();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				req.getInputStream(), "UTF8"));
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
		String[] path = req.getPathInfo().split(delims);

		System.arraycopy(path, 1, path, 0, path.length - 1);
		path[path.length - 1] = null;

		try {
			out.println(ManagerLayer.getInstance().update(path, in.readLine(),
					req.getCookies()));
			// notify if something was written to the database
			synchronized (updateNotifier) {
				lastCookies = req.getCookies();
				updateNotifier.notifyAll();
			}
			System.out.println();
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
			out.write(this.reponseFormatter.formatContent(e));
			out.flush();
			out.close();
		}

		out.close();
	}

	/**
	 * Forwards delete requests and restful parameters to the ManagerLayer
	 * singleton
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		String delims = "[/]+";
		String[] path = req.getPathInfo().split(delims);

		System.arraycopy(path, 1, path, 0, path.length - 1);
		path[path.length - 1] = null;

		try {
			out.println(ManagerLayer.getInstance().delete(path,
					req.getCookies()));
			// notify if something was written to the database
			synchronized (updateNotifier) {
				lastCookies = req.getCookies();
				updateNotifier.notifyAll();
			}
		} catch (WPISuiteException e) {
			res.setStatus(e.getStatus());
			out.write(this.reponseFormatter.formatContent(e));
			out.flush();
			out.close();
		}

		out.close();
	}
}