package com.wings2d.editor.ui.project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.wings2d.editor.objects.project.ProjectEntity;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.UIElement;

public class CurrentItemEdit extends UIElement<ProjectEdit>{
	private JLabel nameLabel;
	private JButton editItem;
	private ProjectEntity selectedItem;
	
	public CurrentItemEdit(final ProjectEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		nameLabel = new JLabel("Name");
		panel.add(nameLabel);
		editItem = new JButton("Edit");
		editItem.setEnabled(false);
		panel.add(editItem);
	}
	
	public void setItem(final ProjectEntity item)
	{
		editItem.setEnabled(true);
		if (item instanceof Skeleton)
		{
			nameLabel.setText(item.toString());
			selectedItem = item;
		}
	}

	public void createEvents() {
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedItem != null)
				{
					if (selectedItem instanceof Skeleton)
					{
						getEditPanel().getEditor().showSkeleton((Skeleton)selectedItem);
					}
				}
			}
		});
	}
}
