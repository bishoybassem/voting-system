package gui.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class NewSessionPanel extends JPanel {

	private ServerInterface mainFrame;
	
	public NewSessionPanel(ServerInterface serverInterface) {
		mainFrame = serverInterface;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		
		JLabel newSession = new JLabel("New session");
		newSession.setFont(new Font("Consolas", Font.PLAIN, 26));
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				mainFrame.switchToSessionPanel();
			}
			
		});
		cancel.setFocusable(false);
		
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		headerPanel.add(cancel, BorderLayout.EAST);
		headerPanel.add(newSession, BorderLayout.WEST);

		add(headerPanel, BorderLayout.NORTH);
		
		final DatePanel startDatePanel = new DatePanel(true);
		final DatePanel endDatePanel = new DatePanel(true);
		
		JPanel dataPanel1 = new JPanel(new GridLayout(2, 1, 0, 5));
		dataPanel1.setOpaque(false);
		dataPanel1.add(new JLabel("Start date"));
		dataPanel1.add(new JLabel("End date"));
		
		JPanel dataPanel2 = new JPanel(new GridLayout(2, 1, 0, 5));
		dataPanel2.setOpaque(false);
		dataPanel2.add(new JLabel(":"));
		dataPanel2.add(new JLabel(":"));
		
		JPanel dataPanel3 = new JPanel(new GridLayout(2, 1, 0, 5));
		dataPanel3.setOpaque(false);
		dataPanel3.add(startDatePanel);
		dataPanel3.add(endDatePanel);
					
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
		p1.setOpaque(false);
		p1.add(dataPanel1);
		p1.add(dataPanel2);
		p1.add(dataPanel3);
		
		final JTextArea candidatesTextArea = new JTextArea();
		candidatesTextArea.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 5));
		
		Border b = BorderFactory.createMatteBorder(1, 1, 1, 0, new Color(89, 190, 237));
		
		JScrollPane scrollPane = new JScrollPane(candidatesTextArea); 
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 40, 0, 20), b));
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		JLabel candidates = new JLabel("Candidates");
		candidates.setForeground(Color.RED);
		
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 15));
		p2.setOpaque(false);
		p2.add(candidates);
		
		JPanel p3 = new JPanel(new BorderLayout(0, 0));
		p3.setOpaque(false);
		p3.add(p2, BorderLayout.NORTH);
		p3.add(scrollPane);
		
		JPanel p4 = new JPanel(new BorderLayout(0, 0));
		p4.setOpaque(false);
		p4.add(p1, BorderLayout.NORTH);
		p4.add(p3);
		
		add(p4);
		
		JButton create = new JButton("Create");
		create.setFocusable(false);
		create.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy hh:mm a");
				Date startDate = null;
				try {
					startDate = formatter.parse(startDatePanel.getDate());
				} catch (Exception e) {
					mainFrame.messageDialog.showMessage("Invalid start date!", true);
					return;
				}
				if (startDate.compareTo(new Date()) < 0) {
					mainFrame.messageDialog.showMessage("Invalid start date!", true);
					return;
				}
				Date endDate = null;
				try {
					endDate = formatter.parse(endDatePanel.getDate());
				} catch (Exception e) {
					mainFrame.messageDialog.showMessage("Invalid end date!", true);
					return;
				}
				if (endDate.compareTo(startDate) <= 0) {
					mainFrame.messageDialog.showMessage("Invalid end date!", true);
					return;
				}
				String candidates = candidatesTextArea.getText();
				if (!candidates.trim().matches("([\\w ]+\n+)+[\\w ]+")) {
					mainFrame.messageDialog.showMessage("Invalid candidates names\nOnly letters, digits and spaces are allowed!", true);
					return;
				}
				mainFrame.serverData.newSession(candidates, startDate, endDate);
				mainFrame.switchToSessionPanel();
			}
			
		});
		
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		p5.setOpaque(false);
		p5.add(create);
		
		add(p5, BorderLayout.SOUTH);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(mainFrame.sideLength, mainFrame.sideLength);
	}
	
}