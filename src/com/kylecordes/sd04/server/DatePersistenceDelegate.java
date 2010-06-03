/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.server;

/*
 * This is used with the XMLEncoder to efficienlty and correctly
 * store Date instances
 */
import java.beans.Expression;
import java.beans.PersistenceDelegate;

class DatePersistenceDelegate extends PersistenceDelegate {
	protected Expression instantiate(Object oldInstance, java.beans.Encoder out) {
		java.sql.Date d = (java.sql.Date) oldInstance;
		return new Expression(oldInstance, d.getClass(), "new",
				new Object[]{new Long(d.getTime())});
	}
}