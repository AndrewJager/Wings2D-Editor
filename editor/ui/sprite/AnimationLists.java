package editor.ui.sprite;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import editor.objects.EditorAnimation;
import editor.objects.EditorFrame;
import editor.objects.EditorJoint;
import editor.ui.Editor;

public class AnimationLists extends SpriteUIElement{
	private JPanel animsPanel;
	private JPanel framesPanel;
	private JPanel objectsPanel;
	private JLabel animsLabel;
	private JLabel framesLabel;
	private JLabel objectsLabel;
	private JButton newAnim;
	private JButton newFrame;
	private JButton newObject;
	private JList<String> animList;
	private JList<String> frameList;
	private JList<String> objectList;
	private JScrollPane animScroll;
	private JScrollPane frameScroll;
	private JScrollPane objectScroll;
	
	public AnimationLists(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setLayout(new GridLayout(1, 3));
		
		animsPanel = new JPanel();
		animsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		animsLabel = new JLabel("Animations:");
		animsPanel.add(animsLabel);
		animList = new JList<String>();
		animScroll = new JScrollPane(animList);  
		animsPanel.add(animScroll);
		newAnim = new JButton("New Animation");
		animsPanel.add(newAnim);
		animsPanel.setLayout(new BoxLayout(animsPanel, BoxLayout.PAGE_AXIS));
		panel.add(animsPanel);
		
		framesPanel = new JPanel();
		framesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		framesLabel = new JLabel("Frames:");
		framesPanel.add(framesLabel);
		frameList = new JList<String>();
		frameScroll = new JScrollPane(frameList);  
		framesPanel.add(frameScroll);
		newFrame = new JButton("New Frame");
		framesPanel.add(newFrame);
		framesPanel.setLayout(new BoxLayout(framesPanel, BoxLayout.PAGE_AXIS));
		panel.add(framesPanel);
		
		objectsPanel = new JPanel();
		objectsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		objectsLabel = new JLabel("Objects:");
		objectsPanel.add(objectsLabel);
		objectList = new JList<String>();
		objectScroll = new JScrollPane(objectList);
		objectsPanel.add(objectScroll);
		newObject = new JButton("New Object");
		objectsPanel.add(newObject);
		objectsPanel.setLayout(new BoxLayout(objectsPanel, BoxLayout.PAGE_AXIS));
		panel.add(objectsPanel);
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getSpriteEdit().getAnimLists();
		newAnim.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String animName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Add animation",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Animation");
	    		editor.getActiveSprite().addNewAnimation(animName);
	    		animList.setListData(editor.getActiveSprite().getAnimNames());
	    		animList.setSelectedIndex(editor.getActiveSprite().getAnimations().size() - 1);
	    	}
	    });
	    newFrame.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1)
	    		{
		    		EditorAnimation curAnim = ani.getSelectedAnimation();
		    		String frameName = curAnim.getName()
		    				+ "_" + frameList.getModel().getSize();
		    		if (frameList.getModel().getSize() <= 0)
		    		{
		    			curAnim.addNewFrame(frameName);
		    		}
		    		else
		    		{
		    			curAnim.addNewFrame(frameName, curAnim.getFrame(frameList.getModel().getSize() - 1));
		    		}
		    		frameList.setListData(curAnim.getFrameNames());
		    		frameList.setSelectedIndex(curAnim.getFrames().size() - 1);
		    		ani.getSelectedFrame().setEditOptions(editor.getOptions());
	    		}
	    	}
	    });
	    newObject.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String objectName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Add object",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		EditorFrame curFrame = ani.getSelectedFrame();
	    		if (curFrame.getJointByName(objectName) == null)
	    		{ // Object with this name does not exist
		    		curFrame.addNewJoint(objectName);
		    		objectList.setListData(curFrame.getJointNames());
		    		objectList.setSelectedIndex(curFrame.getJoints().size() - 1);
		    		editor.getSpriteEdit().getRender().setShouldReRender(true);
	    		}
	    		else
	    		{
	    			JOptionPane.showMessageDialog(editor.getFrame(),
	    					"Object with this name already exists", "Invalid input", JOptionPane.WARNING_MESSAGE);
	    		}
	    	}
	    });
        objectList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                	if (editor.getSpriteEdit().getAnimLists().getIsObjectSelected())
                	{
	                	editor.getSpriteEdit().getObjectInfo().updateInfo(editor.getSpriteEdit().getAnimLists().getSelectedObject());
	                	editor.getSpriteEdit().getEditOptions().getEditing().setSelected(false);
	        			editor.getSpriteEdit().getFilters().setFilterButtons(ani.getSelectedObject());
	        			editor.getSpriteEdit().getDrawing().setShouldRedraw(true);
                	}
                }
            }
        });
        frameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                	editor.getSpriteEdit().getEditOptions().getEditing().setSelected(false);
                	if (ani.getIsFrameSelected())
                	{
	                	EditorFrame curFrame = ani.getSelectedFrame();
	                	objectList.setListData(curFrame.getJointNames());
	                	objectList.setSelectedIndex(0);
	                	editor.getSpriteEdit().getFrameInfo().getFrameNameLabel().setText(curFrame.getName());
	                	editor.getSpriteEdit().getFrameInfo().getFrameTimeLabel().setText(Double.toString(curFrame.getFrameTime()));
	                	editor.getSpriteEdit().getDrawing().setShouldRedraw(true);
                	}
                }
            }
        });
	}
	public EditorAnimation getSelectedAnimation()
	{
		return editor.getActiveSprite().getAnimation(animList.getSelectedIndex());
	}
	public EditorFrame getSelectedFrame()
	{
		return editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	}
	public EditorJoint getSelectedObject()
	{
		return editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex())
				.getJoint(objectList.getSelectedIndex());
	}
	public Boolean getIsObjectSelected()
	{
		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1 && objectList.getSelectedIndex() != -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public Boolean getIsFrameSelected()
	{
		if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public JList<String> getAnimList()
	{
		return animList;
	}
	public JList<String> getFrameList()
	{
		return frameList;
	}
	public JList<String> getObjectList()
	{
		return objectList;
	}
	public JPanel getAnimPanel()
	{
		return animsPanel;
	}
	public JPanel getFramePanel()
	{
		return framesPanel;
	}
	public JPanel getObjectPanel()
	{
		return objectsPanel;
	}
}
