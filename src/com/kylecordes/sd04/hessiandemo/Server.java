/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.hessiandemo;

import org.mortbay.http.*;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.util.MultiException;

public class Server {
	
	private static void startHttpServer() throws MultiException {
		HttpServer hServer = new HttpServer();
		SocketListener listener = new SocketListener();
		listener.setPort(8080);
		hServer.addListener(listener);

		HttpContext context = new HttpContext();
		context.setContextPath("/");

		ServletHandler sh = new ServletHandler();
		String handlerName = AdderImpl.class.getName();
		sh.addServlet("name1", "/foo", handlerName);
		
		context.addHandler(sh);
		hServer.addContext(context);

		hServer.start();
	}
	
	public static void main(String[] args) throws Exception {
		AdderImpl a = new AdderImpl();
		startHttpServer();
	}
}
