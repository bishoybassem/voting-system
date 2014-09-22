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
import engine.Status;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel{
	
	private ClientInterface mainFrame;
	
	public LoginPanel(ClientInterface clientInterface) {
		mainFrame = clientInterface;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		
		JLabel loginLabel = new JLabel("Login");
		loginLabel.setFont(new Font("Consolas", Font.PLAIN, 26));

		final JTextField username = new JTextField(20);
		
		final JPasswordField password = new JPasswordField(20);
		password.setEchoChar((char) 8226);
		
		JButton login = new JButton("Login");
		login.setFocusable(false);
		login.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					mainFrame.connection = new ClientConnection();
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
					mainFrame.switchToCandidatesPanel();
				} catch (Exception ex) {
					mainFrame.messageDialog.showMessage("Couldn't connect to server!", true);
				}
			}
			
		});
		
		JButton register = new JButton("Register");
		register.setFocusable(false);
		register.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.switchToRegisterPanel();
			}
			
		});
		
		JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		usernamePanel.add(new JLabel("Username"));
		usernamePanel.add(username);
		usernamePanel.setOpaque(false);
		
		JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		passwordPanel.add(new JLabel("Password"));
		passwordPanel.add(password);
		passwordPanel.setOpaque(false);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		buttonsPanel.add(login);
		buttonsPanel.add(register);
		buttonsPanel.setOpaque(false);
		
		JPanel loginPanel = new JPanel(new GridLayout(4, 1, 0, 5));
		loginPanel.add(loginLabel);
		loginPanel.add(usernamePanel);
		loginPanel.add(passwordPanel);
		loginPanel.add(buttonsPanel);
		loginPanel.setOpaque(false);
		loginPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(191, 230, 249).darker()), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		add(loginPanel);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(mainFrame.windowSize, mainFrame.windowSize);
	}
	
}
