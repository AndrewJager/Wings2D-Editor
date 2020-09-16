package editor.ui.project;

import java.awt.Rectangle;

import editor.objects.project.Project;
import editor.ui.Editor;
import editor.ui.UIPanel;

public class ProjectEdit extends UIPanel{
	public static String CARD_ID = "Project";
	
	private Project project;
	private ProjectSelect projectSelect;
	private CurrentItemEdit currentItemEdit;

	public ProjectEdit(final Editor edit) {
		super(edit);
		project = new Project();
		projectSelect = new ProjectSelect(this, new Rectangle(0, 0, 400, 100));
		currentItemEdit = new CurrentItemEdit(this, new Rectangle(0, 100, 400, 100));
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
}
