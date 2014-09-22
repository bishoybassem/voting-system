package gui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class CandidatesPanel extends JPanel {

	private ClientInterface mainFrame;
	private JScrollPane scrollPane;
	
	public CandidatesPanel(ClientInterface clientInterface, Point viewPoint) {
		mainFrame = clientInterface;
		
		setLayout(new BorderLayout(0, 0));
		setOpaque(false);
		
		JLabel welcome = new JLabel("Welcome ");
		welcome.setFont(new Font("Consolas", Font.PLAIN, 26));
		
		JLabel username = new JLabel(mainFrame.connection.getUser().getUsername());
		username.setForeground(Color.RED);
		username.setFont(new Font("Consolas", Font.PLAIN, 26));
		
		JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		welcomePanel.add(welcome);
		welcomePanel.add(username);
		welcomePanel.setOpaque(false);
		
		JButton signout = new JButton("Sign out");
		signout.setFocusable(false);
		signout.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					mainFrame.connection.closeConnection();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				mainFrame.connection = null;
				mainFrame.switchToLoginPanel();
			}
			
		});
		
		JPanel headerPanel = new JPanel(new BorderLayout(0, 0));
		headerPanel.add(signout, BorderLayout.EAST);
		headerPanel.add(welcomePanel, BorderLayout.WEST);
		headerPanel.setOpaque(false);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		add(headerPanel, BorderLayout.NORTH);
		
		JLabel winner = new JLabel((mainFrame.connection.getWinner() == null)? "-" : mainFrame.connection.getWinner());
		winner.setForeground(Color.GREEN.darker());
		
		JLabel votedFor = new JLabel(mainFrame.connection.getUser().getVote() == null ? "-" : mainFrame.connection.getUser().getVote());
		votedFor.setForeground(Color.GREEN.darker());
		
		JPanel dataPanel1 = new JPanel(new GridLayout(4, 1, 0, 5));
		dataPanel1.setOpaque(false);
		dataPanel1.add(new JLabel("Start date"));
		dataPanel1.add(new JLabel("End date"));
		dataPanel1.add(new JLabel("Voted for"));
		dataPanel1.add(new JLabel("Winner"));
		
		JPanel dataPanel2 = new JPanel(new GridLayout(4, 1, 0, 5));
		dataPanel2.setOpaque(false);
		dataPanel2.add(new JLabel(":"));
		dataPanel2.add(new JLabel(":"));
		dataPanel2.add(new JLabel(":"));
		dataPanel2.add(new JLabel(":"));
		
		JPanel dataPanel3 = new JPanel(new GridLayout(4, 1, 0, 5));
		dataPanel3.setOpaque(false);
		dataPanel3.add(new JLabel(mainFrame.connection.getStartDate()));
		dataPanel3.add(new JLabel(mainFrame.connection.getEndDate()));
		dataPanel3.add(votedFor);
		dataPanel3.add(winner);
		
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
		p1.setOpaque(false);
		p1.add(dataPanel1);
		p1.add(dataPanel2);
		p1.add(dataPanel3);
				
		final ArrayList<Candidate> candidates = mainFrame.connection.getCandidates();
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
		
		JPanel columnHeadersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
		columnHeadersPanel.setOpaque(false);
		columnHeadersPanel.add(Box.createRigidArea(new Dimension(50 + new VoteButton(clientInterface, 0).getPreferredSize().width, 0)));
		columnHeadersPanel.add(votesLabel);
		columnHeadersPanel.add(Box.createRigidArea(new Dimension(40, 0)));
		columnHeadersPanel.add(candidateLabel);
		
		JPanel candidatesPanel = new JPanel(new GridLayout(candidates.size(), 1, 0, 0));
		candidatesPanel.setOpaque(false);
		candidatesPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		for (int i = 0; i < candidates.size(); i++) {
			VoteButton vote = new VoteButton(mainFrame, i);
			votesLabel = new JLabel(String.format("%-" + length + "d", candidates.get(i).getVotes()));
			candidateLabel = new JLabel(candidates.get(i).getName());
			
			JPanel candidatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
			candidatePanel.setOpaque(false);
			candidatePanel.add(Box.createRigidArea(new Dimension(10, 0)));
			candidatePanel.add(vote);
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
				mainFrame.refreshCandidatesPanel();
			}
			
		});
		
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		p5.setOpaque(false);
		p5.add(refresh);
		
		add(p5, BorderLayout.SOUTH);
	}
	
	public Point getViewPoint() {
		return scrollPane.getViewport().getViewPosition();
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(mainFrame.sideLength, mainFrame.sideLength);
	}
	
}