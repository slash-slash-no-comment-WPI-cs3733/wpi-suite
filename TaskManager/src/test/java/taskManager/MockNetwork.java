/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Mock Network object for testing
 *
 * @author Sam Khalandovsky
 * @version Dec 6, 2014
 */
public class MockNetwork extends Network {
	/**
	 * Method makeRequest.
	 * 
	 * @param path
	 *            String
	 * @param requestMethod
	 *            HttpMethod
	 * 
	 * @return MockRequest
	 */
	@Override
	public Request makeRequest(String path, HttpMethod requestMethod) {
		if (requestMethod == null) {
			throw new NullPointerException("requestMethod may not be null");
		}

		if (defaultNetworkConfiguration == null) {
			defaultNetworkConfiguration = new NetworkConfiguration(
					"http://wpisuitetng");
		}

		return new MockRequest(defaultNetworkConfiguration, path, requestMethod);
	}
}
