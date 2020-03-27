package editor.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import framework.animation.Frame;
import framework.animation.Joint;

public class ObjectInfo {
	private Editor editor;
	private JPanel panel;
	private JLabel infoLabel;
	private JLabel nameLabel;
	private JButton addVertex;
	private JButton setColor;
	private JButton addFilter;
	private JButton changeName;

	public ObjectInfo(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(500, 10, 180, 195);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		infoLabel = new JLabel("Object Info");
		infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 14f));
		panel.add(infoLabel);
		nameLabel = new JLabel("Object name");
		panel.add(nameLabel);
		addVertex = new JButton("Add vertix");
		panel.add(addVertex);
		setColor = new JButton("Change color");
		panel.add(setColor);
		addFilter = new JButton("Add filter");
		panel.add(addFilter);
		changeName = new JButton("Change name");
		panel.add(changeName);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getAnimLists();
		addVertex.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			Frame curFrame = ani.getSelectedFrame();
	    			curFrame.addVertex(ani.getObjectList().getSelectedIndex());
	    			editor.getDrawing().updateDrawing();
	    			editor.setShouldReRender(true);
	    		}
	    	}
	    });
	    setColor.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    		    Joint joint = ani.getSelectedObject();
	    			Color color = JColorChooser.showDialog(editor.getFrame(), "Select a color", joint.getColor());    
	    			joint.setColor(color);
	    			editor.getDrawing().updateDrawing();
	    			editor.setShouldReRender(true);
	    		}
	    	}
	    });
        
	    addFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			Joint curObject = ani.getSelectedObject();
		    		Object[] filters = {"Basic Variance", "Blur Edges", "Darken From", "Lighten From", "Outline"};
		    		String filterName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Choose Filter",
		    				JOptionPane.PLAIN_MESSAGE, null, filters, "Basic Variance");
		    		curObject.addNewFilter(filterName);
		    		
		    		editor.getFilters().setFilterButtons(curObject);
		    		editor.setShouldReRender(true);
	    		}
	    	}
	    });
	    
	    changeName.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String objectName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Rename Object",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		Frame curFrame = ani.getSelectedFrame();
	    		if (ani.getObjectList().getSelectedIndex() != -1)
	    		{
	    			int index = ani.getObjectList().getSelectedIndex();
		    		if (curFrame.getJointByName(objectName) == null)
		    		{ // Object with this name does not exist
			    		curFrame.getJoint(ani.getObjectList().getSelectedIndex()).setName(objectName);
			    		ani.getObjectList().setListData(curFrame.getJointNames());
			    		ani.getObjectList().setSelectedIndex(index);
			    		nameLabel.setText(objectName);
		    		}
		    		else
		    		{
		    			JOptionPane.showMessageDialog(editor.getFrame(),
		    					"Object with this name already exists", "Invalid input", JOptionPane.WARNING_MESSAGE);
		    		}
	    		}
	    	}
	    });
	}
	
	public JLabel getNameLabel()
	{
		return nameLabel;
	}
	public JPanel getPanel()
	{
		return panel;
	}
}
