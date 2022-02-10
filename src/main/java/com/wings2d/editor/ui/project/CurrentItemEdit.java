package com.wings2d.editor.ui.project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.UIElement;

public class CurrentItemEdit extends UIElement<ProjectEdit>{
	private JLabel nameLabel;
	private JButton editItem, deleteItem;
	private Skeleton curItem;
	
	private Connection con;
	
	public CurrentItemEdit(final ProjectEdit edit, final Connection con) {
		super(edit);
		this.con = con;
		
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		nameLabel = new JLabel("Name");
		panel.add(nameLabel);
		editItem = new JButton("Edit");
		editItem.setEnabled(false);
		panel.add(editItem);
		
		deleteItem = new JButton("Delete");
		deleteItem.setEnabled(false);
		panel.add(deleteItem);
	}
	
	public void setItem(final Skeleton item)
	{
		editItem.setEnabled(true);
		deleteItem.setEnabled(true);
		nameLabel.setText(item.toString());
		curItem = item;
	}

	@Override
	public void createEvents() {
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (curItem != null)
				{
					getEditPanel().getEditor().showSkeleton(curItem);
				}
			}
		});
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (curItem != null) {
					curItem.delete(con);
					getEditPanel().getProjectItems().setListItems();
				}
			}
		});
	}
}
