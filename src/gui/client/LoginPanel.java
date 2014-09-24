package gui.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
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
import engine.Status;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel{
	
	private ClientInterface mainFrame;
	private JTextField username;
	private JTextField serverIP;
	private JPasswordField password;
	
	public LoginPanel(ClientInterface clientInterface) {
		this(clientInterface, new JTextField(20), new JTextField("localhost", 20));
	}
	
	public LoginPanel(ClientInterface clientInterface, JTextField name, JTextField ip) {
		mainFrame = clientInterface;
		this.username = name;
		this.serverIP = ip;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		
		JLabel loginLabel = new JLabel("Login");
		loginLabel.setFont(new Font("Consolas", Font.PLAIN, 26));

		JLabel serverIPLabel = new JLabel("Server IP");
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setPreferredSize(serverIPLabel.getPreferredSize());
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setPreferredSize(serverIPLabel.getPreferredSize());
		
		username.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Login");
		username.getActionMap().put("Login", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				login();
			}
			
		});
		
		password = new JPasswordField(20);
		password.setEchoChar((char) 8226);
		password.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Login");
		password.getActionMap().put("Login", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				login();
			}
			
		});
		
		serverIP.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "Login");
		serverIP.getActionMap().put("Login", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				login();
			}
			
		});
		
		
		JButton login = new JButton("Login");
		login.setFocusable(false);
		login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				login();
			}
			
		});
		
		JButton register = new JButton("Register");
		register.setFocusable(false);
		register.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.switchTo(new RegisterPanel(mainFrame, username, serverIP));
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
		
		JPanel serverIPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		serverIPPanel.add(serverIPLabel);
		serverIPPanel.add(serverIP);
		serverIPPanel.setOpaque(false);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonsPanel.add(login);
		buttonsPanel.add(register);
		buttonsPanel.setOpaque(false);
		
		JPanel loginPanel = new JPanel(new GridLayout(5, 1, 0, 5));
		loginPanel.add(loginLabel);
		loginPanel.add(usernamePanel);
		loginPanel.add(passwordPanel);
		loginPanel.add(serverIPPanel);
		loginPanel.add(buttonsPanel);
		loginPanel.setOpaque(false);
		loginPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(191, 230, 249).darker()), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		add(loginPanel);
	}
	
	private void login() {
		try {
			mainFrame.connection = new ClientConnection(serverIP.getText());
			String pass = new String(password.getPassword());
			if (!username.getText().matches("[a-zA-Z0-9]+") || !pass.matches("[a-zA-Z0-9]+")) {
				mainFrame.messageDialog.showMessage("Invalid username/password!", true);
				return;
			}
			mainFrame.connection.login(username.getText(), pass);
			if (mainFrame.connection.getStatus() == Status.NOT_REGISTERED) {
				mainFrame.messageDialog.showMessage("Invalid username/password!", true);
				return;
			}
			if (mainFrame.connection.getStatus() == Status.NO_SESSION) {
				mainFrame.messageDialog.showMessage("There's no current voting session!", true);
				return;
			}
			mainFrame.switchTo(new CandidatesPanel(mainFrame, new Point(0, 0)));
		} catch (UnknownHostException ex) {
			mainFrame.messageDialog.showMessage("The IP address of the server cannot be resolved!", true);
		} catch (Exception ex) {
			mainFrame.messageDialog.showMessage("Couldn't connect to server!", true);
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(mainFrame.windowSize, mainFrame.windowSize);
	}
	
}
