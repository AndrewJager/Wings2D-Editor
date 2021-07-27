package com.wings2d.editor.ui.project;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.project.ProjectEntity;
import com.wings2d.editor.ui.UIElement;

public class ProjectItems extends UIElement<ProjectEdit>{
	private JList<ProjectEntity> list;

	public ProjectItems(final ProjectEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		DefaultListModel<ProjectEntity> model = new DefaultListModel<ProjectEntity>();
		list = new JList<ProjectEntity>(model);
		panel.add(list);
	}
	
	public void setListItems(final Project project)
	{
		DefaultListModel<ProjectEntity> model = (DefaultListModel<ProjectEntity>)list.getModel();
		model.clear();
		for (int i = 0; i < project.getEntities().size(); i++)
		{
			model.addElement(project.getEntities().get(i));
		}
		
	}

	public void createEvents() {
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				getEditPanel().setSelectedEntity(list.getSelectedValue());
			}
		});
	}
}
