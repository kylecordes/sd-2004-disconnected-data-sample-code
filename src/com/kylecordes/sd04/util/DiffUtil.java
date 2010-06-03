/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jrcs.diff.Diff;
import org.apache.commons.jrcs.diff.DifferentiationFailedException;
import org.apache.commons.jrcs.diff.PatchFailedException;
import org.apache.commons.jrcs.diff.Revision;

public class DiffUtil {
	
	static String[] stringToStringArray(String data) {
		return data.split("\n");
	}

	static List stringToList(String data) {
		return new ArrayList(Arrays.asList(stringToStringArray(data)));
	}

	static String listToString(List orig) {
		StringBuffer buf = new StringBuffer();
		for(Iterator iter = orig.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			buf.append(item);
			buf.append("\n");
		}
		return buf.toString();
	}

	public static Revision calculateLineDiff(String beforeXml, String afterXml) {
		String[] before = DiffUtil.stringToStringArray(beforeXml);
		String[] after = DiffUtil.stringToStringArray(afterXml);

		try {
			return Diff.diff(before, after);
		} catch (DifferentiationFailedException e) {
			throw new RuntimeException(e);
		}
	}

	public static String applyLineDiff(String before, Revision rev) {
		List orig = stringToList(before);
		
		try {
			// update it with the diff from the server:
			rev.applyTo(orig);
		} catch (PatchFailedException e) {
			throw new RuntimeException(e);
		}

		return listToString(orig);
	}
}
