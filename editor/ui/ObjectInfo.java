package editor.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import editor.objects.EditorFrame;
import editor.objects.EditorJoint;

public class ObjectInfo extends UIElement{
	private JLabel infoLabel;
	private JLabel nameLabel;
	private JLabel orderLabel;
	private JButton addVertex;
	private JButton setColor;
	private JButton addFilter;
	private JButton changeName;
	private JComboBox<String> selectParent;

	public ObjectInfo(Editor edit, Rectangle bounds)
	{
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		infoLabel = new JLabel("Object Info");
		infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 14f));
		panel.add(infoLabel);
		nameLabel = new JLabel("Object name");
		panel.add(nameLabel);
		orderLabel = new JLabel("Object order");
		panel.add(orderLabel);
		addVertex = new JButton("Add vertix");
		panel.add(addVertex);
		setColor = new JButton("Change color");
		panel.add(setColor);
		addFilter = new JButton("Add filter");
		panel.add(addFilter);
		changeName = new JButton("Change name");
		panel.add(changeName);
		selectParent = new JComboBox<String>();
		panel.add(selectParent);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	}
	
	public void createEvents()
	{
		AnimationLists ani = editor.getAnimLists();
		addVertex.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			EditorFrame curFrame = ani.getSelectedFrame();
	    			curFrame.addVertex(ani.getObjectList().getSelectedIndex());
	    			editor.getDrawing().setShouldRedraw(true);
	    			editor.getRender().setShouldReRender(true);
	    		}
	    	}
	    });
	    setColor.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    		    EditorJoint joint = ani.getSelectedObject();
	    			Color color = JColorChooser.showDialog(editor.getFrame(), "Select a color", joint.getColor());    
	    			joint.setColor(color);
	    			editor.getDrawing().setShouldRedraw(true);
	    			editor.getRender().setShouldReRender(true);
	    		}
	    	}
	    });
        
	    addFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			EditorJoint curObject = ani.getSelectedObject();
		    		Object[] filters = {"Basic Variance", "Blur Edges", "Darken From", "Lighten From", "Outline"};
		    		String filterName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Choose Filter",
		    				JOptionPane.PLAIN_MESSAGE, null, filters, "Basic Variance");
		    		curObject.addNewFilter(filterName);
		    		
		    		editor.getFilters().setFilterButtons(curObject);
		    		editor.getRender().setShouldReRender(true);
	    		}
	    	}
	    });
	    
	    changeName.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String objectName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Rename Object",
	    				JOptionPane.PLAIN_MESSAGE, null, null, "Object");
	    		EditorFrame curFrame = ani.getSelectedFrame();
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
	    
	    selectParent.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (selectParent.getSelectedIndex() == 0)
	    		{
	    			ani.getSelectedObject().setParent(null);
	    		}
	    		else
	    		{
	    			ani.getSelectedObject().setParent(ani.getSelectedFrame().getJointByName(selectParent.getSelectedItem().toString()));
	    		}
	    	}
	    });
	}
	
	public void updateInfo(EditorJoint joint)
	{
		nameLabel.setText("Name: " + joint.getName());
		orderLabel.setText("Order: " + joint.getRenderOrder());
		
		String[] allJointNames = joint.getFrame().getJointNames(); 
		String[] otherJointNames = new String[allJointNames.length - 1];
		int jointsAdded = 0;
		for (int i = 0; i < allJointNames.length; i++)
		{
			if (!allJointNames[i].equals(joint.getName()))
			{
				otherJointNames[jointsAdded] = allJointNames[i];
				jointsAdded++;
			}
		}
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(otherJointNames);
		selectParent.setModel(model);
		selectParent.insertItemAt("<None>", 0);
		if (joint.getParent() == null)
		{
			selectParent.setSelectedIndex(0);
		}
		else
		{
			selectParent.setSelectedItem(joint.getParent());
		}
	}
	
	public JLabel getNameLabel()
	{
		return nameLabel;
	}
	public JComboBox<String> getSelectParent() {
		return selectParent;
	}
}
