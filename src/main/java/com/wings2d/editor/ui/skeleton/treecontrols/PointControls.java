package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.SpritePoint;

public class PointControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Point";
	private SpritePoint point;

	private JPanel xPosPanel, yPosPanel;
	private JFormattedTextField xPos, yPos;
	
	public PointControls(final SkeletonTreeControls controls) {
		super(controls);

		xPosPanel = new JPanel();
		xPosPanel.add(new JLabel("X:"));
		xPos = new JFormattedTextField(new DecimalFormat());
		xPosPanel.add(xPos);
		
		yPosPanel = new JPanel();
		yPosPanel.add(new JLabel("Y:"));
		yPos = new JFormattedTextField(new DecimalFormat());
		yPosPanel.add(yPos);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		super.updatePanelInfo(node);
		point = (SpritePoint)node;
		
		panel.add(xPosPanel);
		xPos.setValue(point.getX());
		
		panel.add(yPosPanel);
		yPos.setValue(point.getY());
	}
	
	@Override
	protected void createOtherEvents() {
		xPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				point.getSprite().setRelativeVertexLocation(Double.parseDouble(xPos.getText()), Double.parseDouble(yPos.getText()), 1.0);
				controls.getSkeleton().getSkeletonTree().reloadModel();
				controls.getSkeleton().getDrawingArea().getDrawingPanel().getDrawArea().repaint();
			}
		});
		yPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				point.getSprite().setRelativeVertexLocation(Double.parseDouble(xPos.getText()), Double.parseDouble(yPos.getText()), 1.0);
				controls.getSkeleton().getSkeletonTree().reloadModel();
				controls.getSkeleton().getDrawingArea().getDrawingPanel().getDrawArea().repaint();
			}
		});
	}
}
