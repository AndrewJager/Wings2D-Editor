package editor.ui.project;

import java.awt.Rectangle;

import editor.ui.Editor;
import editor.ui.UIPanel;

public class ProjectEdit extends UIPanel{
	private ProjectSelect projectSelect;

	public ProjectEdit(Editor edit) {
		super(edit);
		projectSelect = new ProjectSelect(this, new Rectangle(0, 0, 400, 100));
	}

}
