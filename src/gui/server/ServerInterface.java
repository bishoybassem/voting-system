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
	private boolean isSessionPanel;
	private SessionPanel sessionPanel;
	
	private JPanel mainPanel;
	private Timer refreshTimer;
	private Timer saveTimer;

	public int sideLength;
	public MessageDialog messageDialog;
	
	public ServerInterface() throws Exception {
		super("Server Voting System");

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		UIManager.put("Label.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("Button.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("TextField.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("TextArea.font", new Font("Consolas", Font.PLAIN, 18));
		UIManager.put("ComboBox.font", new Font("Consolas", Font.PLAIN, 18));
		
		serverData = new ServerData();
		sideLength = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 200);
		messageDialog = new MessageDialog(this);

		refreshTimer = new Timer(5000, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isSessionPanel) {
					switchToSessionPanel();
				}
			}
			
		});
		
		saveTimer = new Timer(60000, new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					serverData.saveAllData();
				} catch (Exception ex) {
					// XXX Auto-generated catch block
					ex.printStackTrace();
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

				GradientPaint gp1 = new GradientPaint(0, 0, new Color(89, 190, 237), 0, 90, Color.WHITE);
				g2d.setPaint(gp1);
				g2d.fillRect(0, 0, w, (h / 2) + 1);
				
				GradientPaint gp2 = new GradientPaint(0, sideLength - 80, Color.WHITE, 0, sideLength, new Color(89, 190, 237));
				g2d.setPaint(gp2);
				g2d.fillRect(0, (h / 2) - 1, w, h);

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
					// XXX Auto-generated catch block
					ex.printStackTrace();
				}	
			}

		});
		
		add(mainPanel);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		switchToSessionPanel();
		setLocationRelativeTo(null);
		
		refreshTimer.start();
		saveTimer.start();
	}
	
	public void switchToSessionPanel() {
		isSessionPanel = true;
		sessionPanel = new SessionPanel(this, (sessionPanel == null)? new Point(0, 0) : sessionPanel.getViewPoint());
		mainPanel.removeAll();
		mainPanel.add(sessionPanel);
		pack();
		mainPanel.requestFocusInWindow();
	}
	
	public void switchToNewSessionPanel() {
		isSessionPanel = false;
		sessionPanel = null;
		mainPanel.removeAll();
		mainPanel.add(new NewSessionPanel(this));
		pack();
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ServerInterface serverInterface = new ServerInterface();
		serverInterface.setVisible(true);
		ServerSocket serverSocket = new ServerSocket(6000);
		while (true) {
			new Thread(new ServerConnection(serverSocket.accept(), serverInterface.serverData)).start();
		}
	}
	
}
