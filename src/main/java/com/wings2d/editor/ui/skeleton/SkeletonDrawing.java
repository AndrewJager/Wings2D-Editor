package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Dimension;

import com.wings2d.editor.ui.skeleton.treecontrols.SkeletonTreeControls;

public class SkeletonDrawing extends SkeletonUIElement{
	private SkeletonDrawingPanel drawingPanel;
	private SkeletonDrawingControls controls;

	public SkeletonDrawing(SkeletonEdit edit) {
		super(edit);
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(400, 400));
		
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
	
	public void setTreeControls(final SkeletonTreeControls controls) {
		drawingPanel.setTreeControls(controls);
	}
}
