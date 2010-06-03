/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.client;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.kylecordes.sd04.api.ChangeSet;
import com.kylecordes.sd04.api.InitialDataSet;
import com.kylecordes.sd04.util.DiffUtil;
import com.kylecordes.sd04.util.MessageDigestUtil;

public class XmlDiffConnector extends Connector {

	private String xmlData;
	private String hash;

	private void setDataFromXml(String newXmlData, String newHash) {
		xmlData = newXmlData;
		hash = newHash;
		displayer.logText("Deserializing XML");
		// JDK 1.4 XML persistance decoding:
		InputStream is = new ByteArrayInputStream(xmlData.getBytes());
		XMLDecoder dec = new XMLDecoder(is);
		try {
			List result = (List) dec.readObject();
			// Starting with with no data, so grab it all
			updateData(result);
			displayer.logText("Done");
		} finally {
			dec.close();
		}
	}

	public void fetchInitialData() {
		displayer.logText("getting initial (all) data");

		InitialDataSet initialData = service.getXmlAll();

		// Remember the raw XML data and its hash
		String newXmlData = initialData.getXml();

		// see what the xml ser format looks like
		//saveToFile(newXmlData);
		
		displayer.logText("Hash = " + initialData.getAfterHash());

		setDataFromXml(newXmlData, initialData.getAfterHash());
	}

	private void saveToFile(String newXmlData) {
		try {
			// Save the XML to a file so we can look at it, this file is
			// not used by the software.
			FileWriter fw = new FileWriter("data.xml");
			fw.write(newXmlData);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fetchChanges() {
		displayer.logText("Getting change set");
		ChangeSet changes = service.getXmlDiffSince(hash);
		displayer.logText(changes.humanReadableForm());

		displayer.logText("Applying change set");
		String newXmlData = DiffUtil.applyLineDiff(xmlData, changes.getRevision());

		String expectedHash = changes.getAfterHash();
		displayer.logText("Expected 'After' Hash = " + expectedHash);

		String actualHash = MessageDigestUtil.calcStringHash(newXmlData);
		displayer.logText("Actual 'After' Hash = " + actualHash);

		if (!expectedHash.equals(actualHash)) {
			// could automatically refetch all the data
			throw new RuntimeException(
					"Hashes didn't match, corrupt data!");
		}

		setDataFromXml(newXmlData, actualHash);
	}
}
