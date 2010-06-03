/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.api;

import java.util.Date;
import java.util.List;

public interface ApptService {
	
	public List getAll();
	public List getChangesSince(Date last);

	public ChangeSet getXmlDiffSince(String prevVersionId);
	public InitialDataSet getXmlAll();
	
	public void saveAppointment(Appointment appointment);
	
	public void saveMany(List saveCommands);
	
	public void markWorkDone(int appointmentId);
}
