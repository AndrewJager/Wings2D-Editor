package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import com.wings2d.editor.ui.DrawingArea;
import com.wings2d.editor.ui.UIElement;

public class RenderAreaPanel extends UIElement<SkeletonEdit>{
	private DrawingArea drawArea;
	private JScrollPane pane;

	public RenderAreaPanel(SkeletonEdit edit) {
		super(edit);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		drawArea = new DrawingArea(edit.getEditor(), DrawingArea.DrawType.RENDER);
		pane = new JScrollPane(drawArea);
		panel.setLayout(new GridLayout(0,1));
		panel.add(pane);
	}
	
	public DrawingArea getDrawArea()
	{
		return drawArea;
	}

	@Override
	public void createEvents() {
		
	}
}
