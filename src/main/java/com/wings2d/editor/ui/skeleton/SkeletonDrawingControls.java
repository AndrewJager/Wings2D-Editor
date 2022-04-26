package com.wings2d.editor.ui.skeleton;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

import com.wings2d.editor.objects.skeleton.DrawMode;
import com.wings2d.editor.ui.UIElement;

public class SkeletonDrawingControls extends UIElement<SkeletonEdit>{
	private JToggleButton moveBtn, rotateBtn, spriteMoveBtn, spriteEditBtn, snapBtn;
	private SkeletonDrawingPanel drawingPanel;

	public SkeletonDrawingControls(final SkeletonEdit edit, final SkeletonDrawingPanel drawingPanel) {
		super(edit);
		this.drawingPanel = drawingPanel;
		
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
		panel.add(new JSeparator(JSeparator.VERTICAL));
		snapBtn = new JToggleButton("Snap");
		snapBtn.setFont(snapBtn.getFont().deriveFont(fontSize));
		panel.add(snapBtn);
		
		setControls(getEditPanel().getDrawMode());
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
				getEditPanel().setDrawMode(DrawMode.BONE_MOVE);
				drawingPanel.getDrawArea().repaint();
			}
		});
		rotateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControls(DrawMode.BONE_ROTATE);
				getEditPanel().setDrawMode(DrawMode.BONE_ROTATE);
				drawingPanel.getDrawArea().repaint();
			}
		});
		spriteMoveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControls(DrawMode.SPRITE_MOVE);
				getEditPanel().setDrawMode(DrawMode.SPRITE_MOVE);
				drawingPanel.getDrawArea().repaint();
			}
		});
		spriteEditBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setControls(DrawMode.SPRITE_EDIT);
				getEditPanel().setDrawMode(DrawMode.SPRITE_EDIT);
				drawingPanel.getDrawArea().repaint();
			}
		});
		snapBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getEditPanel().getEditor().getSettings().getDrawingLogic().setSnap(snapBtn.isSelected());
			}
		});
	}
	
	public JToggleButton getSnapBtn() {
		return snapBtn;
	}
}
