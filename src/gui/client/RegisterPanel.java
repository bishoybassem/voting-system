package gui.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import engine.ClientConnection;

@SuppressWarnings("serial")
public class RegisterPanel extends JPanel{
	
	private ClientInterface mainFrame;
	private JTextField username;
	private JTextField serverIP;
	private JPasswordField password;
	private JPasswordField confirm;
	
	public RegisterPanel(ClientInterface clientInterface, JTextField name, JTextField ip) {
		mainFrame = clientInterface;
		this.username = name;
		this.serverIP = ip;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
				
		JLabel registerLabel = new JLabel("Register");
		registerLabel.setFont(new Font("Consolas", Font.PLAIN, 26));
		
		JLabel confirmLabel = new JLabel("Confirm password");

		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setPreferredSize(confirmLabel.getPreferredSize());
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setPreferredSize(confirmLabel.getPreferredSize());
		
		JLabel serverIPLabel = new JLabel("Server IP");
		serverIPLabel.setPreferredSize(confirmLabel.getPreferredSize());
		
		username.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Register");
		username.getActionMap().put("Register", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				register();
			}
			
		});
		
		serverIP.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Register");
		serverIP.getActionMap().put("Register", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				register();
			}
			
		});
		
		password = new JPasswordField(20);
		password.setEchoChar((char) 8226);
		password.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Register");
		password.getActionMap().put("Register", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				register();
			}
			
		});
		
		confirm = new JPasswordField(20);
		confirm.setEchoChar((char) 8226);
		confirm.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Register");
		confirm.getActionMap().put("Register", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				register();
			}
			
		});
		
		JButton register = new JButton("Register");
		register.setFocusable(false);
		register.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				register();
			}
			
		});
		
		JButton back = new JButton("Back");
		back.setFocusable(false);
		back.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.switchTo(new LoginPanel(mainFrame, username, serverIP));
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
		
		JPanel serverIPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		serverIPPanel.add(serverIPLabel);
		serverIPPanel.add(serverIP);
		serverIPPanel.setOpaque(false);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonsPanel.add(register);
		buttonsPanel.add(back);
		buttonsPanel.setOpaque(false);
		
		JPanel registerPanel = new JPanel(new GridLayout(6, 1, 0, 5));
		registerPanel.add(registerLabel);
		registerPanel.add(usernamePanel);
		registerPanel.add(passwordPanel);
		registerPanel.add(confirmPasswordPanel);
		registerPanel.add(serverIPPanel);
		registerPanel.add(buttonsPanel);
		registerPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(191, 230, 249).darker()), BorderFactory.createEmptyBorder(10, 10, 10, 10)));	
		registerPanel.setOpaque(false);

		add(registerPanel);
	}
	
	private void register() {
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
			mainFrame.connection = new ClientConnection(serverIP.getText());
			if (!mainFrame.connection.register(username.getText(), pass)) {
				mainFrame.messageDialog.showMessage("This username/machine is already registered!", true);
			} else {
				mainFrame.messageDialog.showMessage("You have been successfully registered!", false);
				mainFrame.switchTo(new LoginPanel(mainFrame, username, serverIP));
			}
		} catch (UnknownHostException ex) {
			mainFrame.messageDialog.showMessage("The IP address of the server cannot be resolved!", true);
		} catch (Exception ex) {
			mainFrame.messageDialog.showMessage("Couldn't connect to server!", true);
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(ClientInterface.WINDOW_SIZE, ClientInterface.WINDOW_SIZE);
	}
	
}
