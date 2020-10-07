package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

import com.wings2d.editor.objects.skeleton.DrawMode;

public class SkeletonDrawingControls extends SkeletonUIElement{
	private JToggleButton moveBtn, rotateBtn, spriteMoveBtn, spriteEditBtn;

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
		panel.add(new JSeparator(JSeparator.VERTICAL));
		spriteMoveBtn = new JToggleButton(String.valueOf("\u25ED"));
		spriteMoveBtn.setToolTipText("Move Sprite");
		spriteMoveBtn.setFont(spriteMoveBtn.getFont().deriveFont(fontSize));
		panel.add(spriteMoveBtn);
		spriteEditBtn = new JToggleButton("^");
		spriteEditBtn.setToolTipText("Edit Sprite");
		spriteEditBtn.setFont(spriteMoveBtn.getFont().deriveFont(fontSize));
		panel.add(spriteEditBtn);
		
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
		case SPRITE_MOVE:
			spriteMoveBtn.setSelected(true);
			break;
		case SPRITE_EDIT:
			spriteEditBtn.setSelected(true);
		}
	}
	
	private void deselectAll()
	{
		moveBtn.setSelected(false);
		rotateBtn.setSelected(false);
		spriteMoveBtn.setSelected(false);
		spriteEditBtn.setSelected(false);
	}

	@Override
	public void createEvents() {
		moveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControls(DrawMode.BONE_MOVE);
				skeleton.setDrawMode(DrawMode.BONE_MOVE);
				skeleton.getDrawingArea().getDrawArea().repaint();
			}
		});
		rotateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControls(DrawMode.BONE_ROTATE);
				skeleton.setDrawMode(DrawMode.BONE_ROTATE);
				skeleton.getDrawingArea().getDrawArea().repaint();
			}
		});
		spriteMoveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControls(DrawMode.SPRITE_MOVE);
				skeleton.setDrawMode(DrawMode.SPRITE_MOVE);
				skeleton.getDrawingArea().getDrawArea().repaint();
			}
		});
		spriteEditBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControls(DrawMode.SPRITE_EDIT);
				skeleton.setDrawMode(DrawMode.SPRITE_EDIT);
				skeleton.getDrawingArea().getDrawArea().repaint();
			}
		});
	}
}
