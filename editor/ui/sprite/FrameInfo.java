package editor.ui.sprite;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import editor.ui.Editor;

public class FrameInfo extends SpriteUIElement{
	private JLabel frameName;
	private JButton changeName;
	private JLabel frameTime;
	private JButton changeFrames;
	
	public FrameInfo(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frameName = new JLabel("Frame name");
		panel.add(frameName);
		changeName = new JButton("Change name");
		panel.add(changeName);
		frameTime = new JLabel("Frame time");
		panel.add(frameTime);
		changeFrames = new JButton("Change time");
		panel.add(changeFrames);
		
		panel.setLayout(new GridLayout(2, 2));
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getSpriteEdit().getAnimLists();
		changeName.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String frameName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Rename Frame",
	    				JOptionPane.PLAIN_MESSAGE, null, null, ani.getSelectedFrame().getName());
	    		if (ani.getFrameList().getSelectedIndex() != -1)
	    		{
	    			int index = ani.getFrameList().getSelectedIndex();
	    			if (ani.getSelectedAnimation().getFrameByName(frameName) == null)
	    			{
	    				ani.getSelectedFrame().setName(frameName);
	    				ani.getFrameList().setListData(ani.getSelectedAnimation().getFrameNames());
	    				ani.getFrameList().setSelectedIndex(index);
	    				getFrameNameLabel().setText(ani.getSelectedFrame().getName());
	    			}
	    			else
	    			{
		    			JOptionPane.showMessageDialog(editor.getFrame(),
		    					"Animation with this name already exists", "Invalid input", JOptionPane.WARNING_MESSAGE);
	    			}
	    		}
	    	}
	    });
	}
	
	public JLabel getFrameNameLabel() {
		return frameName;
	}
	public JLabel getFrameTimeLabel() {
		return frameTime;
	}
}
