package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;

import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIPanel;

public class SettingsEdit extends UIPanel{
	public static String CARD_ID = "Settings";
	
	private SettingsToolBar toolBar;

	public SettingsEdit(Editor edit) {
		super(edit);
		panel.setLayout(new BorderLayout());
		
		toolBar = new SettingsToolBar(this);
		panel.add(toolBar.getToolbar(), BorderLayout.NORTH);
	}

}
