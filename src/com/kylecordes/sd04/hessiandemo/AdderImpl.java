/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.hessiandemo;

import com.caucho.hessian.server.HessianServlet;

public class AdderImpl extends HessianServlet implements Adder {

	public int add(int a, int b) {
		return a + b;
	}
}
