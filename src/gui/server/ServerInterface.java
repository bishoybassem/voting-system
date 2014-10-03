package gui.server;

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
import java.net.ServerSocket;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

import engine.ServerConnection;
import engine.ServerData;
import gui.MessageDialog;

@SuppressWarnings("serial")
public class ServerInterface extends JFrame{

	public ServerData serverData;
	
	private JPanel mainPanel;
	private Timer refreshTimer;
	private Timer saveTimer;

	public MessageDialog messageDialog;
	public static final int WINDOW_SIZE;
	
	static {
		WINDOW_SIZE = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 200);
	}
	
	public ServerInterface() throws Exception {
		super("Voting System - Server");

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {

		}
		UIManager.put("Label.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("Button.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("TextField.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("ComboBox.font", new Font("Consolas", Font.PLAIN, 18));
		
		ImageIcon icon1 = new ImageIcon(getClass().getClassLoader().getResource("gui/resources/hand1.png"));
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("gui/resources/hand2.png"));
		setIconImages(Arrays.asList(icon1.getImage(), icon2.getImage()));
		
		serverData = new ServerData();
		messageDialog = new MessageDialog(this);

		refreshTimer = new Timer(5000, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				refreshSessionPanel();
			}
			
		});
		
		saveTimer = new Timer(60000, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					serverData.saveAllData();
				} catch (Exception ex) {

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
		
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent arg0) {
				try {
					serverData.saveAllData();
				} catch (Exception ex) {

				}	
			}

		});
		
		add(mainPanel);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		switchTo(new SessionPanel(this));
		setLocationRelativeTo(null);
		
		refreshTimer.start();
		saveTimer.start();
	}
	
	public void refreshSessionPanel() {
		if (!(mainPanel.getComponent(0) instanceof SessionPanel))
			return;
		
		Point viewPoint = ((SessionPanel)mainPanel.getComponent(0)).getViewPoint();
		switchTo(new SessionPanel(this, viewPoint));
	}
	
	public void switchTo(JPanel panel) {
		mainPanel.removeAll();
		mainPanel.add(panel);
		pack();
	}
		
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ServerInterface serverInterface = new ServerInterface();
		serverInterface.setVisible(true);
		ServerSocket serverSocket = new ServerSocket(ServerConnection.PORT);
		while (true) {
			new Thread(new ServerConnection(serverSocket.accept(), serverInterface.serverData)).start();
		}
	}
	
}