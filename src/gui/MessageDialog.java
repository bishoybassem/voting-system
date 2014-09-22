package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

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
	
	public static final int ERROR = 0;
	public static final int SUCCESS = 1;
	
	private JTextArea text;
	private JLabel image;
	
	private static final String aboutText;
	
	static {
		aboutText = readTextFile("resources/about.txt");
	}
	
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
		
		JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		messagePanel.add(image);
		messagePanel.add(text);
		messagePanel.setOpaque(false);
		
		JPanel okPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		okPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
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
        	    
        	    GradientPaint gp1 = new GradientPaint(0, 0, Color.WHITE, w, h, new Color(191, 230, 249));
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
	
	public void showAbout() {
		image.setIcon(new ImageIcon(getClass().getResource("resources/hand2.png")));
		text.setText(aboutText);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void showMessage(String message, boolean isError) {
		image.setIcon(new ImageIcon(getClass().getResource("resources/" + (isError? "error" : "success") + ".png")));
		text.setText(message);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static String readTextFile(String path) {
		String text = "";
		Scanner sc = null;
		try {
			sc = new Scanner(MessageDialog.class.getResourceAsStream(path));
			while (sc.hasNext()){
				text += sc.nextLine() + (sc.hasNext() ? "\n" : "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (sc != null){
				sc.close();
			}	
		}
		return text;
	}
		
}