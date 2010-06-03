/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.mom;

import java.io.Serializable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;

public class Publisher extends JmsBase {

	private MessageProducer producer;

	public Publisher(String host, String topicName) {
		try {
			start(host, topicName);
			producer = session.createProducer(topic);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void publish(Serializable message) {
		try {
			ObjectMessage jmsMessage = session.createObjectMessage(message);
			producer.send(topic, jmsMessage, DeliveryMode.PERSISTENT, 5, 0);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}