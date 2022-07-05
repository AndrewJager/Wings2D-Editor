package com.wings2d.editor.ui.project;

import java.awt.FlowLayout;
import java.sql.Connection;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.skeleton.Skeleton;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIGridPanel;

public class ProjectEdit extends UIGridPanel<ProjectEdit>{
	public static String CARD_ID = "Project";

	private ProjectSelect projectSelect;
	private CurrentItemEdit currentItemEdit;
	private ProjectItems projectItems;
	private JSplitPane horizontal, vertical;

	public ProjectEdit(final Editor edit, final Connection con, final EditorSettings settings) {
		super(edit, 5, 8);
		
		panel.setLayout(new FlowLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		projectSelect = new ProjectSelect(this, con, settings);
		currentItemEdit = new CurrentItemEdit(this, con);
		projectItems = new ProjectItems(this, settings, con);
		
		this.getGrid().addChild(projectSelect.getPanel(), 0, 0, 4, 1);
		this.getGrid().addChild(currentItemEdit.getPanel(), 0, 1, 4, 1);
		this.getGrid().addChild(projectItems.getPanel(), 4, 0, 1, 2);
		
//		JScrollPane pane = new JScrollPane(projectSelect.getPanel());
//		JScrollPane pane2 = new JScrollPane(currentItemEdit.getPanel());
//		JScrollPane pane3 = new JScrollPane(projectItems.getPanel());
//		vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, pane2);
//		horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vertical, pane3);
//
//		panel.add(horizontal);
	}
	
	@Override
	public void initElements()
	{
		for (int i = 0; i < elements.size(); i++)
		{
			elements.get(i).createEvents();
		}
	}
	
	public void refreshInfo()
	{
		projectItems.setListItems();
	}
	public void setSelectedSkeleton(final Skeleton skeleton)
	{
		currentItemEdit.setItem(skeleton);
	}
	public ProjectSelect getProjectSelect()
	{
		return projectSelect;
	}
	public CurrentItemEdit getCurrentItemEdit()
	{
		return currentItemEdit;
	}
	public ProjectItems getProjectItems()
	{
		return projectItems;
	}
}
