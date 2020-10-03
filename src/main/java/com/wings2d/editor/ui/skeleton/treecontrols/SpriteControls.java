package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;

public class SpriteControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Sprite";
	
	private JButton changeColor;
	private Sprite curSprite;

	public SpriteControls(final SkeletonTreeControls controls) {
		super(controls);
		changeColor = new JButton("Change Color");
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		curSprite = (Sprite)node;
		addLabel(curSprite.toString());
		panel.add(changeColor);
	}

	@Override
	protected void createOtherEvents() {
		changeColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(panel, "Choose Sprite color", curSprite.getColor());
				if (color != null)
				{
					curSprite.setColor(color);
					controls.getDrawingArea().getDrawArea().repaint();
				}
			}
		});
	}

}
