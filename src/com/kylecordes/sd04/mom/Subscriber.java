/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.mom;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

public class Subscriber extends JmsBase {

	private MessageConsumer consumer;

	public Subscriber(String host, String topicName, String clientId) {
		try {
			start(host, topicName);
			consumer = session.createDurableSubscriber(topic, clientId);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Serializable getAvailableMessage() {
		try {
			Message mes = consumer.receiveNoWait();
			if(mes == null) {
				return null;
			}
			mes.acknowledge();
			return ((ObjectMessage) mes).getObject();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
