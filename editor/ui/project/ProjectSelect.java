package editor.ui.project;

import java.awt.Color;
import java.awt.Rectangle;

public class ProjectSelect extends ProjectUIElement{

	public ProjectSelect(ProjectEdit edit, Rectangle bounds) {
		super(edit, bounds);
		panel.setBackground(Color.RED);
	}

	@Override
	public void createEvents() {
		// TODO Auto-generated method stub
		
	}
}
