/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.mom;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsBase {

	private String host;
	private Connection connection;
	Session session;
	Topic topic;

	void start(String _host, String topicName) throws NamingException, JMSException {
		if (connection != null) {
			throw new RuntimeException("Already started...");
		}
		
		host = _host;
	
		Context context = getContext();
		try {
			ConnectionFactory factory = (ConnectionFactory) context
					.lookup("connectionFactory");

			connection = factory.createConnection();
			connection.start();

			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

			topic = session.createTopic(topicName);
		} finally {
			context.close();
		}
	}

	public void stop() {
		try {
			connection.close();
			connection = null;
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	private Context getContext() throws NamingException {
		Hashtable properties = new Hashtable();
		String url = "ubermq://" + host;
		
		// Replace this with whatever config is needed for 
		// any JMS server you have handy:
		properties.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.ubermq.jms.client.JMSInitialContextFactory");
		properties.put(Context.PROVIDER_URL, url);
		return new InitialContext(properties);
	}
}
