/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.server;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.jrcs.diff.Revision;

import com.caucho.hessian.server.HessianServlet;
import com.kylecordes.sd04.api.Appointment;
import com.kylecordes.sd04.api.ApptService;
import com.kylecordes.sd04.api.ChangeSet;
import com.kylecordes.sd04.api.InitialDataSet;
import com.kylecordes.sd04.util.DiffUtil;
import com.kylecordes.sd04.util.MessageDigestUtil;

public class ServerImpl extends HessianServlet implements ApptService {

	private static Map storedStates = Collections
			.synchronizedMap(new HashMap());

	public List getAll() {
		System.out.println("Getting all data");
		try {
			Connection con = getConnection();
			try {
				return readAppointmentFromQuery(
						con,
						"select appt_id, worker, site, status, appt_date, mod_date from appointment order by appt_date,site");
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	public List getChangesSince(java.util.Date last) {
		System.out.println("Getting changes since " + sdf.format(last));
		try {
			Connection con = getConnection();
			try {
				return readAppointmentFromQuery(
						con,
						"select appt_id, worker, site, status, appt_date, mod_date from appointment where mod_date>'"
								+ sdf.format(last)
								+ "'  order by appt_date,site");
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		String url = "jdbc:mysql://localhost/appt";
		String userName = "sdexpo";
		String password = "sdexpo";
		return DriverManager.getConnection(url, userName, password);
	}

	private int executeUpdate(Connection con, String sql) throws SQLException {
		Statement stmt = con.createStatement();
		try {
			stmt.execute(sql);
			return stmt.getUpdateCount();
		} finally {
			stmt.close();
		}
	}

	private List readAppointmentFromQuery(Connection con, String sql)
			throws SQLException {
		Statement stmt = con.createStatement();
		try {
			ResultSet rs = stmt.executeQuery(sql);
			try {
				return readAppointmentsFromResultSet(rs);
			} finally {
				rs.close();
			}
		} finally {
			stmt.close();
		}
	}

	private List readAppointmentsFromResultSet(ResultSet rs)
			throws SQLException {
		List appts = new ArrayList();
		while (rs.next()) {
			appts.add(readAppointmentFromResultSet(rs));
		}
		System.out.println("Returning " + appts.size() + " results");
		return appts;
	}

	private Appointment readAppointmentFromResultSet(ResultSet rs)
			throws SQLException {
		Appointment a = new Appointment();
		a.setId(rs.getInt(1));
		a.setWorker(rs.getString(2));
		a.setSite(rs.getString(3));
		a.setStatus(rs.getString(4));
		a.setApptDate(rs.getDate(5));
		Timestamp ts = rs.getTimestamp(6);
		a.setModifiedDate(new Date(ts.getTime()));

		if (rs.wasNull()) {
			throw new RuntimeException("mod_date field unexpectedly NULL");
		}
		return a;
	}

	public void saveAppointment(Appointment appointment) {
		try {
			Connection con = getConnection();
			try {
				// Use a bit of MySQL nonstandard SQL
				String sql = "REPLACE INTO appointment (appt_id, worker, site, status, appt_date, mod_date) values (?, ?, ?, ?, ?, now())";
				PreparedStatement stmt = con.prepareStatement(sql);
				try {
					stmt.setInt(1, appointment.getId());
					stmt.setString(2, appointment.getWorker());
					stmt.setString(3, appointment.getSite());
					stmt.setString(4, appointment.getStatus());
					stmt.setDate(5, new java.sql.Date(appointment.getApptDate()
							.getTime()));
					stmt.execute();

					// Only actually does something if the server is running in
					// JMS mode
					ServerMain.publishChange(appointment);
				} finally {
					stmt.close();
				}
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void saveMany(List saveCommands) {
		for(Iterator iter = saveCommands.iterator(); iter.hasNext();) { 
			Appointment appt = (Appointment) iter.next();
			saveAppointment(appt);
		}
	}

	public String getAllAsXml() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLEncoder e = new XMLEncoder(os);
		e.setPersistenceDelegate(Date.class, new DatePersistenceDelegate());
		e.writeObject(getAll());
		e.close();
		return os.toString();
	}

	public InitialDataSet getXmlAll() {
		String xmlPacket = getAllAsXml();
		String hash = MessageDigestUtil.calcStringHash(xmlPacket);

		storedStates.put(hash, xmlPacket);

		return new InitialDataSet(xmlPacket, hash);
	}

	public ChangeSet getXmlDiffSince(String prevHash) {
		String beforeXml = (String) storedStates.get(prevHash);

		String afterXml = getAllAsXml();
		String afterHash = MessageDigestUtil.calcStringHash(afterXml);
		storedStates.put(afterHash, afterXml);

		Revision diff = DiffUtil.calculateLineDiff(beforeXml, afterXml);
		return new ChangeSet(prevHash, diff, afterHash);
	}

	public void markWorkDone(int appointmentId) {
		try {
			Connection con = getConnection();
			try {
				String sql = "UPDATE appointment SET status='DONE' where appt_id = ?";
				PreparedStatement stmt = con.prepareStatement(sql);
				try {
					stmt.setInt(1, appointmentId);
					stmt.execute();
				} finally {
					stmt.close();
				}
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
