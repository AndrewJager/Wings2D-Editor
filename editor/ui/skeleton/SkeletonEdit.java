package editor.ui.skeleton;

import java.awt.Color;

import editor.ui.Editor;
import editor.ui.UIPanel;

public class SkeletonEdit extends UIPanel{

	public SkeletonEdit(Editor edit) {
		super(edit);
		panel.setBackground(Color.RED);
		
		for (int i = 0; i < elements.size(); i++)
		{
			elements.get(i).createEvents();
			panel.add(elements.get(i).getPanel());
		}
	}

}
