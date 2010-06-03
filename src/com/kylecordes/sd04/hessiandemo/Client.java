/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.hessiandemo;

import com.caucho.hessian.client.HessianProxyFactory;

public class Client {
	public static void main(String[] args) throws Exception {
		
		String url = "http://localhost:8080/foo";

		HessianProxyFactory factory = new HessianProxyFactory();
		Adder a = (Adder) factory.create(Adder.class, url);

		System.out.println("hello(): " + a.add(3,3));
	}
}
