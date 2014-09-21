package gui.server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class DatePanel extends JPanel {

	private JTextField[] fields;
	private ArrayList<JComboBox<String>> comboBoxes;
	
	private class JTextFieldLimit extends PlainDocument {
		
		private int limit;

		public JTextFieldLimit(int limit) {
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;
	
			if ((getLength() + str.length()) <= limit && Character.isDigit(str.charAt(0))) {
				super.insertString(offset, str, attr);
			}
		}
	}
	
	public DatePanel(boolean initialize) {
		fields = new JTextField[4];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField((i == 1)? 4 : 2);
			fields[i].setDocument(new JTextFieldLimit((i == 1)? 4 : 2));
			fields[i].setBorder(BorderFactory.createCompoundBorder(fields[i].getBorder(), BorderFactory.createEmptyBorder(0, 2, 0, 2)));
		}
		
		comboBoxes = new ArrayList<JComboBox<String>>();
		comboBoxes.add(new JComboBox<String>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"})); 
		comboBoxes.add(new JComboBox<String>(new String[]{"AM", "PM"}));
		for (JComboBox<String> comboBox : comboBoxes) {
			comboBox.setFocusable(false);
			comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width - 2, fields[0].getPreferredSize().height));
		}

		setLayout(new FlowLayout(FlowLayout.LEADING, 3, 0));
		add(fields[0]);
		add(new JLabel("/"));
		add(comboBoxes.get(0));
		add(new JLabel("/"));
		add(fields[1]);
		add(Box.createRigidArea(new Dimension(15, 0)));
		add(fields[2]);
		add(new JLabel(":"));
		add(fields[3]);
		add(Box.createRigidArea(new Dimension(2, 0)));
		add(comboBoxes.get(1));
		setOpaque(false);
		
		if (initialize) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR, 1);
			fields[0].setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
			fields[1].setText(calendar.get(Calendar.YEAR) + "");
			fields[2].setText(String.format("%02d", calendar.get(Calendar.HOUR)));
			fields[3].setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
			comboBoxes.get(0).setSelectedIndex(calendar.get(Calendar.MONTH));
			comboBoxes.get(1).setSelectedIndex(calendar.get(Calendar.AM));
		}
	}
	
	public String getDate() {
		return String.format("%s/%s/%s %s:%s %s", fields[0].getText(), comboBoxes.get(0).getSelectedItem(), fields[1].getText(), fields[2].getText(), fields[3].getText(), comboBoxes.get(1).getSelectedItem());
	}
	
}
