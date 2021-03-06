package gui.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import engine.Candidate;

@SuppressWarnings("serial")
public class SessionPanel extends JPanel {

	private ServerInterface mainFrame;
	private JScrollPane scrollPane;
	
	public SessionPanel(ServerInterface serverInterface) {
		this(serverInterface, new Point(0, 0));
	}
	
	public SessionPanel(ServerInterface serverInterface, Point viewPoint) {
		mainFrame = serverInterface;
		
		setLayout(new BorderLayout(0, 0));
		setOpaque(false);
		
		JButton newSession = new JButton("New session");
		newSession.setFocusable(false);
		newSession.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.switchTo(new NewSessionPanel(mainFrame));
			}
			
		});

		if (mainFrame.serverData.isSessionLoaded()) {
			newSession.setEnabled(mainFrame.serverData.isSessionFinished());
		} else {
			setLayout(new GridBagLayout());
			add(newSession);
			return;
		}
		
		JLabel currentSession = new JLabel("Current session");
		currentSession.setFont(new Font("Consolas", Font.PLAIN, 26));
		
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		headerPanel.add(newSession, BorderLayout.EAST);
		headerPanel.add(currentSession, BorderLayout.WEST);
		
		add(headerPanel, BorderLayout.NORTH);
		
		JLabel winner = new JLabel((serverInterface.serverData.getWinner() == null)? "-" : serverInterface.serverData.getWinner());
		winner.setForeground(Color.GREEN.darker());

		JPanel dataPanel1 = new JPanel(new GridLayout(4, 1, 0, 5));
		dataPanel1.setOpaque(false);
		dataPanel1.add(new JLabel("Server IP"));
		dataPanel1.add(new JLabel("Start date"));
		dataPanel1.add(new JLabel("End date"));
		dataPanel1.add(new JLabel("Winner"));
		
		JPanel dataPanel2 = new JPanel(new GridLayout(4, 1, 0, 5));
		dataPanel2.setOpaque(false);
		dataPanel2.add(new JLabel(":"));
		dataPanel2.add(new JLabel(":"));
		dataPanel2.add(new JLabel(":"));
		dataPanel2.add(new JLabel(":"));
		
		JPanel dataPanel3 = new JPanel(new GridLayout(4, 1, 0, 5));
		dataPanel3.setOpaque(false);
		try {
			dataPanel3.add(new JLabel(InetAddress.getLocalHost().getHostAddress()));
		} catch (Exception e) {
			dataPanel3.add(new JLabel("-"));
		}
		dataPanel3.add(new JLabel(serverInterface.serverData.getStartDate()));
		dataPanel3.add(new JLabel(serverInterface.serverData.getEndDate()));
		dataPanel3.add(winner);
					
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
		p1.setOpaque(false);
		p1.add(dataPanel1);
		p1.add(dataPanel2);
		p1.add(dataPanel3);
		
		ArrayList<Candidate> candidates = new ArrayList<Candidate>(serverInterface.serverData.getCandidates().values());
		int length = 10;
		for (Candidate candidate : candidates) {
			int x = (candidate.getVotes() + "").length();
			if (length < x) {
				length = x;
			}
		}

		JLabel votesLabel = new JLabel(String.format("%-" + length + "s", "Votes"));
		votesLabel.setForeground(Color.RED);
		
		JLabel candidateLabel = new JLabel("Candidates");
		candidateLabel.setForeground(Color.RED);
	
		JPanel columnHeadersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 10));
		columnHeadersPanel.setOpaque(false);
		columnHeadersPanel.add(votesLabel);
		columnHeadersPanel.add(candidateLabel);
		
		JPanel candidatesPanel = new JPanel(new GridLayout(candidates.size(), 1, 0, 0));
		candidatesPanel.setOpaque(false);
		candidatesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		for (Candidate candidate : candidates) {
			votesLabel = new JLabel(String.format("%-" + length + "d", candidate.getVotes()));
			candidateLabel = new JLabel(candidate.getName());

			JPanel candidatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
			candidatePanel.setOpaque(false);
			candidatePanel.add(Box.createRigidArea(new Dimension(20, 0)));
			candidatePanel.add(votesLabel);
			candidatePanel.add(Box.createRigidArea(new Dimension(40, 0)));
			candidatePanel.add(candidateLabel);
			
			candidatesPanel.add(candidatePanel);
		}
		
		JPanel p2 = new JPanel(new BorderLayout(0, 0));
		p2.setOpaque(false);
		p2.add(candidatesPanel, BorderLayout.NORTH);
		
		scrollPane = new JScrollPane(p2); 
		scrollPane.getViewport().setViewPosition(viewPoint);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20), new LineBorder(new Color(191, 230, 249).darker())));
		
		JPanel p3 = new JPanel(new BorderLayout(0, 0));
		p3.setOpaque(false);
		p3.add(columnHeadersPanel, BorderLayout.NORTH);
		p3.add(scrollPane);
		
		JPanel p4 = new JPanel(new BorderLayout(0, 0));
		p4.setOpaque(false);
		p4.add(p1, BorderLayout.NORTH);
		p4.add(p3);

		add(p4);
		
		JButton refresh = new JButton("Refresh");
		refresh.setFocusable(false);
		refresh.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.refreshSessionPanel();
			}
			
		});
		
		JButton about = new JButton("About");
		about.setFocusable(false);
		about.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.messageDialog.showAbout();
			}
			
		});
		
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		p5.setOpaque(false);
		p5.add(refresh);
		p5.add(about);
		
		add(p5, BorderLayout.SOUTH);
	}
	
	public Point getViewPoint() {
		if (mainFrame.serverData.isSessionLoaded())
			return scrollPane.getViewport().getViewPosition();

		return new Point(0, 0);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(ServerInterface.WINDOW_SIZE, ServerInterface.WINDOW_SIZE);
	}
	
}

