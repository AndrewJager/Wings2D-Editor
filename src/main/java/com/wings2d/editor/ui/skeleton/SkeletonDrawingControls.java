package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;

import com.wings2d.editor.objects.skeleton.DrawMode;

public class SkeletonDrawingControls extends SkeletonUIElement{
	private JToggleButton moveBtn, rotateBtn, spriteBtn;

	public SkeletonDrawingControls(SkeletonEdit edit, Rectangle bounds) {
		super(edit, bounds);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		float fontSize = 20f;
		moveBtn = new JToggleButton(String.valueOf("\u2B82"));
		moveBtn.setToolTipText("Move Bones");
		moveBtn.setFont(moveBtn.getFont().deriveFont(fontSize));
		panel.add(moveBtn);
		rotateBtn = new JToggleButton(String.valueOf("\u2B6E"));
		rotateBtn.setToolTipText("Rotate Bones");
		rotateBtn.setFont(rotateBtn.getFont().deriveFont(fontSize));
		panel.add(rotateBtn);
		spriteBtn = new JToggleButton(String.valueOf("\u25ED"));
		spriteBtn.setToolTipText("Sprite Edit");
		spriteBtn.setFont(spriteBtn.getFont().deriveFont(fontSize));
		panel.add(spriteBtn);
		
		setControls(skeleton.getDrawMode());
	}
	
	public void setControls(final DrawMode mode)
	{
		deselectAll();
		switch(mode)
		{
		case BONE_MOVE:
			moveBtn.setSelected(true);
			break;
		case BONE_ROTATE:
			rotateBtn.setSelected(true);
			break;
		case SPRITE:
			spriteBtn.setSelected(true);
			break;
		}
	}
	
	private void deselectAll()
	{
		moveBtn.setSelected(false);
		rotateBtn.setSelected(false);
		spriteBtn.setSelected(false);
	}

	@Override
	public void createEvents() {
		moveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rotateBtn.setSelected(false);
				spriteBtn.setSelected(false);	
				skeleton.setDrawMode(DrawMode.BONE_MOVE);
				skeleton.getDrawingArea().getDrawArea().repaint();
			}
		});
		rotateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveBtn.setSelected(false);
				spriteBtn.setSelected(false);
				skeleton.setDrawMode(DrawMode.BONE_ROTATE);
				skeleton.getDrawingArea().getDrawArea().repaint();
			}
		});
		spriteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveBtn.setSelected(false);
				rotateBtn.setSelected(false);
				skeleton.setDrawMode(DrawMode.SPRITE);
				skeleton.getDrawingArea().getDrawArea().repaint();
			}
		});
	}
}
