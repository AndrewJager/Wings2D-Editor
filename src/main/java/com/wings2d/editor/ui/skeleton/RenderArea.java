package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

public class RenderArea extends SkeletonUIElement{

	public RenderArea(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	@Override
	public void createEvents() {
		// TODO Auto-generated method stub
		
	}
}
