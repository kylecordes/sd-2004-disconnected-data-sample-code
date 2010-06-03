/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.util;

import junit.framework.TestCase;

import org.apache.commons.jrcs.diff.Revision;

public class DiffUtilTest extends TestCase {

	public void testStringListConversion() {
		String s1 = "This\nThat\nTheOther\n";
		assertEquals(s1, DiffUtil.listToString(DiffUtil.stringToList(s1)));
	}
	
	public void testDiffPatch() {
		String s1 = "This\nThat\nTheOther\n";
		String s2 = "This\nJoeBlow\nThat\n";
		
		Revision r = DiffUtil.calculateLineDiff(s1, s2);
		
		System.out.println("By the way, the diff is:");
		System.out.println(r.toRCSString());
		
		String calculated = DiffUtil.applyLineDiff(s1, r);
		
		assertEquals(s2, calculated);
	}
}
