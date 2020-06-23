package editor.ui.sprite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import editor.ui.Editor;
import editor.ui.UIElement;

public class RenderControls extends UIElement{
	private JButton play;
	private JButton pause;
	private JLabel currentFrame;

	public RenderControls(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		play = new JButton("\u25B6");
		panel.add(play);
		pause = new JButton("\u23F8");
		panel.add(pause);
		panel.add(Box.createHorizontalGlue());
		currentFrame = new JLabel("0");
		panel.add(currentFrame);
		panel.add(Box.createRigidArea(new Dimension(2, 2)));
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
}
