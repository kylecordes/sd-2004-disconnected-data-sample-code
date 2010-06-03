/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.api;

public class InitialDataSet {

	private String xml;
	private String afterHash;
	
	public InitialDataSet(String _xml, String _afterhash) {
		xml = _xml;
		afterHash = _afterhash;
	}

	public String getAfterHash() {
		return afterHash;
	}

	public String getXml() {
		return xml;
	}
}
