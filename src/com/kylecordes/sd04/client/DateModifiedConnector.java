/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.client;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.kylecordes.sd04.api.Appointment;

public class DateModifiedConnector extends Connector {

	private Date latestRecordDate() {
		// If we were using a local SQL data store, this would be even easier.
		Date d = null;
		for (Iterator iter = data.iterator(); iter.hasNext();) {
			Appointment a = (Appointment) iter.next();
			if (d == null) {
				d = a.getModifiedDate();
			} else if (a.getModifiedDate().compareTo(d) > 0) {
				d = a.getModifiedDate();
			}
		}
		return d;
	}

	public void fetchChanges() {
		Date changesSince = latestRecordDate();
		displayer.logText("getting changes from server since " + changesSince);
		List changes = service.getChangesSince(changesSince);
		displayer.logText("Changed items received: " + changes.size());
		updateData(changes);
		displayer.logText("Done");
	}

	public void fetchInitialData() {
		displayer.logText("getting initial (all) data");
		// Starting with with no data, so grab it all
		updateData(service.getAll());
	}
}
