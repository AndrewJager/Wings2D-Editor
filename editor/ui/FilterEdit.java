package editor.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import editor.objects.Item;
import framework.imageFilters.BasicVariance;
import framework.imageFilters.DarkenFrom;
import framework.imageFilters.ImageFilter;
import framework.imageFilters.LightenFrom;
import framework.imageFilters.Outline;
import framework.imageFilters.ShadeDir;

public class FilterEdit {
	private Editor editor;
	private JPanel panel;
	
	public FilterEdit(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(500, 540, 350, 210);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setLayout(new FlowLayout());
	}
	
	public void setFilterButtons(Item curObject, List<ImageFilter> filters)
	{
		panel.removeAll();
		for (int i = 0; i < filters.size(); i++)
		{
			JPanel btnPanel = new JPanel();
			btnPanel.setSize(100, 40);
			btnPanel.setBackground(Color.WHITE);
			JLabel number = new JLabel(Integer.toString(i));
			btnPanel.add(number);
			JLabel name = new JLabel(filters.get(i).getFilterName());
			btnPanel.add(name);
			JButton edit = new JButton("Edit");
			btnPanel.add(edit);
			JButton moveUp = new JButton("∧");
			btnPanel.add(moveUp);
			JButton moveDown = new JButton("∨");
			btnPanel.add(moveDown);
			btnPanel.setLayout(new FlowLayout());
			panel.add(btnPanel);
			
		    edit.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)moveUp.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		ImageFilter filter = filters.get(i);
		    		String filterName = filter.getFilterName();
		    		if (filterName == "Basic Variance")
		    		{
		    			BasicVariance variance = (BasicVariance)filters.get(i);
			    		String varAmount = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Variance amount",
			    				JOptionPane.PLAIN_MESSAGE, null, null, variance.getVarAmt());
			    		int varAsInt;
			    		try {
			    		   varAsInt = Integer.parseInt(varAmount);
			    		}
			    		catch (NumberFormatException ex)
			    		{
			    		   varAsInt = 1;
			    		   JOptionPane.showMessageDialog(editor.getFrame(),
			    				    ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
			    		}
			    		filters.set(i, new BasicVariance(varAsInt));
		    		}
		    		else if (filterName == "Blur Edges")
		    		{
			    		   JOptionPane.showMessageDialog(editor.getFrame(),
			    				    "Nothing to change for this filter", "Go home", JOptionPane.PLAIN_MESSAGE);
		    		}
		    		else if (filterName == "Darken From")
		    		{
		    			DarkenFrom darken = (DarkenFrom)filters.get(i);
			    		Object[] directions = {"Left", "Right", "Top", "Bottom"};
			    		String filterDir = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Choose Direction",
			    				JOptionPane.PLAIN_MESSAGE, null, directions, ShadeDir.getAsString(darken.getDirection()));
			    		String varAmount = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Variance amount",
			    				JOptionPane.PLAIN_MESSAGE, null, null, darken.getAmt());
			    		int varAsInt;
			    		try {
			    		   varAsInt = Integer.parseInt(varAmount);
			    		}
			    		catch (NumberFormatException ex)
			    		{
			    		   varAsInt = 1;
			    		   JOptionPane.showMessageDialog(editor.getFrame(),
			    				    ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
			    		}
			    		filters.set(i, new DarkenFrom(ShadeDir.createFromString(filterDir), varAsInt));
		    		}
		    		else if (filterName == "Lighten From")
		    		{
		    			LightenFrom lighten = (LightenFrom)filters.get(i);
			    		Object[] directions = {"Left", "Right", "Top", "Bottom"};
			    		String filterDir = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Choose Direction",
			    				JOptionPane.PLAIN_MESSAGE, null, directions, ShadeDir.getAsString(lighten.getDirection()));
			    		String varAmount = (String)JOptionPane.showInputDialog(editor.getFrame(), "","Amount",
			    				JOptionPane.PLAIN_MESSAGE, null, null, lighten.getAmt());
			    		int varAsInt;
			    		try {
			    		   varAsInt = Integer.parseInt(varAmount);
			    		}
			    		catch (NumberFormatException ex)
			    		{
			    		   varAsInt = 1;
			    		   JOptionPane.showMessageDialog(editor.getFrame(),
			    				    ex.getMessage(), "Invalid input", JOptionPane.WARNING_MESSAGE);
			    		}
			    		filters.set(i, new LightenFrom(ShadeDir.createFromString(filterDir), varAsInt));
		    		}
		    		else if (filterName == "Outline")
		    		{
		    			Outline outline = (Outline)filters.get(i);
		    			Color color = JColorChooser.showDialog(editor.getFrame(), "Select a color", outline.getColor());
		    			filters.set(i, new Outline(color));
		    		}
		    	}
		    });
		    moveUp.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)moveUp.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		curObject.swapFilters(i,  i - 1);
		    		setFilterButtons(curObject, filters);
		    	}
		    });
		    moveDown.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)moveUp.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		curObject.swapFilters(i,  i + 1);
		    		setFilterButtons(curObject, filters);
		    	}
		    });
		}

		editor.getFrame().validate();
	}

	
	public JPanel getPanel()
	{
		return panel;
	}
}
