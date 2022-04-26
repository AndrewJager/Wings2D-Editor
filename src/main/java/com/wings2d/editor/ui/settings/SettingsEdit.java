package com.wings2d.editor.ui.settings;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.wings2d.editor.objects.EditorSettings;
import com.wings2d.editor.ui.Editor;
import com.wings2d.editor.ui.UIPanel;

public class SettingsEdit extends UIPanel<SettingsEdit>{
	public static String CARD_ID = "Settings";
	
	private SettingsToolBar toolBar;
	private DrawingSettings drawSettings;
	private JButton saveBtn;

	public SettingsEdit(final Editor edit, final EditorSettings settings) {
		super(edit);
		panel.setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		
		toolBar = new SettingsToolBar(this);
		panel.add(toolBar.getToolbar(), BorderLayout.NORTH);
		
		drawSettings = new DrawingSettings(this, settings);
		mainPanel.add(drawSettings.getPanel());
		panel.add(mainPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		saveBtn = new JButton("Save");
		southPanel.add(saveBtn);
		panel.add(southPanel, BorderLayout.SOUTH);
		
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.update(edit.getConnection());
				
			}
		});
	}

}
