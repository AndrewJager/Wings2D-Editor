package com.wings2d.editor.ui.project;

import java.awt.FlowLayout;
import java.sql.Connection;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.project.ProjectEntity;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIPanel;

public class ProjectEdit extends UIPanel<ProjectEdit>{
	public static String CARD_ID = "Project";
	
	private Project project;
	private ProjectSelect projectSelect;
	private CurrentItemEdit currentItemEdit;
	private ProjectItems projectItems;
	private JSplitPane horizontal, vertical;
	
	private Connection con;
	private EditorSettings settings;

	public ProjectEdit(final Editor edit, final Connection con, final EditorSettings settings) {
		super(edit);
		this.con = con;
		this.settings = settings;
		
		panel.setLayout(new FlowLayout());
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		projectSelect = new ProjectSelect(this, con, settings);
		currentItemEdit = new CurrentItemEdit(this);
		projectItems = new ProjectItems(this);
		
		JScrollPane pane = new JScrollPane(projectSelect.getPanel());
		JScrollPane pane2 = new JScrollPane(currentItemEdit.getPanel());
		JScrollPane pane3 = new JScrollPane(projectItems.getPanel());
		vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pane, pane2);
		horizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, vertical, pane3);

		panel.add(horizontal);
	}
	
	public void initElements()
	{
		for (int i = 0; i < elements.size(); i++)
		{
			elements.get(i).createEvents();
		}
	}
	
	public void refreshInfo()
	{
		projectItems.setListItems(project, settings, con);
	}
	public void setSelectedEntity(final ProjectEntity entity)
	{
		currentItemEdit.setItem(entity);
	}

	public Project getProject()
	{
		return project;
	}
	public void setProject(final Project project)
	{
		this.project = project;
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
