package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.wings2d.editor.ui.UIElement;

public class RenderAreaControls extends UIElement<SkeletonEdit>{

	private JButton render;

	public RenderAreaControls(SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		render = new JButton("Render");
		panel.add(render);
	}

	@Override
	public void createEvents() {
		render.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEditPanel().getSkeleton().generateRender(getEditPanel().getEditor().getUIScale());
				getEditPanel().getRenderArea().getRenderPanel().getDrawArea().setDrawItem(getEditPanel().getSkeletonTree().getFrameToRender());
				getEditPanel().getRenderArea().getRenderPanel().getDrawArea().repaint();
			}
		});
		
	}
}
