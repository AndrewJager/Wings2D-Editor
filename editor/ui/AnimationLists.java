package editor.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import editor.objects.Animation;
import editor.objects.Frame;

public class AnimationLists {
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
	
	public AnimationLists()
	{
		animsPanel = new JPanel();
		animsPanel.setBounds(10, 70, 150, 600);
		animsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		animsLabel = new JLabel("Animations:");
		animsPanel.add(animsLabel);
		animList = new JList<String>();
		animScroll = new JScrollPane(animList);  
		animsPanel.add(animScroll);
		newAnim = new JButton("New Animation");
		animsPanel.add(newAnim);
		animsPanel.setLayout(new BoxLayout(animsPanel, BoxLayout.PAGE_AXIS));
		
		framesPanel = new JPanel();
		framesPanel.setBounds(170, 70, 150, 600);
		framesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		framesLabel = new JLabel("Frames:");
		framesPanel.add(framesLabel);
		frameList = new JList<String>();
		frameScroll = new JScrollPane(frameList);  
		framesPanel.add(frameScroll);
		newFrame = new JButton("New Frame");
		framesPanel.add(newFrame);
		framesPanel.setLayout(new BoxLayout(framesPanel, BoxLayout.PAGE_AXIS));
		
		objectsPanel = new JPanel();
		objectsPanel.setBounds(330, 70, 150, 600);
		objectsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		objectsLabel = new JLabel("Objects:");
		objectsPanel.add(objectsLabel);
		objectList = new JList<String>();
		objectScroll = new JScrollPane(objectList);
		objectsPanel.add(objectScroll);
		newObject = new JButton("New Object");
		objectsPanel.add(newObject);
		objectsPanel.setLayout(new BoxLayout(objectsPanel, BoxLayout.PAGE_AXIS));
	}
	
	public void createEvents(EditOptions editOptions, DrawingArea drawing, JFrame frame, Editor editor)
	{
		newAnim.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String animName = (String)JOptionPane.showInputDialog(frame, "","Add animation",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Animation");
	    		editor.getActiveSprite().addAnimation(animName);
	    		animList.setListData(editor.getActiveSprite().getAnimNames());
	    		animList.setSelectedIndex(editor.getActiveSprite().getAnimations().size() - 1);
	    	}
	    });
	    newFrame.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (animList.getSelectedIndex() != -1)
	    		{
		    		Animation curAnim = editor.getActiveSprite().getAnimation(animList.getSelectedIndex());
		    		String frameName = curAnim.getName()
		    				+ "_" + frameList.getModel().getSize();
		    		if (frameList.getModel().getSize() <= 0)
		    		{
		    			curAnim.addFrame(frameName, true);
		    		}
		    		else
		    		{
		    			curAnim.addFrame(frameName, curAnim.getFrame(frameList.getModel().getSize() - 1));
		    		}
		    		frameList.setListData(curAnim.getFrameNames());
		    		frameList.setSelectedIndex(curAnim.getFrames().size() - 1);
		    		editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()).setOptions(editor.getOptions());;
	    		}
	    	}
	    });
	    newObject.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String objectName = (String)JOptionPane.showInputDialog(frame, "","Add object",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		Frame curFrame = editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	    		if (curFrame.getObjectByName(objectName) == null)
	    		{ // Object with this name does not exist
		    		curFrame.addObject(objectName);
		    		objectList.setListData(curFrame.getObjectNames());
		    		objectList.setSelectedIndex(curFrame.getObjects().size() - 1);
		    		editor.setShouldReRender(true);
	    		}
	    		else
	    		{
	    			JOptionPane.showMessageDialog(frame,
	    					"Object with this name already exists", "Invalid input", JOptionPane.WARNING_MESSAGE);
	    		}
	    	}
	    });
        objectList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                	editOptions.getEditing().setSelected(false);
        			editor.updateDrawing(editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing.getPanel(),
        					objectList.getSelectedIndex());
                }
            }
        });
        frameList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting()) {
                	editOptions.getEditing().setSelected(false);
                	if (animList.getSelectedIndex() != -1 && frameList.getSelectedIndex() != -1)
                	{
	                	Frame curFrame = editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex());
	                	if (curFrame.getIsMaster())
	                	{
	                		editOptions.getEditing().setEnabled(true);
	                	}
	                	else
	                	{
	                		editOptions.getEditing().setEnabled(false);
	                	}
	                	objectList.setListData(curFrame.getObjectNames());
	        			editor.updateDrawing(editor.getActiveSprite().getAnimation(animList.getSelectedIndex()).getFrame(frameList.getSelectedIndex()), drawing.getPanel(),
	        					objectList.getSelectedIndex());
                	}
                }
            }
        });
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
