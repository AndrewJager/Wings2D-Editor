package com.wings2d.editor.ui.skeleton.filterEdits;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.wings2d.framework.imageFilters.ImageFilter;

public abstract class FilterEdit extends JDialog{
	private static final long serialVersionUID = 1L;
	private JButton okBtn;
	private ImageFilter newFilter;
	
	public FilterEdit(final Frame owner)
	{
		super(owner, true);
		this.setLayout(new FlowLayout());
		
		this.setSize(new Dimension(200, 200));
	}
	
	protected void addButtons()
	{
		okBtn = new JButton("Ok");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFilter = getFilter();
				setVisible(false);
				dispose();
			}
		});
		this.add(okBtn);
	}
	
	public ImageFilter showDialog()
	{
		setVisible(true);
		return newFilter;
	}
	
	public abstract ImageFilter getFilter();
}
