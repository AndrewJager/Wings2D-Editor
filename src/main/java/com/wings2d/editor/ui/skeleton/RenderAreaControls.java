package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class RenderAreaControls extends SkeletonUIElement{

	private JButton render;

	public RenderAreaControls(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		render = new JButton("Render");
		panel.add(render);
	}

	@Override
	public void createEvents() {
		render.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skeleton.getSkeleton().generateRender(skeleton.getEditor().getUIScale());
				skeleton.getRenderArea().getDrawArea().setDrawItem(skeleton.getSkeletonTree().getFrameToRender());
				skeleton.getRenderArea().getDrawArea().repaint();
			}
		});
		
	}
}
