package com.wings2d.editor.ui.skeleton.treecontrols;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.wings2d.editor.objects.skeleton.SkeletonBone;
import com.wings2d.editor.objects.skeleton.SkeletonNode;
import com.wings2d.editor.objects.skeleton.Sprite;

public class BoneControls extends SkeletonTreeControlsUIElement{
	public static final String CARD_ID = "Bone";
	
	private JButton addSprite;
	private JLabel xPos, yPos, rotation;

	public BoneControls(final SkeletonTreeControls controls) {
		super(controls);
		addSprite = new JButton("New Sprite");
		
		xPos = new JLabel("X: ", JLabel.CENTER);
		yPos = new JLabel("Y: ", JLabel.CENTER);
		rotation = new JLabel("Rotation: ", JLabel.CENTER);
	}

	@Override
	protected void updatePanelInfo(final SkeletonNode node) {
		SkeletonBone bone = (SkeletonBone)node;
		addLabel(node.toString());
		if (bone.getParentSyncedBone() == null)
		{
			addLabel("Unsynced");
		}
		addNameLine();
		rename.setText("Rename Bone");
		panel.add(rename);
		JLabel parentBoneLabel = new JLabel("Parent bone:");
		panel.add(parentBoneLabel);
		JComboBox<SkeletonBone> otherBones = new JComboBox<SkeletonBone>(bone.getFrame().getArrayOfBonesExcept(bone));
		otherBones.setSelectedItem(bone.getParentBone());
		otherBones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bone.setParentBone((SkeletonBone)otherBones.getSelectedItem());
			}
		});
		panel.add(otherBones);
		if(bone.getParentSyncedBone() != null)
		{
			rename.setEnabled(false);
			otherBones.setEnabled(false);
		}
		addNameLine();
		addLabel(xPos, "X: " + bone.getX());
		addLabel(yPos, "Y: " + bone.getY());
		addLabel(rotation, "Rotation: " + Math.round(bone.getRotation()));
		panel.add(addSprite);
		
		bone.getFrame().setSelectedBone(bone);
		controls.getDrawingArea().setSelectedFrame(bone.getFrame());
		controls.getDrawingArea().getDrawArea().setDrawItem(bone.getFrame());		
	}

	@Override
	protected void createOtherEvents() {
		addSprite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SkeletonNode selectedNode = (SkeletonNode)controls.getTree().getLastSelectedPathComponent();
				if (selectedNode != null)
				{
					DefaultTreeModel model = (DefaultTreeModel)controls.getTree().getModel();
					TreePath path = controls.getTree().getSelectionPath();
					try {
						SkeletonBone bone = (SkeletonBone)selectedNode;
						Sprite newSprite = new Sprite("Sprite", bone);
						model.insertNodeInto((MutableTreeNode)newSprite,
								selectedNode, selectedNode.getChildCount());
						model.reload();
										
						controls.getTree().expandPath(path);
						controls.getTree().setSelectionPath(path.pathByAddingChild(selectedNode.getChildAt(selectedNode.getChildCount() - 1)));
					}
					catch (IllegalArgumentException exception) {
						JOptionPane.showMessageDialog(panel, exception.getMessage(), "Insert Failed!", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});		
	}

	@Override 
	protected void updateVisibleInfo(final SkeletonNode node)
	{
		SkeletonBone bone = (SkeletonBone)node;
		if (bone != null)
		{
			xPos.setText("X: " + bone.getX());
			yPos.setText("Y: " + bone.getY());
			rotation.setText("Rotation: " + Math.round(bone.getRotation()));
		}
	}
}
