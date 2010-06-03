/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.client;

import com.kylecordes.sd04.api.Appointment;
import com.kylecordes.sd04.mom.Subscriber;

public class JmsConnector extends Connector {

	String host;
	String topicName;
	String clientId;

	public JmsConnector(String _host, String _topicName, String _clientId) {
		host = _host;
		topicName = _topicName;
		clientId = _clientId;
	}

	public void fetchChanges() {
		displayer.logText("Connecting to JMS server");
		Subscriber sub = new Subscriber(host, topicName, clientId);
		try {
			Appointment appt;
			while (true) {
				Object mes = sub.getAvailableMessage();
				if(mes == null) {
					break;
				}
				
				appt = (Appointment) mes; 
				displayer.logText("Processing update");
				updateOrAddAppointment(appt);
			}

		} finally {
			displayer.logText("Disconnecting from JMS server");
			sub.stop();
		}
		sortAndRefresh();
	}

	public void fetchInitialData() {
		displayer.logText("getting initial (all) data");
		// Starting with with no data, so grab it all, not using JMS
		updateData(service.getAll());
	}
}
