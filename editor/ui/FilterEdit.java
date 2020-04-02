package editor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import editor.ui.filterEdits.ShadeFromEdit;
import framework.imageFilters.BasicVariance;
import framework.imageFilters.DarkenFrom;
import framework.imageFilters.ImageFilter;
import framework.imageFilters.LightenFrom;
import framework.imageFilters.Outline;
import framework.animation.Joint;

public class FilterEdit {
	private Editor editor;
	private JPanel panel;
	private JPanel internal;
	private JScrollPane pane;
	
	public FilterEdit(Editor edit)
	{
		editor = edit;
		panel = new JPanel();
		panel.setBounds(500, 540, 350, 210);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		internal = new JPanel();
		internal.setLayout(new BoxLayout(internal, BoxLayout.Y_AXIS));
		pane = new JScrollPane(internal, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(pane);
		panel.setLayout(new GridLayout());
	}
	
	public void setFilterButtons(Joint curObject)
	{				
		List<ImageFilter> filters = curObject.getFilters();
		internal.removeAll();
		for (int i = 0; i < filters.size(); i++)
		{
			JPanel btnPanel = new JPanel();
			btnPanel.setPreferredSize(new Dimension(300, 40));
			btnPanel.setBackground(Color.WHITE);
			JLabel number = new JLabel(Integer.toString(i));
			btnPanel.add(number);
			JLabel name = new JLabel(filters.get(i).getFilterName());
			btnPanel.add(name);
			JButton edit = new JButton("Edit");
			btnPanel.add(edit);
			JButton moveUp = new JButton("∧");
			moveUp.setBorder(null);
			moveUp.setBackground(Color.LIGHT_GRAY);
			moveUp.setPreferredSize(new Dimension(15, 15));
			btnPanel.add(moveUp);
			JButton moveDown = new JButton("∨");
			moveDown.setBorder(null);
			moveDown.setBackground(Color.LIGHT_GRAY);
			moveDown.setPreferredSize(new Dimension(15, 15));
			btnPanel.add(moveDown);
			JButton deleteFilter = new JButton("X");
			deleteFilter.setBorder(null);
			deleteFilter.setBackground(Color.RED);
			deleteFilter.setPreferredSize(new Dimension(15, 15));
			btnPanel.add(deleteFilter);
			btnPanel.setLayout(new FlowLayout());
			internal.add(btnPanel);
			
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
		    			ShadeFromEdit edit = new ShadeFromEdit(darken);
			    		filters.set(i, edit.getDarken());
		    		}
		    		else if (filterName == "Lighten From")
		    		{
		    			LightenFrom lighten = (LightenFrom)filters.get(i);
		    			ShadeFromEdit edit = new ShadeFromEdit(lighten);
			    		filters.set(i, edit.getLighten());
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
		    		setFilterButtons(curObject);
		    	}
		    });
		    moveDown.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)moveDown.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		curObject.swapFilters(i,  i + 1);
		    		setFilterButtons(curObject);
		    	}
		    });
		    deleteFilter.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		JLabel num = (JLabel)deleteFilter.getParent().getComponent(0);
		    		int i = Integer.parseInt(num.getText());
		    		curObject.deleteFilter(i);
		    		setFilterButtons(curObject);
		    	}
		    });
		}
		internal.revalidate();
		internal.repaint();
		editor.getFrame().validate();
	}

	
	public JPanel getPanel()
	{
		return panel;
	}
}
