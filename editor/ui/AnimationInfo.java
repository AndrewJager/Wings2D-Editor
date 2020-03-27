package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AnimationInfo {
	private Editor editor;
	private JPanel panel;
	private JLabel nameLabel;
	private JButton changeName;
	
	public AnimationInfo(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(10, 680, 150, 50);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameLabel = new JLabel("Anim name");
		panel.add(nameLabel);
		changeName = new JButton("Change Name");
		panel.add(changeName);
		
		panel.setLayout(new FlowLayout());
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getAnimLists();
		changeName.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String animName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Rename Animation",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		if (ani.getAnimList().getSelectedIndex() != -1)
	    		{
	    			int index = ani.getAnimList().getSelectedIndex();
	    			if (editor.getActiveSprite().getAnimByName(animName) == null)
	    			{
	    				ani.getSelectedAnimation().setName(animName);
	    				ani.getAnimList().setListData(editor.getActiveSprite().getAnimNames());
	    				ani.getAnimList().setSelectedIndex(index);
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

	public JPanel getPanel()
	{
		return panel;
	}
}
