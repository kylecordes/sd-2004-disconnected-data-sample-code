/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.beans.EventHandler;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class ClientScreen implements Displayer {

	private Connector connector;
	private String userName;
	private JFrame frame;
	private JTextArea logArea;

	public ClientScreen(String _userName, Connector _connector) {
		userName = _userName;
		connector = _connector;
		connector.setDisplayer(this);  // hook up ability to show log

		// Tedious code to configure Swing objects:
		JTable table = new JTable(connector.getTableModel());
		table.setPreferredScrollableViewportSize(new Dimension(450, 270));
		TableColumn sportColumn = table.getColumnModel().getColumn(3);
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("TODO");
		comboBox.addItem("DONE");
		comboBox.addItem("CANCELLED");
		sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
		JScrollPane tableScrollPane = new JScrollPane(table);

		JButton syncButton = new JButton("Sync Changes");
		syncButton.addActionListener((ActionListener) EventHandler.create(
				ActionListener.class, this, "sync"));

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(syncButton);

		logArea = new JTextArea();
		JScrollPane logPane = new JScrollPane(logArea);
		logArea.setFont(new Font("Courier New", Font.PLAIN,14));
		logPane.setPreferredSize(new Dimension(450, 270));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(buttonPanel);
		mainPanel.add(tableScrollPane);
		mainPanel.add(logPane);
		mainPanel.setOpaque(true);
		frame = new JFrame("Demo App - Logged In As " + userName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(mainPanel);
		frame.pack();
	}

	public void go() {
		frame.setVisible(true);

		// For this example, I didn't bother to save the data locally between
		// invocations, so this loads it all from the server:
		try {
			connector.fetchInitialData();
		} catch (Exception e) {
			logText("ERROR" + e.toString());
			e.printStackTrace();
		}
	}

	public void sync() {
		try {
			// Send first,
			connector.sendChanges();

			// Then get, so our changes will have taken effect
			connector.fetchChanges();
		} catch (Exception e) {
			logText("ERROR" + e.toString());
			e.printStackTrace();
		}
	}

	public void logText(String s) {
		logArea.setText(logArea.getText() + s + "\n");
	}
}
