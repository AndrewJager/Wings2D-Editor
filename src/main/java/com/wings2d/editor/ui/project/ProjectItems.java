package com.wings2d.editor.ui.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

	final Connection con;

	public ProjectItems(final ProjectEdit edit, final EditorSettings settings, final Connection con) {
		super(edit);

		this.con = con;
		
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		DefaultListModel<Skeleton> model = new DefaultListModel<Skeleton>();
		list = new JList<Skeleton>(model);
		list.setPreferredSize(new Dimension(150, 100));
		panel.add(list);
	}
	
	public void setListItems()
	{
		Project project = getEditPanel().getProjectSelect().getProjectList().getSelectedValue();
		DefaultListModel<Skeleton> model = (DefaultListModel<Skeleton>)list.getModel();
		if (project != null) {
			model.clear();
			project.query(con, project.getID());
			List<Skeleton> skeletons = project.getSkeletons();
			for (int i = 0; i < skeletons.size(); i++)
			{
				model.addElement(skeletons.get(i));
			}
		}
		else {
			model.clear();
		}
	}

	@Override
	public void createEvents() {
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (list.getSelectedValue() != null) {
					getEditPanel().setSelectedSkeleton(list.getSelectedValue());
				}
			}
		});
		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					if (list.getSelectedValue() != null) {
						getEditPanel().getEditor().showSkeleton(list.getSelectedValue());
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
	}
}
