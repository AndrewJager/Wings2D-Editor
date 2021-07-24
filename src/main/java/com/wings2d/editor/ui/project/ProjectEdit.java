package com.wings2d.editor.ui.project;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.wings2d.editor.objects.project.Project;
import com.wings2d.editor.objects.project.ProjectEntity;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIPanel;

public class ProjectEdit extends UIPanel{
	public static String CARD_ID = "Project";
	
	private Project project;
	private ProjectSelect projectSelect;
	private CurrentItemEdit currentItemEdit;
	private ProjectItems projectItems;
	protected List<ProjectUIElement> elements;
	private JSplitPane horizontal, vertical;

	public ProjectEdit(final Editor edit) {
		super(edit);
		elements = new ArrayList<ProjectUIElement>();
		panel.setLayout(new FlowLayout());
		projectSelect = new ProjectSelect(this);
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
		projectItems.setListItems(project);
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
	public List<ProjectUIElement> Elements()
	{
		return elements;
	}
}
