package com.wings2d.editor.ui.project;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.UIElement;

public class ProjectItems extends UIElement<ProjectEdit>{
	private JList<Skeleton> list;

	public ProjectItems(final ProjectEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		DefaultListModel<Skeleton> model = new DefaultListModel<Skeleton>();
		list = new JList<Skeleton>(model);
		list.setPreferredSize(new Dimension(150, 100));
		panel.add(list);
	}
	
	public void setListItems(final Project project, final EditorSettings settings, final Connection con)
	{
		DefaultListModel<Skeleton> model = (DefaultListModel<Skeleton>)list.getModel();
		model.clear();
		List<Skeleton> skeletons = project.getSkeletons(settings, con);
		for (int i = 0; i < skeletons.size(); i++)
		{
			model.addElement(skeletons.get(i));
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
