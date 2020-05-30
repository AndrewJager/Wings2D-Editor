package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import framework.animation.Frame;
import framework.animation.SpriteSheet;
import framework.animation.Animation;

public class FilePanel extends UIElement{
	private JButton selectButton;
	private JButton newButton;
	private JButton renderBtn;
	private JButton saveBtn;
	private JLabel fileLabel;
	private JLabel fileName;

	public FilePanel(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		fileLabel = new JLabel("File:");
		panel.add(fileLabel);
		fileName = new JLabel("None");
		panel.add(fileName);
		selectButton = new JButton("Select"); 
		panel.add(selectButton);
		newButton = new JButton("New");
	    panel.add(newButton);
	    renderBtn = new JButton("Render");
	    panel.add(renderBtn);
	    saveBtn = new JButton("Save");
	    panel.add(saveBtn);
		panel.setLayout(new FlowLayout());
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getAnimLists();
		selectButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){  
	    		JFileChooser file = new JFileChooser("test");
	    		int result = file.showSaveDialog(editor.getFrame());
	    		if (result == JFileChooser.APPROVE_OPTION) 
	    		{
	    			fileName.setText(file.getSelectedFile().getName());
	    			try {
	    				Scanner in = new Scanner(new FileReader(file.getSelectedFile()));
	    				editor.setActiveSprite(new SpriteSheet(in, editor.getDemoLevel()));
	    				for (int i = 0; i < editor.getActiveSprite().getAnimations().size(); i++)
	    				{
	    					for (int j = 0; j < editor.getActiveSprite().getAnimations().get(i).getFrames().size(); j++) {
	    						editor.getActiveSprite().getAnimations().get(i).getFrames().get(j).setEditOptions(editor.getOptions());
	    					}
	    				}
	    				AnimationLists ani = editor.getAnimLists();
	    				ani.getAnimList().setListData(editor.getActiveSprite().getAnimNames());
	    				ani.getAnimList().setSelectedIndex(0);
	    				Animation curAnim = editor.getActiveSprite().getAnimation(ani.getAnimList().getSelectedIndex());
	    				ani.getFrameList().setListData(curAnim.getFrameNames());
	    				ani.getFrameList().setSelectedIndex(0);
	    				Frame curFrame = curAnim.getFrame(ani.getFrameList().getSelectedIndex());
	    				ani.getObjectList().setListData(curFrame.getJointNames());
	    				ani.getObjectList().setSelectedIndex(0);
	    				editor.getFilters().setFilterButtons(curFrame.getJoint(ani.getObjectList().getSelectedIndex()));
	    				in.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
	    		} else if (result == JFileChooser.CANCEL_OPTION) 
	    		{
	    		    System.out.println("Cancel was selected");
	    		}
	        }  
	    });
	    newButton.addActionListener(new ActionListener(){  
	    	public void actionPerformed(ActionEvent e){ 
	    		String spriteName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Name sprite",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		editor.setActiveSprite(new SpriteSheet(spriteName, editor.getDemoLevel()));
	        }  
	    }); 
	    renderBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsFrameSelected())
	    		{
	    			editor.setShouldReRender(true);
	    			editor.getRender().updateRender(false);
	    		}
	    	}
	    });
	    saveBtn.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		editor.getActiveSprite().saveToFile();
	    	}
	    });
	}
}
