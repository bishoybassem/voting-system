package gui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

import engine.ClientConnection;
import gui.MessageDialog;

@SuppressWarnings("serial")
public class ClientInterface extends JFrame{
	
	public ClientConnection connection;
	
	private JPanel mainPanel;
	private Timer refreshTimer;
	
	public final int windowSize;
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
		
		windowSize = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 200);
				
		refreshTimer = new Timer(5000, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				refreshCandidatesPanel();
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
		
		ImageIcon icon1 = new ImageIcon(getClass().getClassLoader().getResource("gui/resources/hand1.png"));
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("gui/resources/hand2.png"));
		
		add(mainPanel);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImages(Arrays.asList(new Image[]{icon1.getImage(), icon2.getImage()}));
		pack();
		setLocationRelativeTo(null);
		
		refreshTimer.start();
	}

	public void refreshCandidatesPanel() {
		if (!(mainPanel.getComponent(0) instanceof CandidatesPanel))
			return;
		
		Point viewPoint = ((CandidatesPanel)mainPanel.getComponent(0)).getViewPoint();
		try {
			connection.refresh();
			switchTo(new CandidatesPanel(this, viewPoint));
		} catch (Exception ex) {
			messageDialog.showMessage("Couldn't connect to server!", true);
			switchTo(new LoginPanel(this));
		}
	}
	
	public void switchTo(JPanel panel) {
		mainPanel.removeAll();
		mainPanel.add(panel);
		pack();
	}
	
	public static void main(String[] args) {
		new ClientInterface().setVisible(true);
	}
	
}
