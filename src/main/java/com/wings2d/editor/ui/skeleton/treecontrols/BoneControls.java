package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.tree.DefaultTreeModel;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;
import com.wings2d.editor.ui.edits.AddToTree;
import com.wings2d.editor.ui.edits.SetBoneLocation;
import com.wings2d.editor.ui.edits.SetParentBone;

public class BoneControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Bone";
	private SkeletonBone bone;
	
	private JPanel namePanel, parentBone, xPosPanel, yPosPanel, rotPanel, spritesPanel;
	private JComboBox<SkeletonBone> otherBones;
	private JFormattedTextField xPos, yPos, rotation;
	private JButton addSprite;
	

	public BoneControls(final SkeletonTreeControls controls) {
		super(controls);
		namePanel = new JPanel();
		
		parentBone = new JPanel();
		parentBone.add(new JLabel("Parent Bone: "));
		otherBones = new JComboBox<SkeletonBone>();
		parentBone.add(otherBones);
		
		xPosPanel = new JPanel();
		xPosPanel.add(new JLabel("X:"));
		xPos = new JFormattedTextField(new DecimalFormat());
		xPosPanel.add(xPos);
		
		yPosPanel = new JPanel();
		yPosPanel.add(new JLabel("Y:"));
		yPos = new JFormattedTextField(new DecimalFormat());
		yPosPanel.add(yPos);
		
		rotPanel = new JPanel();
		rotPanel.add(new JLabel("Rotation:"));
		rotation = new JFormattedTextField(new DecimalFormat());
		rotPanel.add(rotation);
		
		spritesPanel = new JPanel();
		addSprite = new JButton("New Sprite");
		spritesPanel.add(addSprite);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		super.updatePanelInfo(node);
		bone = (SkeletonBone)node;
		panel.add(namePanel);
		namePanel.removeAll();
		namePanel.add(new JLabel(node.toString()));
		if (bone.getParentSyncedBone() == null)
		{
			JLabel unsynced = new JLabel("(Unsynced)");
			unsynced.setFont(unsynced.getFont().deriveFont(Font.ITALIC));
			namePanel.add(unsynced);
		}
		panel.add(new JSeparator());
		
		rename.setText("Rename Bone");
		panel.add(controlsPanel);

		panel.add(parentBone);
		DefaultComboBoxModel<SkeletonBone> model = new DefaultComboBoxModel<SkeletonBone>(bone.getFrame().getArrayOfBonesExcept(bone));
		otherBones.setModel(model);
		otherBones.setSelectedItem(bone.getParentBone());
		if(bone.getParentSyncedBone() != null)
		{
			rename.setEnabled(false);
			otherBones.setEnabled(false);
		}
		else {
			rename.setEnabled(true);
			otherBones.setEnabled(true);
		}
		panel.add(new JSeparator());

		panel.add(xPosPanel);
		xPos.setValue(bone.getX());
		panel.add(yPosPanel);
		yPos.setValue(bone.getY());
		panel.add(rotPanel);
		rotation.setValue(Math.round(bone.getRotation()));
		panel.add(new JSeparator());
		
		panel.add(spritesPanel);
		panel.add(Box.createRigidArea(new Dimension(0,100)));
		
		setSelectedBone(bone);		
	}

	@Override
	protected void createOtherEvents() {
		otherBones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bone.getParentBone() != (SkeletonBone)otherBones.getSelectedItem()) {
					controls.getSkeleton().getEditor().getEditsManager().edit(new SetParentBone(bone, (SkeletonBone)otherBones.getSelectedItem()));
				}
			}
		});
		addSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					DefaultTreeModel model = (DefaultTreeModel)controls.getTree().getModel();
					try {
						SkeletonBone bone = (SkeletonBone)selectedNode;
						Sprite newSprite = new Sprite("Sprite", bone);

						controls.getSkeleton().getEditor().getEditsManager().edit(new AddToTree(model, newSprite, selectedNode));
						controls.getTree().setSelectionPath(controls.getTree().getSelectionPath().pathByAddingChild(
								selectedNode.getChildAt(selectedNode.getChildCount() - 1)));
						
					}
					catch (IllegalArgumentException exception) {
						JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});	
		xPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controls.getSkeleton().getEditor().getEditsManager().edit(new SetBoneLocation(
						bone, Double.parseDouble(xPos.getText()), bone.getY(), true));
			}
		});
		yPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controls.getSkeleton().getEditor().getEditsManager().edit(new SetBoneLocation(
						bone, bone.getX(), Double.parseDouble(yPos.getText()), true));
			}
		});
		rotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bone.rotate(Double.parseDouble(rotation.getText()));
				controls.getSkeleton().getDrawingArea().getDrawingPanel().getDrawArea().repaint();
			}
		});
	}

	@Override 
	protected void updateVisibleInfo(final SkeletonNode node)
	{
		SkeletonBone bone = (SkeletonBone)node;
		if (bone != null)
		{
			xPos.setValue(bone.getX());
			yPos.setValue(bone.getY());
			rotation.setValue(Math.round(bone.getRotation()));
		}
	}
}
