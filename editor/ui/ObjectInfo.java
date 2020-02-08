package editor.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import editor.objects.Frame;
import editor.objects.Item;

public class ObjectInfo {
	private JPanel panel;
	private JLabel infoLabel;
	private JButton addVertex;
	private JButton setColor;
	private JButton addFilter;

	public ObjectInfo()
	{
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
	
	public void createEvents(AnimationLists ani, DrawingArea drawing, JFrame frame, FilterEdit filter, Editor editor)
	{
		addVertex.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			Frame curFrame = editor.getActiveSprite().getAnimation(ani.getAnimList().getSelectedIndex()).getFrame(ani.getFrameList().getSelectedIndex());
	    			curFrame.addVertex(ani.getObjectList().getSelectedIndex());
	    			editor.updateDrawing(ani);
	    			editor.setShouldReRender(true);
	    		}
	    	}
	    });
	    setColor.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			Frame curFrame = editor.getActiveSprite().getAnimation(ani.getAnimList().getSelectedIndex()).getFrame(ani.getFrameList().getSelectedIndex());
	    			Color color = JColorChooser.showDialog(frame, "Select a color", curFrame.getObject(ani.getObjectList().getSelectedIndex()).getColor());    
	    			curFrame.setColor(ani.getObjectList().getSelectedIndex(), color); 
	    			editor.updateDrawing(ani);
	    			editor.setShouldReRender(true);
	    		}
	    	}
	    });
        
	    addFilter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (ani.getIsObjectSelected())
	    		{
	    			Frame curFrame = editor.getActiveSprite().getAnimation(ani.getAnimList().getSelectedIndex()).getFrame(ani.getFrameList().getSelectedIndex());
	    			Item curObject = curFrame.getObject(ani.getObjectList().getSelectedIndex());
		    		Object[] filters = {"Basic Variance", "Blur Edges", "Darken From", "Lighten From", "Outline"};
		    		String filterName = (String)JOptionPane.showInputDialog(frame, "","Choose Filter",
		    				JOptionPane.PLAIN_MESSAGE, null, filters, "Basic Variance");
		    		curObject.addNewFilter(filterName);
		    		
		    		filter.setFilterButtons(filter.getPanel(), frame, curObject, curObject.getFilters());
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
