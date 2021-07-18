package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.wings2d.framework.imageFilters.FilterFactory;
import com.wings2d.framework.imageFilters.ImageFilter;

public class CreateFilterDialog extends JDialog{
	private static final long serialVersionUID = 1L;

	private class FiltersCellRenderer extends JLabel implements ListCellRenderer<Class<? extends ImageFilter>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<? extends Class<? extends ImageFilter>> list,
				Class<? extends ImageFilter> value, int index, boolean isSelected, boolean cellHasFocus) {
			this.setText(value.getSimpleName());
			if (isSelected)
			{
				this.setOpaque(true);
				this.setBackground(Color.BLUE);
			}
			else
			{
				this.setOpaque(false);
			}
			return this;
		}
	}
	
	private JButton okBtn;
	private Class<? extends ImageFilter> filterClass;
	
	public CreateFilterDialog(final Frame owner)
	{
		super(owner, true);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		DefaultListModel<Class<? extends ImageFilter>> listItems = new DefaultListModel<Class<? extends ImageFilter>>();
		List<Class<? extends ImageFilter>> classes = FilterFactory.getFilterClasses();
		for (int i = 0; i < classes.size(); i++)
		{
			listItems.addElement(classes.get(i));
		}
		JList<Class<? extends ImageFilter>> filterOptions = new JList<Class<? extends ImageFilter>>(listItems);
		filterOptions.setCellRenderer(new FiltersCellRenderer());
		JScrollPane pane = new JScrollPane(filterOptions);
		this.add(pane);
		
		okBtn = new JButton("Ok");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterClass = filterOptions.getSelectedValue();
				setVisible(false);
				dispose();
			}
		});
		this.add(okBtn);
		this.setSize(new Dimension(200, 300));
		
		filterClass = null;
	}
	
	public Class<? extends ImageFilter> showDialog()
	{
		setVisible(true);
		return filterClass;
	}
}
