/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.server;

import java.io.Serializable;

import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.util.MultiException;

import com.kylecordes.sd04.mom.Publisher;

public class ServerMain {

	private static void startJettytHttpServer() throws MultiException {
		// Set up Jetty HTTP server with our servlet; this accidental
		// complexity is annoying for a demo app, but much less so than the
		// steps in deploying this in to Tomcat or whatever
		HttpServer webServer = new HttpServer();
		SocketListener listener = new SocketListener();
		listener.setPort(8080);
		webServer.addListener(listener);

		ServletHandler sh = new ServletHandler();
		sh.addServlet("service1", "/service1", ServerImpl.class.getName());

		HttpContext context = new HttpContext(webServer, "/");
		context.addHandler(sh);

		webServer.start();
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 1 && args[0].equals("-jms")) {
			publisher = new Publisher("localhost", "appt");
		}

		ServerImpl si = new ServerImpl();
		startJettytHttpServer();
	}

	private static Publisher publisher;

	public static void publishChange(Serializable change) {
		if (publisher != null) {
			publisher.publish(change);
		}
	}
}
