package editor.ui.project;

import java.awt.Rectangle;

import editor.objects.project.Project;
import editor.objects.project.ProjectEntity;
import editor.ui.Editor;
import editor.ui.UIPanel;

public class ProjectEdit extends UIPanel{
	public static String CARD_ID = "Project";
	
	private Project project;
	private ProjectSelect projectSelect;
	private CurrentItemEdit currentItemEdit;
	private ProjectItems projectItems;

	public ProjectEdit(final Editor edit) {
		super(edit);
		projectSelect = new ProjectSelect(this, new Rectangle(0, 0, 400, 100));
		currentItemEdit = new CurrentItemEdit(this, new Rectangle(0, 100, 400, 100));
		projectItems = new ProjectItems(this, new Rectangle(400, 0, 200, 200));
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
}
