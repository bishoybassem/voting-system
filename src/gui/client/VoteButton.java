package gui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import engine.Status;

@SuppressWarnings("serial")
public class VoteButton extends JButton {
	
	public VoteButton(final ClientInterface mainFrame, final int index) {
		super("Vote");
		setFocusable(false);
		
		if (mainFrame.connection.getStatus() != Status.CAN_VOTE) {
			setEnabled(false);
			return;
		}
		
		addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					mainFrame.connection.voteFor(mainFrame.connection.getCandidates().get(index).getName());
					mainFrame.refreshCandidatesPanel();
				} catch (Exception ex) {
					mainFrame.switchToLoginPanel();
					mainFrame.messageDialog.showMessage("Couldn't connect to server!", true);
				}
			}
			
		});
	}
	
}