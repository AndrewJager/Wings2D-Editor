package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.DecimalFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.wings2d.editor.objects.skeleton.SkeletonNode;

public class PointControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Point";

	private JPanel xPosPanel, yPosPanel, indexPanel;
	private JFormattedTextField xPos, yPos;
	private JLabel index;
	
	public PointControls(final SkeletonTreeControls controls, final Connection con) {
		super(controls, con);

		xPosPanel = new JPanel();
		xPosPanel.add(new JLabel("X:"));
		xPos = new JFormattedTextField(new DecimalFormat());
		xPosPanel.add(xPos);
		
		yPosPanel = new JPanel();
		yPosPanel.add(new JLabel("Y:"));
		yPos = new JFormattedTextField(new DecimalFormat());
		yPosPanel.add(yPos);
		
		indexPanel = new JPanel();
		indexPanel.add(new JLabel("Index:"));
		index = new JLabel();
		indexPanel.add(index);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		super.updatePanelInfo(node);

	}
	
	@Override
	protected void createOtherEvents() {

	}
}
