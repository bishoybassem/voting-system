package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MessageDialog extends JDialog {
	
	private JTextArea text;
	private JLabel image;
	
	public MessageDialog(JFrame main) {
		super(main, "Message", true);
		
		image = new JLabel();
		
		text = new JTextArea();
		text.setOpaque(false);
		text.setFocusable(false);
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
			
		});
		ok.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "exit");
		ok.getActionMap().put("exit", new AbstractAction(){

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
			
		});
		
		JPanel messagePanel = new JPanel(new FlowLayout(0, 10, FlowLayout.LEFT));
		messagePanel.add(image);
		messagePanel.add(text);
		messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		messagePanel.setOpaque(false);
		
		JPanel okPanel = new JPanel();
		okPanel.add(ok);
		okPanel.setOpaque(false);
		
		JPanel paintPanel = new JPanel(new BorderLayout()) {
        	
        	public void paintComponent(Graphics g) {
        	    if (!isOpaque()) {
        	        super.paintComponent(g);
        	        return;
        	    }
        	    Graphics2D g2d = (Graphics2D) g;
        	    int w = getWidth();
        	    int h = getHeight();
        	    
        	    GradientPaint gp1 = new GradientPaint(0, 0, Color.WHITE, w, h, new Color(130, 207, 244));
        	    g2d.setPaint(gp1);
        	    g2d.fillRect(0, 0, w, h);
        	 
        	    setOpaque(false);
        	    super.paintComponent(g);
        	    setOpaque(true);
        	}
        	
        };

        paintPanel.add(messagePanel);
		paintPanel.add(okPanel, BorderLayout.SOUTH);
		
		add(paintPanel);
		
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
	
	public void showMessage(String message, boolean isError) {
		image.setIcon(new ImageIcon(getClass().getResource("resources/" + ((isError)? "error" : "success") + ".png")));
		text.setText(message);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
		
}