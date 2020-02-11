package editor.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import editor.objects.Frame;
import editor.objects.Item;

public class ObjectInfo {
	private Editor editor;
	private JPanel panel;
	private JLabel infoLabel;
	private JButton addVertex;
	private JButton setColor;
	private JButton addFilter;

	public ObjectInfo(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(500, 430, 350, 100);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		infoLabel = new JLabel("Object Info");
		panel.add(infoLabel);
		addVertex = new JButton("Add vertix");
		panel.add(addVertex);
		setColor = new JButton("Change color");
		panel.add(setColor);
		addFilter = new JButton("Add filter");
		panel.add(addFilter);
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
	    			Item obj = ani.getSelectedObject();
	    			Color color = JColorChooser.showDialog(editor.getFrame(), "Select a color", obj.getColor());    
	    			obj.setColor(color);
	    			editor.getDrawing().updateDrawing();
	    			editor.setShouldReRender(true);
	    		}
	    	}
	    });
        
	    addFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			Item curObject = ani.getSelectedObject();
		    		Object[] filters = {"Basic Variance", "Blur Edges", "Darken From", "Lighten From", "Outline"};
		    		String filterName = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Choose Filter",
		    				JOptionPane.PLAIN_MESSAGE, null, filters, "Basic Variance");
		    		curObject.addNewFilter(filterName);
		    		
		    		editor.getFilters().setFilterButtons(curObject);
		    		editor.setShouldReRender(true);
	    		}
	    	}
	    });
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
