package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIPanel;

public class SettingsEdit extends UIPanel{
	public static String CARD_ID = "Settings";
	
	private SettingsToolBar toolBar;
	private DrawingSettings drawSettings;

	public SettingsEdit(final Editor edit, final EditorSettings settings) {
		super(edit);
		panel.setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		
		toolBar = new SettingsToolBar(this);
		panel.add(toolBar.getToolbar(), BorderLayout.NORTH);
		
		drawSettings = new DrawingSettings(this, settings);
		mainPanel.add(drawSettings.getPanel());
		
		panel.add(mainPanel, BorderLayout.CENTER);
	}

}
