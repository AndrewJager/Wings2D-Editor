package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;

import com.wings2d.editor.ui.UIElement;

public class SkeletonDrawing extends UIElement<SkeletonEdit>{
	private SkeletonDrawingPanel drawingPanel;
	private SkeletonDrawingControls controls;

	public SkeletonDrawing(SkeletonEdit edit) {
		super(edit);
		panel.setLayout(new BorderLayout());
		
		drawingPanel = new SkeletonDrawingPanel(edit);
		panel.add(drawingPanel.getPanel(), BorderLayout.CENTER);
		controls = new SkeletonDrawingControls(edit, drawingPanel);
		panel.add(controls.getPanel(), BorderLayout.SOUTH);
	}

	@Override
	public void createEvents() {
		
	}

	public SkeletonDrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
	public SkeletonDrawingControls getControls() {
		return controls;
	}
}
