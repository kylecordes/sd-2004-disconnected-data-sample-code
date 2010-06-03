/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.api;

import org.apache.commons.jrcs.diff.Revision;

public class ChangeSet {

	private String beforeHash;
	private Revision revision;
	private String afterHash;
	
	public ChangeSet(String _beforeHash, Revision _revision, String _afterHash) {
		beforeHash = _beforeHash;
		revision = _revision;
		afterHash = _afterHash;
	}
	
	public String getAfterHash() {
		return afterHash;
	}

	public String getBeforeHash() {
		return beforeHash;
	}
	
	public Revision getRevision() {
		return revision;
	}

	public String humanReadableForm() {
		return "Revision data:\n" + getRevision().toRCSString();
	}
}
