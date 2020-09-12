package editor.ui.project;

import java.awt.Rectangle;

import editor.objects.project.Project;
import editor.ui.Editor;
import editor.ui.UIPanel;

public class ProjectEdit extends UIPanel{
	private Project project;
	private ProjectSelect projectSelect;

	public ProjectEdit(Editor edit) {
		super(edit);
		project = new Project();
		projectSelect = new ProjectSelect(this, new Rectangle(0, 0, 400, 100));
	}

	public Project getProject()
	{
		return project;
	}
	public void setProject(Project project)
	{
		this.project = project;
	}
	public ProjectSelect getProjectSelect()
	{
		return projectSelect;
	}
}
