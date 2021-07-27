package com.wings2d.editor.ui.skeleton;

import java.awt.BorderLayout;
import java.awt.Dimension;

import com.wings2d.editor.ui.UIElement;

public class RenderArea extends UIElement<SkeletonEdit>{
	private RenderAreaPanel renderPanel;
	private RenderAreaControls controls;

	public RenderArea(SkeletonEdit edit) {
		super(edit);
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(400, 400));
		
		renderPanel = new RenderAreaPanel(edit);
		controls = new RenderAreaControls(edit);
		panel.add(renderPanel.getPanel(), BorderLayout.CENTER);
		panel.add(controls.getPanel(), BorderLayout.SOUTH);
	}

	@Override
	public void createEvents() {
		
	}
	
	public RenderAreaPanel getRenderPanel() {
		return renderPanel;
	}
	public RenderAreaControls getControls() {
		return controls;
	}
}
