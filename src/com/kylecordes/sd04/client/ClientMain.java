/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.client;

import java.net.MalformedURLException;
import com.caucho.hessian.client.HessianProxyFactory;
import com.kylecordes.sd04.api.ApptService;

public class ClientMain implements Runnable {
	private static String serverUrl = "http://localhost:8080/service1";

	public ApptService getServiceProxy() throws MalformedURLException {
		// Wire up the client to the server;
		// this wouldn't be hardcoded in a real app, of course:
		HessianProxyFactory factory = new HessianProxyFactory();
		return (ApptService) factory.create(ApptService.class, serverUrl);
	}

	public void run() {
		try {
			LoginScreen loginScreen = new LoginScreen(serverUrl);
			if (!loginScreen.showGui()) {
				// User did not log in
				System.exit(0);
			}
			
			Connector connector = null;
			
			if(loginScreen.getMode() == MethodConstants.DATE_MODIFIED) {
				connector = new DateModifiedConnector();
			}
			if(loginScreen.getMode() == MethodConstants.XML_DIFF) {
				System.out.println("using XML");
				connector = new XmlDiffConnector();
			}
			if(loginScreen.getMode() == MethodConstants.JMS_MOM) {
				connector = new JmsConnector("localhost", "appt", loginScreen.getUserName());
			}
			
			connector.setService(getServiceProxy());
			ClientScreen cs = new ClientScreen(loginScreen.getUserName(),
					connector);
			cs.go();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(new ClientMain());
	}
}
