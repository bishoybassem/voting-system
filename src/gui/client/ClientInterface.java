package gui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

import engine.ClientConnection;
import gui.MessageDialog;

@SuppressWarnings("serial")
public class ClientInterface extends JFrame{
	
	public ClientConnection connection;
	private boolean isCandidatesPanel;
	private CandidatesPanel candidatesPanel;
	
	private JPanel mainPanel;
	private Timer refreshTimer;
	
	public final int sideLength;
	public final MessageDialog messageDialog;
	
	public ClientInterface() {
		super("Voting System - Client");
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
	
		}
		UIManager.put("Label.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("Button.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("TextField.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("PasswordField.font", new Font("Consolas", Font.PLAIN, 18));
		
		messageDialog = new MessageDialog(this);
		
		sideLength = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 200);
		
		setResizable(false);
		
		refreshTimer = new Timer(5000, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isCandidatesPanel) {
					refreshCandidatesPanel();
				}
			}
			
		});
		
		mainPanel = new JPanel(new BorderLayout()) {

			public void paintComponent(Graphics g) {
				if (!isOpaque()) {
					super.paintComponent(g);
					return;
				}
				Graphics2D g2d = (Graphics2D) g;
				int w = getWidth();
				int h = getHeight();

				GradientPaint gp1 = new GradientPaint(0, 0, Color.WHITE, 0, h / 2, new Color(191, 230, 249));
				GradientPaint gp2 = new GradientPaint(0, h / 2, new Color(191, 230, 249), 0, h, Color.WHITE);
				
				g2d.setPaint(gp1);
				g2d.fillRect(0, 0, w, h / 2);
								
				g2d.setPaint(gp2);
				g2d.fillRect(0, h / 2, w, h);
				setOpaque(false);
				super.paintComponent(g);
				setOpaque(true);
			}

		};
		mainPanel.add(new LoginPanel(this));
		
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				try {
					connection.closeConnection();
				} catch (Exception ex) {
					
				}	
			}

		});
		
		add(mainPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		
		refreshTimer.start();
	}
	
	public void switchToRegisterPanel() {
		isCandidatesPanel = false;
		candidatesPanel = null;
		mainPanel.removeAll();
		mainPanel.add(new RegisterPanel(this));
		pack();
	}
	
	public void switchToLoginPanel() {
		isCandidatesPanel = false;
		candidatesPanel = null;
		mainPanel.removeAll();
		mainPanel.add(new LoginPanel(this));
		pack();
	}
	
	public void switchToCandidatesPanel() {
		isCandidatesPanel = true;
		candidatesPanel = new CandidatesPanel(this, (candidatesPanel == null)? new Point(0, 0) : candidatesPanel.getViewPoint());
		mainPanel.removeAll();
		mainPanel.add(candidatesPanel);
		pack();
	}
	
	public void refreshCandidatesPanel() {
		try {
			connection.refresh();
			switchToCandidatesPanel();
		} catch (Exception ex) {
			messageDialog.showMessage("Couldn't connect to server!", true);
			switchToLoginPanel();
		}
	}
	
	public static void main(String[] args) {
		new ClientInterface().setVisible(true);
	}
	
}
