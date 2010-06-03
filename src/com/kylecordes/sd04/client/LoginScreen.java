/* Example code for SD 2004 talk:
 * Disconnected Data Handling in Mobile / Wireless Applications
 * Copyright 2004 Kyle Cordes
 * Oasis Digital Solutions Inc.
 * http://kylecordes.com
 * http://oasisdigital.com
 */

package com.kylecordes.sd04.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class LoginScreen {
	private JTextField userNameField;
	private JTextField passwordField;
	private JLayeredPane layeredPane;
	private JComboBox syncMethodCombo;
	private JLabel failureLabel;
	private JDialog dialog;
	private static final int picHeight = 220;
	private static final int picWidth = 320;
	private static final String picUrl = "/images/loginscreen.gif";
	private boolean loggedIn = false;
	private String userName;
	private Object mode;
	private Integer topLayer = new Integer(10);
	
	private class OkAction extends AbstractAction {
		public OkAction() {
			super("Log In", null);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				login(userNameField.getText(), passwordField.getText());
			} catch (LoginFailureException lfe) {
				Toolkit.getDefaultToolkit().beep();
				showFailureBriefly();
				userNameField.requestFocus();
			}
		}
	}
	private OkAction okAction = new OkAction();

	private class PasswordFieldAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			passwordField.requestFocus();
		}
	}
	private class CancelAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	}

	private void login(String name, String pw) throws LoginFailureException {
		if (name.equals("")) throw new LoginFailureException();
		if (pw.equals("")) throw new LoginFailureException();
		// In a real application, we'd need to actual test these credential
		// against a server of some kind, but for this example, we'll just
		// assume that any username and password are OK.
		loggedIn = true;
		userName = userNameField.getText();
		mode = syncMethodCombo.getSelectedItem();
		dialog.dispose();
	}

	public LoginScreen(String connectionDescription) {
		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(picWidth, picHeight));
		layeredPane.setBorder(BorderFactory.createEmptyBorder());
		makeBackgroundImage();
		int left = 150;
		
		makeUserNameField(left);
		makePasswordField(left);
		makeMethodCombo(left);
		makeLoginButton(left);
		makeFailureLabel(left);
		layeredPane.setOpaque(true);

		dialog = new JDialog();
		dialog.setTitle("Demo App Login");
		dialog.setContentPane(layeredPane);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
	}

	private void makeBackgroundImage() {
		ImageIcon image = ImageUtil.createImageIcon(picUrl);
		JLabel picture = new JLabel(image);
		picture.setBounds(0, 0, picWidth, picHeight);
		layeredPane.add(picture);
	}

	private void makeFailureLabel(int left) {
		failureLabel = new JLabel(
				"<html><center><b>Login Failed, <br>please try again</b></center></html>");
		failureLabel.setVisible(false);
		failureLabel.setBounds(left, 175, 90, 40);
		layeredPane.add(failureLabel, topLayer);
	}

	private void makeLoginButton(int left) {
		JButton loginButton = new JButton();
		loginButton.setBounds(left, 175, 90, 28);
		layeredPane.add(loginButton, topLayer);
		loginButton.setAction(okAction);
	}

	private void makePasswordField(int left) {
		passwordField = new JPasswordField();
		passwordField.setBounds(left, 105, 100, 20);
		layeredPane.add(passwordField, topLayer);
	}

	private void makeUserNameField(int left) {
		userNameField = new JTextField(15);
		userNameField.setBounds(left, 69, 100, 20);
		layeredPane.add(userNameField, topLayer);
	}
	
	private void makeMethodCombo(int left) {
		syncMethodCombo = new JComboBox();
		syncMethodCombo.setBounds(left, (105-69)*2 + 69, 160, 20);
		syncMethodCombo.addItem(MethodConstants.DATE_MODIFIED);
		syncMethodCombo.addItem(MethodConstants.XML_DIFF);
		syncMethodCombo.addItem(MethodConstants.JMS_MOM);
		layeredPane.add(syncMethodCombo, topLayer);
	}

	public boolean showGui() {
		dialog.show();
		return loggedIn;
	}

	private void showFailureBriefly() {
		failureLabel.setVisible(true);
		final Runnable hider = new Runnable() {
			public void run() {
				failureLabel.setVisible(false);
			}
		};
		final Runnable waitor = new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
					SwingUtilities.invokeLater(hider);
				} catch (InterruptedException e) {
				}
			}
		};
		new Thread(waitor).start();
	}

	public String getUserName() {
		return userName;
	}
	
	public Object getMode() {
		return mode;
	}
}
