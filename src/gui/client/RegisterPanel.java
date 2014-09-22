package gui.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import engine.ClientConnection;

@SuppressWarnings("serial")
public class RegisterPanel extends JPanel{
	
	private ClientInterface mainFrame;
	
	public RegisterPanel(ClientInterface clientInterface) {
		mainFrame = clientInterface;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
				
		JLabel registerLabel = new JLabel("Register");
		registerLabel.setFont(new Font("Consolas", Font.PLAIN, 26));
		
		JLabel confirmLabel = new JLabel("Confirm password");

		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setPreferredSize(confirmLabel.getPreferredSize());
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setPreferredSize(confirmLabel.getPreferredSize());
		
		final JTextField username = new JTextField(20);
		
		final JPasswordField password = new JPasswordField(20);
		password.setEchoChar((char) 8226);
		
		final JPasswordField confirm = new JPasswordField(20);
		confirm.setEchoChar((char) 8226);
		
		JButton register = new JButton("Register");
		register.setFocusable(false);
		register.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					String pass = new String(password.getPassword());
					String con = new String(confirm.getPassword());
					if (!username.getText().matches("[a-zA-Z0-9]+") || !pass.matches("[a-zA-Z0-9]+")) {
						mainFrame.messageDialog.showMessage("Invalid username/password\nOnly letters and digits are allowed!", true);
						return;
					}
					if (!(pass.length() > 5)) {
						mainFrame.messageDialog.showMessage("Password must be at least 6 characters!", true);
						return;
					}
					if (!pass.equals(con)) {
						mainFrame.messageDialog.showMessage("Passwords do not match!", true);
						return;
					}
					mainFrame.connection = new ClientConnection();
					if (!mainFrame.connection.register(username.getText(), pass)) {
						mainFrame.messageDialog.showMessage("Username is already taken!", true);
					} else {
						mainFrame.messageDialog.showMessage("You have been successfully registered!", false);
						mainFrame.switchToLoginPanel();
					}
				} catch (Exception ex) {
					mainFrame.messageDialog.showMessage("Couldn't connect to server!", true);
				}
			}
			
		});
		
		JButton back = new JButton("Back");
		back.setFocusable(false);
		back.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.switchToLoginPanel();
			}
			
		});
		
		JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		usernamePanel.add(usernameLabel);
		usernamePanel.add(username);
		usernamePanel.setOpaque(false);
		
		JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		passwordPanel.add(passwordLabel);
		passwordPanel.add(password);
		passwordPanel.setOpaque(false);
		
		JPanel confirmPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		confirmPasswordPanel.add(confirmLabel);
		confirmPasswordPanel.add(confirm);
		confirmPasswordPanel.setOpaque(false);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonsPanel.add(register);
		buttonsPanel.add(back);
		buttonsPanel.setOpaque(false);
		
		JPanel registerPanel = new JPanel(new GridLayout(5, 1, 0, 5));
		registerPanel.add(registerLabel);
		registerPanel.add(usernamePanel);
		registerPanel.add(passwordPanel);
		registerPanel.add(confirmPasswordPanel);
		registerPanel.add(buttonsPanel);
		registerPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(191, 230, 249).darker()), BorderFactory.createEmptyBorder(10, 10, 10, 10)));	
		registerPanel.setOpaque(false);

		add(registerPanel);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(mainFrame.windowSize, mainFrame.windowSize);
	}
	
}
