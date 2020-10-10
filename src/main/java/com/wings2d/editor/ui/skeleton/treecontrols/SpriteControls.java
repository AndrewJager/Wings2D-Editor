package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JSeparator;

import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;

public class SpriteControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Sprite";
	
	private JButton changeColor, newVertex;
	private Sprite curSprite;

	public SpriteControls(final SkeletonTreeControls controls) {
		super(controls);
		changeColor = new JButton("Change Color");
		newVertex = new JButton("New Vertex");
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		curSprite = (Sprite)node;
		addLabel(curSprite.toString());
		panel.add(rename);
		panel.add(delete);
		panel.add(changeColor);
		panel.add(newVertex);
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
		newVertex.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Sprite sprite = (Sprite)controls.getTree().getLastSelectedPathComponent();
				sprite.addVertex();
			}
		});
	}

}
