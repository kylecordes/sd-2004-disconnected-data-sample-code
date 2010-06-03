/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.client;

/*
 * This "base" connector represents a local data store and can provide a
 * TableModel for GUI convenience. It doesn't have any actual sync logic.
 *  
 */

import java.util.*;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.kylecordes.sd04.api.Appointment;
import com.kylecordes.sd04.api.ApptService;

public abstract class Connector {

	private String[] columnNames = {"Appt Date", "Site", "Worker", "Status",
			"ID"};
	ArrayList data = new ArrayList();
	Displayer displayer;
	ApptService service;

	public void setService(ApptService _service) {
		service = _service;
	}

	public void setDisplayer(ClientScreen _displayer) {
		displayer = _displayer;
	}

	class ConnectorTableModel extends AbstractTableModel {
		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			Appointment a = (Appointment) data.get(row);
			switch (col) {
				case 0 :
					return a.getApptDate();
				case 1 :
					return a.getSite();
				case 2 :
					return a.getWorker();
				case 3 :
					return a.getStatus();
				case 4 :
					return "ID: " + a.getId();
				default :
					return "";
			}
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			return col == 3 || col == 2;
		}

		public void setValueAt(Object value, int row, int col) {
			Appointment a = (Appointment) data.get(row);
			switch (col) {
				case 2 :
					a.setWorker((String) value);
					break;
				case 3 :
					a.setStatus((String) value);
					break;
			}
			a.makeDirty();
			fireTableCellUpdated(row, col);
		}
	}

	void updateData(List newData) {
		for (Iterator iter = newData.iterator(); iter.hasNext();) {
			Appointment a = (Appointment) iter.next();
			updateOrAddAppointment(a);
		}
		sortAndRefresh();
	}

	void sortAndRefresh() {
		Collections.sort(data, new Comparator() {
			public int compare(Object left, Object right) {
				Appointment leftAppt = (Appointment) left;
				Appointment rightAppt = (Appointment) right;

				int result = 0;
				
				if (leftAppt.getApptDate() != null
						&& rightAppt.getApptDate() != null) {
					result = leftAppt.getApptDate().compareTo(
							rightAppt.getApptDate());
				}

				if (result == 0) {
					result = leftAppt.getSite().compareTo(rightAppt.getSite());
				}

				return result;
			}
		});
		myTableModel.fireTableDataChanged();
	}

	private ConnectorTableModel myTableModel = new ConnectorTableModel();

	public TableModel getTableModel() {
		return myTableModel;
	}

	void updateOrAddAppointment(Appointment a) {
		int index = 0;
		for (Iterator iter = data.iterator(); iter.hasNext();) {
			Appointment existing = (Appointment) iter.next();
			if (a.getId() == existing.getId()) {
				data.set(index, a);
				a.makeClean();
				return;
			}
			index++;
		}
		data.add(a);
	}

	/*
	 * The changes are send to the server in the same way for all of the
	 * approaches, so the implementation is here
	 *  
	 */

	public void sendChanges() {
		displayer.logText("Sending updates to server...");
		int n = 0;
		for (Iterator iter = data.iterator(); iter.hasNext();) {
			Appointment a = (Appointment) iter.next();
			if (a.isDirty()) {
				displayer.logText("Saving appointment " + a.getId());
				service.saveAppointment(a);
				n++;
				a.makeClean();
			}
		}
		displayer.logText("Updates sent: " + n);
	}

	public void sendChangesBoxcarred() {
		displayer.logText("Sending updates to server with Boxcarring...");
		List changesToSend = new ArrayList();
		for (Iterator iter = data.iterator(); iter.hasNext();) {
			Appointment a = (Appointment) iter.next();
			if (a.isDirty()) {
				changesToSend.add(a);
				a.makeClean();
			}
		}
		service.saveMany(changesToSend);
		displayer.logText("Updates sent: " + changesToSend.size());
	}

	public void sendChangesDomain() {
		// example code - call a service method at the app domain
		// level, rather than as "data"
		
		service.markWorkDone(45);
	}

	public abstract void fetchInitialData();

	public abstract void fetchChanges();

}
