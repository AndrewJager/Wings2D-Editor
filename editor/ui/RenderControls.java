package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RenderControls {
	private Editor editor;
	private JPanel panel;
	private JButton play;
	private JButton pause;
	private JLabel currentFrame;

	public RenderControls(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(700, 170, 150, 35);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		play = new JButton("Play");
		panel.add(play);
		pause = new JButton("Pause");
		panel.add(pause);
		currentFrame = new JLabel("0");
		panel.add(currentFrame);
		panel.setLayout(new FlowLayout());
	}
	
	public void createEvents()
	{
		 play.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		editor.setPlaying(true);
	    	}
	    });
	    pause.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		editor.setPlaying(false);
	    	}
	    });
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
